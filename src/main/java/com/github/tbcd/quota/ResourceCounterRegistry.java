package com.github.tbcd.quota;

public interface ResourceCounterRegistry {

	ResourceCounter getResourceCounter(Tenant tenant, Object resource);

}
