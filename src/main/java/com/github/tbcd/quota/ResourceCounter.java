package com.github.tbcd.quota;

public interface ResourceCounter {

	Long count(Tenant tenant, Object resource);

	default void increment(Tenant tenant, Object resource, long count) {
	}

	default void decrement(Tenant tenant, Object resource, long count) {
	}

	boolean supports(Tenant tenant, Object resource);

}
