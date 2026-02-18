package com.github.tbcd.quota.aspect;

import com.github.tbcd.quota.QuotaManager;
import com.github.tbcd.quota.QuotaResult;
import com.github.tbcd.quota.Tenant;
import com.github.tbcd.quota.TenantResolver;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Aspect
public class QuotaAspect {

	private final QuotaManager quotaManager;
	private final TenantResolver tenantResolver;
	private final ExpressionParser expressionParser = new SpelExpressionParser();
	private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

	public QuotaAspect(QuotaManager quotaManager,
					   TenantResolver tenantResolver
	) {
		this.quotaManager = quotaManager;
		this.tenantResolver = tenantResolver;
	}

	@Before("@annotation(quota)")
	public void checkResourceQuota(JoinPoint joinPoint, Quota quota) {
		Tenant tenant = resolveTenant(joinPoint, quota);
		Object resource = evaluateExpression(joinPoint, quota.resource());
		long cost = quota.cost();

		QuotaResult result = quotaManager.check(tenant, resource, cost);

		if (!result.allowed()) {
			String resourceName = resource.getClass().getSimpleName();
			String message = quota.message().isEmpty() ? "Quota exceeded for " + resourceName : quota.message();
			throw new QuotaExceededException(message, resourceName, result.state().getUsed(), result.state().getLimit());
		}
	}

	private Tenant resolveTenant(JoinPoint joinPoint, Quota quota) {
		String tenantExpr = quota.tenant();

		if (tenantExpr == null || tenantExpr.isEmpty()) {
			return tenantResolver.resolve();
		}

		Object result = evaluateExpression(joinPoint, tenantExpr);

		if (result == null) {
			throw new IllegalArgumentException("Tenant expression resolved to null");
		}

		if (result instanceof Tenant tenant) {
			return tenant;
		}

		return () -> result;
	}

	private Object evaluateExpression(JoinPoint joinPoint, String expression) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String[] parameterNames = parameterNameDiscoverer.getParameterNames(signature.getMethod());
		Object[] args = joinPoint.getArgs();

		EvaluationContext context = new StandardEvaluationContext();

		if (parameterNames != null) {
			for (int i = 0; i < parameterNames.length; i++) {
				context.setVariable(parameterNames[i], args[i]);
			}
		}

		return expressionParser.parseExpression(expression).getValue(context);
	}
}