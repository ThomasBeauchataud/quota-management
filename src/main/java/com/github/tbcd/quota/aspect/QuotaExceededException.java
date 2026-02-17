package com.github.tbcd.quota.aspect;

import lombok.Getter;

@Getter
public class QuotaExceededException extends RuntimeException {

	private final String resource;
	private final long currentCount;
	private final long maxAllowed;

	public QuotaExceededException(String message, String resource, long currentCount, long maxAllowed) {
		super(message);
		this.resource = resource;
		this.currentCount = currentCount;
		this.maxAllowed = maxAllowed;
	}
}
