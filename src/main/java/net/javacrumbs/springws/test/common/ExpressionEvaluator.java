package net.javacrumbs.springws.test.common;

import java.net.URI;

import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

/**
 * Evaluates an expression.
 * @author Lukas Krecan
 *
 */
public interface ExpressionEvaluator {

	public String evaluateExpression(Document source, String expression, URI uri, NamespaceContext namespaceContext) ;
}
