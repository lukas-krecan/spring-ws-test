package net.krecan.springws.test.lookup;

import java.net.URI;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

/**
 * Does resource lookup based on XPath expression.
 * @author Lukas Krecan
 *
 */
public class XPathResourceLookup extends AbstractResourceLookup {

	/**
	 * Evaluates the expression.
	 * @param expression
	 * @param uri
	 * @param document
	 * @return
	 */
	protected String evaluateExpression(String expression, URI uri, Document document) {
		XPathFactory factory = XPathFactory.newInstance();
		factory.setXPathVariableResolver(new WsTestXPathVariableResolver(uri));
		try {
			XPath xpath = factory.newXPath();
			xpath.setNamespaceContext(getNamespaceContext());
			return xpath.evaluate(expression, document);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Unexpected exception",e);
		}
	}

}
