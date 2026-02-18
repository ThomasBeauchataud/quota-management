package com.github.tbcd.quota;

/**
 * Registry that provides the appropriate {@link ResourceCounter} for a given resource type.
 *
 * <p>This registry maintains a collection of {@link ResourceCounter} implementations
 * and selects the correct one based on the resource type being checked. It acts as
 * a lookup mechanism to find the counter that supports a specific tenant and resource
 * combination.</p>
 *
 * <p>The default implementation discovers all {@link ResourceCounter} beans in the
 * Spring application context and iterates through them to find a matching counter.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * ResourceCounter counter = registry.getResourceCounter(tenant, Recipe.class);
 * Long currentCount = counter.count(tenant, Recipe.class);
 * }</pre>
 *
 * @see ResourceCounter
 * @see QuotaManager
 */
public interface ResourceCounterRegistry {

	/**
	 * Retrieves the {@link ResourceCounter} capable of counting the specified resource type.
	 *
	 * <p>This method iterates through all registered counters and returns the first one
	 * where {@link ResourceCounter#supports(Tenant, Object)} returns {@code true}.</p>
	 *
	 * @param tenant   the tenant requesting the quota check
	 * @param resource the resource to count, either an instance or a {@link Class}
	 * @return the appropriate {@link ResourceCounter} for the given resource type
	 * @throws IllegalArgumentException if no counter is registered for the given resource type
	 */
	ResourceCounter getResourceCounter(Tenant tenant, Object resource);

}
