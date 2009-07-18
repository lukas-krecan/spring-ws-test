package net.krecan.springws.test.expression;

import java.net.URI;

import org.w3c.dom.Document;

/**
 * Evaluates an expression. 
 * @author Lukas Krecan
 *
 */
public interface ExpressionResolver {
	/**
	 * Evaluates an expression. 
	 * @param expression
	 * @param uri
	 * @param document
	 * @return
	 */
	public abstract String resolveExpression(String expression, URI uri, Document document);
}
