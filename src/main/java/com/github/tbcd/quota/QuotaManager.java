package com.github.tbcd.quota;

import java.util.Optional;

/**
 * Central service for managing and enforcing resource quotas.
 *
 * <p>The {@code QuotaManager} provides the main API for checking, acquiring,
 * and releasing quota units. It coordinates between the {@link ResourceCounterRegistry}
 * to count current resource usage and the {@link QuotaLimitResolver} to determine
 * the allowed limits for each tenant.</p>
 *
 * <p>There are two main usage patterns:</p>
 * <ul>
 *   <li><b>Count-based quotas:</b> Use {@link #check(Tenant, Object)} before creating
 *       a resource. The quota is based on the actual count in the database.</li>
 *   <li><b>Counter-based quotas:</b> Use {@link #acquire(Tenant, Object)} and
 *       {@link #release(Tenant, Object)} to manually track quota consumption.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * QuotaResult result = quotaManager.check(tenant, Recipe.class);
 * if (!result.allowed()) {
 *     throw new QuotaExceededException("Recipe limit reached");
 * }
 * recipeRepository.save(recipe);
 * }</pre>
 *
 * @see QuotaResult
 * @see QuotaState
 * @see ResourceCounter
 * @see QuotaLimitResolver
 */
public interface QuotaManager {

	/**
	 * Checks if the tenant can consume one unit of the specified resource.
	 *
	 * <p>This is a convenience method equivalent to calling
	 * {@link #check(Tenant, Object, long)} with an amount of 1.</p>
	 *
	 * @param tenant   the tenant to check the quota for
	 * @param resource the resource type to check, either an instance or a {@link Class}
	 * @return the result of the quota check
	 * @see #check(Tenant, Object, long)
	 */
	default QuotaResult check(Tenant tenant, Object resource) {
		return check(tenant, resource, 1);
	}

	/**
	 * Checks if the tenant can consume the specified amount of a resource.
	 *
	 * <p>This method verifies whether the tenant's current usage plus the requested
	 * amount would exceed their quota limit. It does not modify any counters.</p>
	 *
	 * <p>Possible return values:</p>
	 * <ul>
	 *   <li>{@link QuotaResult.Allowed} - the operation is permitted</li>
	 *   <li>{@link QuotaResult.Denied} - the quota would be exceeded</li>
	 *   <li>{@link QuotaResult.NoQuota} - no quota is configured for this resource</li>
	 * </ul>
	 *
	 * @param tenant   the tenant to check the quota for
	 * @param resource the resource type to check, either an instance or a {@link Class}
	 * @param amount   the number of units to check
	 * @return the result of the quota check
	 */
	QuotaResult check(Tenant tenant, Object resource, long amount);

	/**
	 * Acquires one unit of quota for the specified resource.
	 *
	 * <p>This is a convenience method equivalent to calling
	 * {@link #acquire(Tenant, Object, long)} with an amount of 1.</p>
	 *
	 * @param tenant   the tenant acquiring the quota
	 * @param resource the resource type to acquire, either an instance or a {@link Class}
	 * @return the result of the acquisition attempt
	 * @see #acquire(Tenant, Object, long)
	 */
	default QuotaResult acquire(Tenant tenant, Object resource) {
		return acquire(tenant, resource, 1);
	}

	/**
	 * Acquires the specified amount of quota for a resource.
	 *
	 * <p>This method first checks if the quota allows the acquisition, then
	 * increments the internal counter if permitted. Use this for counter-based
	 * quotas where you manually track consumption.</p>
	 *
	 * <p>If the acquisition is denied, no counter is modified.</p>
	 *
	 * @param tenant   the tenant acquiring the quota
	 * @param resource the resource type to acquire, either an instance or a {@link Class}
	 * @param amount   the number of units to acquire
	 * @return the result of the acquisition attempt
	 * @see #release(Tenant, Object, long)
	 */
	QuotaResult acquire(Tenant tenant, Object resource, long amount);

	/**
	 * Releases one unit of quota for the specified resource.
	 *
	 * <p>This is a convenience method equivalent to calling
	 * {@link #release(Tenant, Object, long)} with an amount of 1.</p>
	 *
	 * @param tenant   the tenant releasing the quota
	 * @param resource the resource type to release, either an instance or a {@link Class}
	 * @return the result after releasing the quota
	 * @see #release(Tenant, Object, long)
	 */
	default QuotaResult release(Tenant tenant, Object resource) {
		return release(tenant, resource, 1);
	}

	/**
	 * Releases the specified amount of quota for a resource.
	 *
	 * <p>This method decrements the internal counter for counter-based quotas.
	 * Use this when a resource is deleted to free up quota for future use.</p>
	 *
	 * <p>The counter will not go below zero.</p>
	 *
	 * @param tenant   the tenant releasing the quota
	 * @param resource the resource type to release, either an instance or a {@link Class}
	 * @param amount   the number of units to release
	 * @return the result after releasing the quota
	 * @see #acquire(Tenant, Object, long)
	 */
	QuotaResult release(Tenant tenant, Object resource, long amount);

	/**
	 * Retrieves the current quota state for a tenant and resource.
	 *
	 * <p>This method returns detailed information about the tenant's current
	 * usage and limit for the specified resource type.</p>
	 *
	 * @param tenant   the tenant to get the state for
	 * @param resource the resource type to query, either an instance or a {@link Class}
	 * @return the current quota state, or empty if no quota is configured
	 */
	Optional<QuotaState> getState(Tenant tenant, Object resource);
}