package com.github.tbcd.quota;

import java.util.Optional;

@FunctionalInterface
public interface QuotaLimitResolver {

	/**
	 * Return the quota limit for the given tenant of the given resource
	 *
	 * @param tenant The tenant
	 * @param resource The resource
	 * @return The quota limit of empty if there is no quota
	 */
	Optional<Long> resolve(Tenant tenant, Object resource);

}
