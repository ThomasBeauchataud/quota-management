package com.github.tbcd.quota.aspect;

import com.github.tbcd.quota.QuotaManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QuotaAspect {

//	private final QuotaManager quotaManager;
//
//	public QuotaAspect(QuotaManager quotaManager) {
//		this.quotaManager = quotaManager;
//	}
//
//	@Before("@annotation(quota)")
//	public void checkResourceQuota(Quota quota) {
//		Class<?> resourceType = quota.resource();
//		QuotaService resourceQuotaService = quotaManager.c(resourceType);
//		if (resourceQuotaService.count() >= resourceQuotaService.getAuthorizedQuota()) {
//			String message = quota.message().isEmpty() ? "Quota exceeded" : quota.message();
//			throw new QuotaExceededException(message, resourceType.getSimpleName(), resourceQuotaService.count(), resourceQuotaService.getAuthorizedQuota());
//		}
//	}
}
