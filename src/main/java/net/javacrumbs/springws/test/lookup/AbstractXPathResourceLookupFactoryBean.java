package net.javacrumbs.springws.test.lookup;

import java.util.Map;

import net.javacrumbs.springws.test.expression.XPathExpressionResolver;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public abstract class AbstractXPathResourceLookupFactoryBean extends AbstractFactoryBean {

	private Map<String, String> namespaceMap;
	private String[] xPathExpressions;
	
	public AbstractXPathResourceLookupFactoryBean() {
		super();
	}

	protected ResourceLookup getResourceLookup() {
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);

		ExpressionBasedResourceLookup resourceLookup = new ExpressionBasedResourceLookup();
		resourceLookup.setExpressionResolver(expressionResolver);
		resourceLookup.setResourceExpressions(xPathExpressions);
		return resourceLookup;
	}

	public String[] getXPathExpressions() {
		return xPathExpressions;
	}

	public void setXPathExpressions(String[] responseXPathExpressions) {
		this.xPathExpressions = responseXPathExpressions;
	}

	public Map<String, String> getNamespaceMap() {
		return namespaceMap;
	}

	public void setNamespaceMap(Map<String, String> namespaceMap) {
		this.namespaceMap = namespaceMap;
	}
}