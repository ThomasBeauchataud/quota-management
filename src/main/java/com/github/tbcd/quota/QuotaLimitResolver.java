package com.github.tbcd.quota;

import java.util.Optional;

/**
 * Resolves the quota limit for a given tenant and resource combination.
 *
 * <p>Implementations of this interface determine the maximum number of resources
 * a tenant is allowed to own. The limit can be based on various factors such as
 * subscription tier, organization settings, or custom business rules.</p>
 *
 * <p>This is a functional interface, allowing for concise lambda implementations:</p>
 * <pre>{@code
 * QuotaLimitResolver resolver = (tenant, resource) -> {
 *     if (resource.equals(Recipe.class)) {
 *         return Optional.of(100L);
 *     }
 *     return Optional.empty();
 * };
 * }</pre>
 *
 * <p>Typical implementation with subscription tiers:</p>
 * <pre>{@code
 * @Component
 * public class TieredQuotaLimitResolver implements QuotaLimitResolver {
 *
 *     private final SubscriptionService subscriptionService;
 *
 *     private static final Map<String, Map<String, Long>> LIMITS = Map.of(
 *         "recipe", Map.of("free", 10L, "pro", 500L, "enterprise", Long.MAX_VALUE),
 *         "recipebook", Map.of("free", 3L, "pro", 50L, "enterprise", Long.MAX_VALUE)
 *     );
 *
 *     @Override
 *     public Optional<Long> resolve(Tenant tenant, Object resource) {
 *         String tier = subscriptionService.getTier(tenant.getValue());
 *         String resourceName = resource.getClass().getSimpleName().toLowerCase();
 *         return Optional.ofNullable(LIMITS.get(resourceName))
 *                 .map(tierLimits -> tierLimits.get(tier));
 *     }
 * }
 * }</pre>
 *
 * @see QuotaManager
 * @see Tenant
 */
@FunctionalInterface
public interface QuotaLimitResolver {

	/**
	 * Resolves the quota limit for the given tenant and resource.
	 *
	 * <p>Returns the maximum number of resources the tenant is allowed to own.
	 * Special return values:</p>
	 * <ul>
	 *   <li>{@code Optional.empty()} - no quota is configured, operation is allowed</li>
	 *   <li>{@code Optional.of(0L)} - tenant cannot create any resources</li>
	 *   <li>{@code Optional.of(Long.MAX_VALUE)} - unlimited quota</li>
	 * </ul>
	 *
	 * @param tenant   the tenant to resolve the limit for
	 * @param resource the resource type, either an instance or a {@link Class}
	 * @return the quota limit, or empty if no quota is configured for this combination
	 */
	Optional<Long> resolve(Tenant tenant, Object resource);
}