package com.github.tbcd.quota.impl;

import com.github.tbcd.quota.ResourceCounter;
import com.github.tbcd.quota.ResourceCounterRegistry;
import com.github.tbcd.quota.Tenant;
import org.springframework.context.annotation.Lazy;

import java.util.List;

public class DefaultResourceCounterRegistry implements ResourceCounterRegistry {

	private final List<ResourceCounter> resourceCounters;

	public DefaultResourceCounterRegistry(@Lazy List<ResourceCounter> resourceCounters) {
		this.resourceCounters = resourceCounters;
	}

	@Override
	public ResourceCounter getResourceCounter(Tenant tenant, Object resource) {
		return resourceCounters.stream()
				.filter(rc -> rc.supports(tenant, resource))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No resource counter registered for: " + resource.getClass().getSimpleName()));
	}
}
