package com.github.tbcd.quota.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enforces quota limits on a method before execution.
 *
 * <p>Usage:</p>
 * <pre>{@code
 * @Quota(resource = "#recipe")
 * public RecipeDto createRecipe(Recipe recipe) {
 *     return recipeService.create(recipe);
 * }
 * }</pre>
 *
 * <p>With tenant:</p>
 * <pre>{@code
 * @Quota(resource = "#recipe", tenant = "#recipe.getAuthor()")
 * public RecipeDto createRecipe(Recipe recipe) {
 *     return recipeService.create(recipe);
 * }
 * }</pre>
 *
 * @see QuotaAspect
 * @see QuotaExceededException
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Quota {

	/**
	 * SpEL expression to resolve the resource.
	 *
	 * <p>Examples:</p>
	 * <ul>
	 *   <li>{@code "#recipe"} - parameter reference</li>
	 *   <li>{@code "#request.recipe"} - property access</li>
	 *   <li>{@code "#request.getRecipe()"} - method call</li>
	 * </ul>
	 *
	 * @return the SpEL expression
	 */
	String resource();

	/**
	 * SpEL expression to resolve the tenant.
	 *
	 * <p>If empty, the default {@link com.github.tbcd.quota.TenantResolver} is used.</p>
	 *
	 * @return the SpEL expression, or empty to use the default TenantResolver
	 */
	String tenant() default "";

	/**
	 * The quota cost of the operation.
	 *
	 * @return the number of quota units consumed, defaults to 1
	 */
	long cost() default 1;

	/**
	 * Custom error message when quota is exceeded.
	 *
	 * @return the error message
	 */
	String message() default "";
}