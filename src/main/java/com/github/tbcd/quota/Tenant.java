package com.github.tbcd.quota;

/**
 * Represents an entity that owns resources subject to quota enforcement.
 *
 * <p>A tenant is the unit of ownership for quota management. It typically
 * represents a user, organization, or any other entity that can own resources
 * and have limits applied to them.</p>
 *
 * <p>Implementations should provide a unique identifier that can be used
 * to distinguish one tenant from another when counting resources and
 * resolving quota limits.</p>
 *
 * <p>Example implementation:</p>
 * <pre>{@code
 * public record UserTenant(Long userId) implements Tenant {
 *
 *     @Override
 *     public Object getValue() {
 *         return userId;
 *     }
 *
 *     public static UserTenant of(User user) {
 *         return new UserTenant(user.getId());
 *     }
 * }
 * }</pre>
 *
 * @see TenantResolver
 * @see QuotaManager
 */
public interface Tenant {

	/**
	 * Returns the unique identifier of this tenant.
	 *
	 * <p>This value is used to identify the tenant when counting resources
	 * and looking up quota limits. The returned object should have proper
	 * {@code equals()} and {@code hashCode()} implementations.</p>
	 *
	 * <p>Common return types include:</p>
	 * <ul>
	 *   <li>{@link Long} - for database primary keys</li>
	 *   <li>{@link String} - for UUIDs or usernames</li>
	 *   <li>{@link java.util.UUID} - for distributed systems</li>
	 * </ul>
	 *
	 * @return the unique identifier of this tenant, never null
	 */
	Object getValue();

}
