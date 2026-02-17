package com.github.tbcd.quota.config;

import com.github.tbcd.quota.QuotaLimitResolver;
import com.github.tbcd.quota.QuotaManager;
import com.github.tbcd.quota.ResourceCounter;
import com.github.tbcd.quota.ResourceCounterRegistry;
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
}
