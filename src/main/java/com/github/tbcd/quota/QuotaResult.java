package com.github.tbcd.quota;

/**
 * Represents the result of a quota check or acquisition operation.
 *
 * <p>This sealed interface has three possible implementations:</p>
 * <ul>
 *   <li>{@link Allowed} - the operation is permitted within quota limits</li>
 *   <li>{@link Denied} - the operation would exceed quota limits</li>
 *   <li>{@link NoQuota} - no quota is configured for the resource (permitted by default)</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * QuotaResult result = quotaManager.check(tenant, Recipe.class);
 *
 * if (!result.allowed()) {
 *     QuotaState state = result.state();
 *     throw new QuotaExceededException(
 *         "Limit reached: " + state.getUsed() + "/" + state.getLimit()
 *     );
 * }
 * }</pre>
 *
 * <p>Pattern matching can be used to handle each case:</p>
 * <pre>{@code
 * switch (result) {
 *     case QuotaResult.Allowed a -> log.info("Quota OK: {}", a.state());
 *     case QuotaResult.Denied d -> throw new QuotaExceededException(d.state());
 *     case QuotaResult.NoQuota n -> log.warn("No quota configured for {}", n.resource());
 * }
 * }</pre>
 *
 * @see QuotaManager
 * @see QuotaState
 */
public interface QuotaResult {

	/**
	 * Returns whether the operation is allowed.
	 *
	 * @return {@code true} if the operation is permitted, {@code false} if denied
	 */
	boolean allowed();

	/**
	 * Returns the quota state at the time of the check.
	 *
	 * <p>This provides details about current usage and limits. May be {@code null}
	 * for {@link NoQuota} results where no quota is configured.</p>
	 *
	 * @return the quota state, or {@code null} if no quota is configured
	 */
	QuotaState state();

	/**
	 * Indicates that the operation is allowed within quota limits.
	 *
	 * <p>This result is returned when the tenant's current usage plus the
	 * requested amount does not exceed their configured limit.</p>
	 *
	 * @param state the current quota state showing usage and limits
	 */
	record Allowed(QuotaState state) implements QuotaResult {

		@Override
		public boolean allowed() {
			return true;
		}
	}

	/**
	 * Indicates that the operation is denied due to quota limits.
	 *
	 * <p>This result is returned when the tenant's current usage plus the
	 * requested amount would exceed their configured limit.</p>
	 *
	 * <p>The {@link #state()} method provides details about the current usage
	 * and limit, which can be used to construct an informative error message.</p>
	 *
	 * @param state the current quota state showing why the request was denied
	 */
	record Denied(QuotaState state) implements QuotaResult {

		@Override
		public boolean allowed() {
			return false;
		}
	}

	/**
	 * Indicates that no quota is configured for the requested resource.
	 *
	 * <p>By default, operations are allowed when no quota is configured.
	 * The {@link #state()} method returns {@code null} since there is no
	 * quota information available.</p>
	 *
	 * @param resource the resource for which no quota was found
	 */
	record NoQuota(Object resource) implements QuotaResult {

		@Override
		public boolean allowed() {
			return true;
		}

		@Override
		public QuotaState state() {
			return null;
		}
	}
}