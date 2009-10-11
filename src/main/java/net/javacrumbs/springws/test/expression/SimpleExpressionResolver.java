package net.javacrumbs.springws.test.expression;

import java.net.URI;

import org.w3c.dom.Document;

/**
 * Resolver that always returns the same result.
 * @author Lukas Krecan
 *
 */
public class SimpleExpressionResolver implements ExpressionResolver {

	private final String result;
	
	public SimpleExpressionResolver(String result) {
		this.result = result;
	}

	public String resolveExpression(String expression, URI uri, Document document) {
		return result;
	}

}
