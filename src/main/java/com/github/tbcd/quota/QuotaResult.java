package com.github.tbcd.quota;

public interface QuotaResult {

	boolean allowed();
	QuotaState state();

	record Allowed(QuotaState state) implements QuotaResult {
		@Override public boolean allowed() { return true; }
	}

	record Denied(QuotaState state) implements QuotaResult {
		@Override public boolean allowed() { return false; }
	}

	record NoQuota(Object resource) implements QuotaResult {
		@Override public boolean allowed() { return true; }
		@Override public QuotaState state() { return null; }
	}
}
