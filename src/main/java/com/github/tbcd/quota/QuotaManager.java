package com.github.tbcd.quota;

import java.util.Optional;

/**
 * Central service for managing and enforcing resource quotas.
 *
 * <p>The {@code QuotaManager} provides the main API for checking quota limits
 * before creating resources. It coordinates between the {@link ResourceCounterRegistry}
 * to count current resource usage and the {@link QuotaLimitResolver} to determine
 * the allowed limits for each tenant.</p>
 *
 * <p>Use {@link #check(Tenant, Object)} before creating a resource to verify
 * that the tenant has not exceeded their quota. The quota is based on the
 * actual count of resources in the database.</p>
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