package net.javacrumbs.springws.test.common;

import java.net.URI;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.javacrumbs.springws.test.expression.ExpressionResolverException;
import net.javacrumbs.springws.test.expression.WsTestXPathVariableResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

public class XPathExpressionEvaluator implements ExpressionEvaluator {
	
	private final Log logger = LogFactory.getLog(getClass());

	public String evaluateExpression(Document document, String expression, URI uri, NamespaceContext namespaceContext) {
		XPathFactory factory = XPathFactory.newInstance();
		factory.setXPathVariableResolver(new WsTestXPathVariableResolver(uri));
		try {
			XPath xpath = factory.newXPath();
			if (namespaceContext!=null)
			{
				xpath.setNamespaceContext(namespaceContext);
			}
			String result = xpath.evaluate(expression, document);
			logger.debug("Expression \"" + expression + "\" resolved to \"" + result+"\"");
			return result;
		} catch (XPathExpressionException e) {
			throw new ExpressionResolverException("Could not evaluate XPath expression \"" + expression + "\" : \"" + e.getMessage()+"\"", e);
		}
	}

}
