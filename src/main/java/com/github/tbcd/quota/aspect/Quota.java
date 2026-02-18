package com.github.tbcd.quota.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enforces quota limits on a method before execution.
 *
 * <p>When applied to a method, the {@link QuotaAspect} intercepts the call
 * and verifies that the current tenant has sufficient quota for the specified
 * resource. If the quota would be exceeded, a {@link QuotaExceededException}
 * is thrown and the method is not executed.</p>
 *
 * <p>Basic usage:</p>
 * <pre>{@code
 * @Quota(resource = Recipe.class)
 * public RecipeDto createRecipe(@RequestBody CreateRecipeRequest request) {
 *     return recipeService.create(request);
 * }
 * }</pre>
 *
 * <p>With SpEL expression for tenant:</p>
 * <pre>{@code
 * @Quota(resource = Recipe.class, tenant = "#recipe.getAuthor()")
 * public RecipeDto createRecipe(Recipe recipe) {
 *     return recipeService.create(recipe);
 * }
 * }</pre>
 *
 * <p>With parameter reference:</p>
 * <pre>{@code
 * @Quota(resource = Recipe.class, tenant = "#userId")
 * public RecipeDto createRecipe(Long userId, CreateRecipeRequest request) {
 *     return recipeService.create(userId, request);
 * }
 * }</pre>
 *
 * @see QuotaAspect
 * @see QuotaExceededException
 * @see com.github.tbcd.quota.QuotaManager
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Quota {

	/**
	 * The resource type to enforce quota on.
	 *
	 * @return the resource class to check quota for
	 */
	Class<?> resource();

	/**
	 * SpEL expression to resolve the tenant.
	 *
	 * <p>If empty, the default {@link com.github.tbcd.quota.TenantResolver} is used.</p>
	 *
	 * <p>The expression has access to method parameters by name using {@code #paramName}.</p>
	 *
	 * <p>Examples:</p>
	 * <ul>
	 *   <li>{@code "#userId"} - direct parameter</li>
	 *   <li>{@code "#recipe.getAuthor()"} - method call on parameter</li>
	 *   <li>{@code "#request.userId"} - property access</li>
	 *   <li>{@code "#user.getId()"} - method call returning the tenant id</li>
	 * </ul>
	 *
	 * <p>The expression must resolve to one of:</p>
	 * <ul>
	 *   <li>A {@link com.github.tbcd.quota.Tenant} instance</li>
	 *   <li>A {@link String}, {@link Long}, or any object (will be wrapped in a Tenant)</li>
	 * </ul>
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
	 * @return the error message to use, or empty string for default message
	 */
	String message() default "";
}