package com.github.tbcd.quota.impl;

import com.github.tbcd.quota.QuotaLimitResolver;
import com.github.tbcd.quota.Tenant;

import java.util.Optional;

public class EmptyQuotaLimitResolver implements QuotaLimitResolver {

	@Override
	public Optional<Long> resolve(Tenant tenant, Object resource) {
		return Optional.empty();
	}
}
