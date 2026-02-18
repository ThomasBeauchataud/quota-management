package com.github.tbcd.quota.impl;

import com.github.tbcd.quota.Tenant;
import com.github.tbcd.quota.TenantResolver;

public class NullTenantResolver implements TenantResolver {

	@Override
	public Tenant resolve() {
		return null;
	}
}
