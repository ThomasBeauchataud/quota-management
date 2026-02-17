package com.github.tbcd.quota.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Quota {

	/**
	 * The resource type to limit
	 */
	Class<?> resource();

	/**
	 * The error message
	 */
	String message() default "";

}
