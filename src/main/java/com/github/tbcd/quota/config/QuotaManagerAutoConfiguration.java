package com.github.tbcd.quota.config;

import com.github.tbcd.quota.*;
import com.github.tbcd.quota.aspect.QuotaAspect;
import com.github.tbcd.quota.impl.DefaultQuotaManager;
import com.github.tbcd.quota.impl.DefaultResourceCounterRegistry;
import com.github.tbcd.quota.impl.EmptyQuotaLimitResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@AutoConfiguration
public class QuotaManagerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ResourceCounterRegistry resourceCounterRegistry(@Lazy List<ResourceCounter> resourceCounters) {
		return new DefaultResourceCounterRegistry(resourceCounters);
	}

	@Bean
	@ConditionalOnMissingBean
	public QuotaLimitResolver quotaLimitResolver() {
		return new EmptyQuotaLimitResolver();
	}

	@Bean
	@ConditionalOnMissingBean
	public QuotaManager quotaManager(ResourceCounterRegistry resourceCounterRegistry, QuotaLimitResolver quotaLimitResolver) {
		return new DefaultQuotaManager(resourceCounterRegistry, quotaLimitResolver);
	}

	@Bean
	@ConditionalOnMissingBean
	public TenantResolver tenantResolver() {
		return () -> {
			throw new IllegalStateException("No TenantResolver configured. Please provide a TenantResolver bean.");
		};
	}

	@Bean
	@ConditionalOnMissingBean
	public QuotaAspect quotaAspect(QuotaManager quotaManager, TenantResolver tenantResolver) {
		return new QuotaAspect(quotaManager, tenantResolver);
	}
}
