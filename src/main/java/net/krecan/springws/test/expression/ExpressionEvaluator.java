package net.krecan.springws.test.expression;

import java.net.URI;

import org.w3c.dom.Document;

/**
 * Evaluates an expression. 
 * @author Lukas Krecan
 *
 */
public interface ExpressionEvaluator {
	/**
	 * Evaluates an expression. 
	 * @param expression
	 * @param uri
	 * @param document
	 * @return
	 */
	public abstract String evaluateExpression(String expression, URI uri, Document document);
}
