package com.github.tbcd.quota.aspect;

import com.github.tbcd.quota.QuotaManager;
import com.github.tbcd.quota.QuotaResult;
import com.github.tbcd.quota.Tenant;
import com.github.tbcd.quota.TenantResolver;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class QuotaAspect {

	private final QuotaManager quotaManager;
	private final TenantResolver tenantResolver;

	public QuotaAspect(QuotaManager quotaManager,
					   TenantResolver tenantResolver
	) {
		this.quotaManager = quotaManager;
		this.tenantResolver = tenantResolver;
	}

	@Before("@annotation(quota)")
	public void checkResourceQuota(Quota quota) {
		Tenant tenant = tenantResolver.resolve();
		Class<?> resourceType = quota.resource();
		long cost = quota.cost();

		QuotaResult result = quotaManager.check(tenant, resourceType, cost);

		if (!result.allowed()) {
			String message = quota.message().isEmpty() ? "Quota exceeded for " + resourceType.getSimpleName() : quota.message();
			throw new QuotaExceededException(message, resourceType.getSimpleName(), result.state().getUsed(), result.state().getLimit());
		}
	}
}
