package com.github.tbcd.quota;

import lombok.Getter;

@Getter
public class QuotaState {

	private final Tenant tenant;
	private final Object resource;
	private final long used;
	private final long limit;

	public QuotaState(Tenant tenant, Object resource, long used, long limit) {
		this.tenant = tenant;
		this.resource = resource;
		this.used = used;
		this.limit = limit;
	}
}