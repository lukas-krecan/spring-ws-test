package net.javacrumbs.springws.test.expression;

/**
 * Exception thrown if expression resolving fails.
 * @author Lukas Krecan
 *
 */
public class ExpressionResolverException extends RuntimeException {

	private static final long serialVersionUID = 3081963379089820552L;

	public ExpressionResolverException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExpressionResolverException(String message) {
		super(message);
	}

}
