package net.javacrumbs.springws.test.expression;

import java.net.URI;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.xml.namespace.SimpleNamespaceContext;
import org.springframework.xml.xpath.XPathException;
import org.w3c.dom.Document;

/**
 * Evaluates XPath expression.
 * @author Lukas Krecan
 *
 */
public class XPathExpressionResolver implements ExpressionResolver{
	
	private final Log logger = LogFactory.getLog(getClass());

	private NamespaceContext namespaceContext;
	/**
	 * Evaluates an expression.
	 * @param expression
	 * @param uri
	 * @param document
	 * @return
	 */
	public String resolveExpression(String expression, URI uri, Document document) {
		XPathFactory factory = XPathFactory.newInstance();
		factory.setXPathVariableResolver(new WsTestXPathVariableResolver(uri));
		try {
			XPath xpath = factory.newXPath();
			xpath.setNamespaceContext(getNamespaceContext());
			String result = xpath.evaluate(expression, document);
			logger.debug("Expression "+expression+" resolved to "+result);
			return result;
		} catch (XPathExpressionException e) {
			throw new XPathException("Could not evaluate XPath expression:" + e.getMessage(), e);
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
