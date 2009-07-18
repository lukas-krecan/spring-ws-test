package net.krecan.springws.test.expression;

import java.net.URI;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.xml.namespace.SimpleNamespaceContext;
import org.w3c.dom.Document;

/**
 * Evaluates XPath expression.
 * @author Lukas Krecan
 *
 */
public class XPathExpressionEvaluator implements ExpressionEvaluator{

	private NamespaceContext namespaceContext;
	/**
	 * Evaluates an expression.
	 * @param expression
	 * @param uri
	 * @param document
	 * @return
	 */
	public String evaluateExpression(String expression, URI uri, Document document) {
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
	public void setNamespaceMap(Map<String, String> namespaceMap) {
		SimpleNamespaceContext context = new SimpleNamespaceContext();
		context.setBindings(namespaceMap);
		setNamespaceContext(context);
	}

	public NamespaceContext getNamespaceContext() {
		return namespaceContext;
	}

	public void setNamespaceContext(NamespaceContext namespaceContext) {
		this.namespaceContext = namespaceContext;
	}

}
