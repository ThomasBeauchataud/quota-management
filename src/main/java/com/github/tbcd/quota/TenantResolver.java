package com.github.tbcd.quota;

/**
 * Resolves the current tenant from the execution context.
 *
 * <p>Implementations of this interface extract the tenant information from
 * the current context, typically from the security context, HTTP request,
 * or thread-local storage. This allows the quota system to automatically
 * identify the tenant without requiring explicit parameters.</p>
 *
 * <p>This is a functional interface used primarily by the {@link com.github.tbcd.quota.aspect.QuotaAspect}
 * to determine which tenant is performing an operation when the
 * {@link com.github.tbcd.quota.aspect.Quota} annotation is used.</p>
 *
 * <p>Example implementation using Spring Security:</p>
 * <pre>{@code
 * @Component
 * public class SecurityTenantResolver implements TenantResolver {
 *
 *     @Override
 *     public Tenant resolve() {
 *         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 *         if (auth == null || !auth.isAuthenticated()) {
 *             throw new IllegalStateException("No authenticated user");
 *         }
 *         String userId = auth.getName();
 *         return () -> userId;
 *     }
 * }
 * }</pre>
 *
 * <p>Example implementation with a custom user service:</p>
 * <pre>{@code
 * @Component
 * public class UserTenantResolver implements TenantResolver {
 *
 *     private final AuthenticationContextHelper authHelper;
 *
 *     @Override
 *     public Tenant resolve() {
 *         User user = authHelper.getUser();
 *         return () -> user.getId();
 *     }
 * }
 * }</pre>
 *
 * @see Tenant
 * @see QuotaManager
 * @see com.github.tbcd.quota.aspect.QuotaAspect
 */
@FunctionalInterface
public interface TenantResolver {

	/**
	 * Resolves the current tenant from the execution context.
	 *
	 * <p>This method is called during quota checks to identify the tenant
	 * performing the operation. Implementations should extract the tenant
	 * information from the appropriate context (security, request, etc.).</p>
	 *
	 * @return the current tenant, never null
	 * @throws IllegalStateException if no tenant can be resolved from the current context
	 */
	Tenant resolve();
}