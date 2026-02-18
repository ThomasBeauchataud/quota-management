package com.github.tbcd.quota.impl;

import com.github.tbcd.quota.*;

import java.util.Optional;

public class DefaultQuotaManager implements QuotaManager {

	private final ResourceCounterRegistry resourceCounterRegistry;
	private final QuotaLimitResolver limitResolver;

	public DefaultQuotaManager(ResourceCounterRegistry resourceCounterRegistry, QuotaLimitResolver limitResolver) {
		this.resourceCounterRegistry = resourceCounterRegistry;
		this.limitResolver = limitResolver;
	}

	@Override
	public QuotaResult check(Tenant tenant, Object resource, long amount) {
		Optional<QuotaState> stateOpt = getState(tenant, resource);
		if (stateOpt.isEmpty()) {
			return new QuotaResult.NoQuota(resource);
		}

		QuotaState state = stateOpt.get();
		if (state.getUsed() + amount > state.getLimit()) {
			return new QuotaResult.Denied(state);
		}

		return new QuotaResult.Allowed(state);
	}

	@Override
	public Optional<QuotaState> getState(Tenant tenant, Object resource) {
		Optional<Long> limitOpt = limitResolver.resolve(tenant, resource);
		if (limitOpt.isEmpty()) {
			return Optional.empty();
		}

		ResourceCounter counter = resourceCounterRegistry.getResourceCounter(tenant, resource);
		long limit = limitOpt.get();
		long usage = counter.count(tenant, resource);

		return Optional.of(new QuotaState(tenant, resource, usage, limit));
	}
}
