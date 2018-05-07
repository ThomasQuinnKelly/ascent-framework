package gov.va.ascent.framework.util;

import java.util.Collection;

import org.springframework.util.Assert;

/**
 * Utility class to make runtime assertions against parameters. Currently delegates many of its exposed calls to
 * Spring's internal Assert class, however using this custom Defense interface is prefered as opposed to directly
 * using Springs internal class to avoid unnecessarily coupling clients to the Spring API. The Spring dependency could
 * be removed in future releases without affecting clients.
 *
 * @author Jon Shrader, Jimmy Ray
 */
public final class Defense {

	/**
	 * Instantiates a new defense.
	 */
	private Defense() {
	}

	/**
	 * Checks if is instance of.
	 *
	 * @param clazz the clazz
	 * @param obj the obj
	 */
	public static void isInstanceOf(final Class<?> clazz, final Object obj) {
		Assert.isInstanceOf(clazz, obj);
	}

	/**
	 * Assert the expression.
	 *
	 * @param expression the expression
	 * @param message the message
	 */
	public static void state(final boolean expression, final String message) {
		Assert.state(expression, message);
	}

	/**
	 * Assert the expression.
	 *
	 * @param expression the expression
	 */
	public static void state(final boolean expression) {
		Assert.state(expression, "[Assertion failed] - this state invariant must be true");
	}

	/**
	 * Checks if is null.
	 *
	 * @param ref the ref
	 */
	public static void isNull(final Object ref) {
		Assert.isNull(ref,  "[Assertion failed] - the object argument must be null");
	}

	/**
	 * Checks if is null.
	 *
	 * @param ref the ref
	 * @param message the message
	 */
	public static void isNull(final Object ref, final String message) {
		Assert.isNull(ref, message);
	}

	/**
	 * Not null.
	 *
	 * @param ref the ref
	 */
	public static void notNull(final Object ref) {
		Assert.notNull(ref, "[Assertion failed] - this argument is required; it must not be null");
	}

	/**
	 * Not null.
	 *
	 * @param ref the ref
	 * @param message the message
	 */
	public static void notNull(final Object ref, final String message) {
		Assert.notNull(ref, message);
	}

	/**
	 * Checks for text.
	 *
	 * @param text the text
	 */
	public static void hasText(final String text) {
		Assert.hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	/**
	 * Checks for text.
	 *
	 * @param text the text
	 * @param message the message
	 */
	public static void hasText(final String text, final String message) {
		Assert.hasText(text, message);
	}

	/**
	 * Not empty.
	 *
	 * @param ref the ref
	 */
	public static void notEmpty(final Collection<?> ref) {
		Assert.notEmpty(ref, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	/**
	 * Not empty.
	 *
	 * @param ref the ref
	 * @param message the message
	 */
	public static void notEmpty(final Collection<?> ref, final String message) {
		Assert.notEmpty(ref, message);
	}

	/**
	 * Not empty.
	 *
	 * @param ref the ref
	 * @param message the message
	 */
	public static void notEmpty(final String[] ref, final String message) {
		Assert.notEmpty(ref, message);
	}

	/**
	 * Checks if is true.
	 *
	 * @param expression the expression
	 */
	public static void isTrue(final Boolean expression) {
		Assert.notNull(expression, "[Assertion failed] - this argument is required; it must not be null");
		Assert.isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	/**
	 * Checks if is true.
	 *
	 * @param expression the expression
	 * @param message the message
	 */
	public static void isTrue(final Boolean expression, final String message) {
		Assert.notNull(expression, message);
		Assert.isTrue(expression, message);
	}

}
