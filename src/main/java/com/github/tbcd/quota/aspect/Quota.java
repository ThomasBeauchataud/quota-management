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
 * <p>The tenant is resolved automatically using the configured
 * {@link com.github.tbcd.quota.TenantResolver}.</p>
 *
 * <p>Basic usage:</p>
 * <pre>{@code
 * @PostMapping
 * @Quota(resource = Recipe.class)
 * public RecipeDto createRecipe(@RequestBody CreateRecipeRequest request) {
 *     return recipeService.create(request);
 * }
 * }</pre>
 *
 * <p>With custom cost (for bulk operations):</p>
 * <pre>{@code
 * @PostMapping("/bulk")
 * @Quota(resource = Recipe.class, cost = 10)
 * public List<RecipeDto> bulkCreate(@RequestBody List<CreateRecipeRequest> requests) {
 *     return recipeService.createAll(requests);
 * }
 * }</pre>
 *
 * <p>With custom error message:</p>
 * <pre>{@code
 * @PostMapping
 * @Quota(resource = Recipe.class, message = "Upgrade to Pro to create more recipes")
 * public RecipeDto createRecipe(@RequestBody CreateRecipeRequest request) {
 *     return recipeService.create(request);
 * }
 * }</pre>
 *
 * @see QuotaAspect
 * @see QuotaExceededException
 * @see com.github.tbcd.quota.QuotaManager
 * @see com.github.tbcd.quota.TenantResolver
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Quota {

	/**
	 * The resource type to enforce quota on.
	 *
	 * <p>This class is used to look up the appropriate {@link com.github.tbcd.quota.ResourceCounter}
	 * and {@link com.github.tbcd.quota.QuotaLimitResolver} for the quota check.</p>
	 *
	 * <p>Example: {@code Recipe.class}, {@code RecipeBook.class}</p>
	 *
	 * @return the resource class to check quota for
	 */
	Class<?> resource();

	/**
	 * The quota cost of the operation.
	 *
	 * <p>Specifies how many quota units this operation consumes. Defaults to 1
	 * for single-resource operations. Increase for bulk operations or operations
	 * that should consume more quota.</p>
	 *
	 * @return the number of quota units consumed, defaults to 1
	 */
	long cost() default 1;

	/**
	 * Custom error message when quota is exceeded.
	 *
	 * <p>If not specified, a default message will be generated including
	 * the resource type and current usage information.</p>
	 *
	 * <p>Example: {@code "Upgrade to Pro to create more recipes"}</p>
	 *
	 * @return the error message to use, or empty string for default message
	 */
	String message() default "";
}