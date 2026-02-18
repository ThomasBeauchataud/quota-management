package com.github.tbcd.quota;

/**
 * Counts resources owned by a tenant for quota enforcement.
 *
 * <p>Implementations of this interface are responsible for counting
 * the current number of resources of a specific type that belong to a tenant.
 * This count is then compared against the tenant's quota limit to determine
 * if they can create additional resources.</p>
 *
 * <p>Example implementation:</p>
 * <pre>{@code
 * @Component
 * public class RecipeResourceCounter implements ResourceCounter {
 *
 *     private final RecipeRepository recipeRepository;
 *
 *     @Override
 *     public boolean supports(Tenant tenant, Object resource) {
 *         return resource instanceof Recipe || resource.equals(Recipe.class);
 *     }
 *
 *     @Override
 *     public Long count(Tenant tenant, Object resource) {
 *         return recipeRepository.countByOwnerId(tenant.id());
 *     }
 * }
 * }</pre>
 *
 * @see QuotaManager
 * @see ResourceCounterRegistry
 */
public interface ResourceCounter {

	/**
	 * Counts the number of resources currently owned by the given tenant.
	 *
	 * <p>This method is called during quota checks to determine the current
	 * usage level. The returned count is compared against the configured
	 * limit to decide whether the tenant can create more resources.</p>
	 *
	 * @param tenant   the tenant whose resources should be counted
	 * @param resource the resource type to count, either an instance or a {@link Class}
	 * @return the current number of resources owned by the tenant, never null
	 */
	Long count(Tenant tenant, Object resource);


	/**
	 * Determines whether this counter supports the given tenant and resource combination.
	 *
	 * <p>The {@link ResourceCounterRegistry} uses this method to find the appropriate
	 * counter for a given resource type. Only one counter should return {@code true}
	 * for any given resource type.</p>
	 *
	 * <p>Typical implementations check the resource type:</p>
	 * <pre>{@code
	 * @Override
	 * public boolean supports(Tenant tenant, Object resource) {
	 *     return resource instanceof Recipe || resource.equals(Recipe.class);
	 * }
	 * }</pre>
	 *
	 * @param tenant   the tenant requesting the quota check
	 * @param resource the resource to check, either an instance or a {@link Class}
	 * @return {@code true} if this counter can handle the given resource type,
	 *         {@code false} otherwise
	 */
	boolean supports(Tenant tenant, Object resource);

}
