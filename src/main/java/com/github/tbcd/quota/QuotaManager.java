package com.github.tbcd.quota;

import java.util.Optional;

public interface QuotaManager {

	/**
	 * Vérifie si l'opération est autorisée (sans consommer)
	 *
	 * @param tenant
	 * @param resource
	 * @return
	 */
	default QuotaResult check(Tenant tenant, Object resource) {
		return check(tenant, resource, 1);
	}

	QuotaResult check(Tenant tenant, Object resource, long amount);

	default QuotaResult acquire(Tenant tenant, Object resource) {
		return acquire(tenant, resource, 1);
	}

	QuotaResult acquire(Tenant tenant, Object resource, long amount);

	default QuotaResult release(Tenant tenant, Object resource) {
		return release(tenant, resource, 1);
	}

	QuotaResult release(Tenant tenant, Object resource, long amount);

	Optional<QuotaState> getState(Tenant tenant, Object resource);

}
