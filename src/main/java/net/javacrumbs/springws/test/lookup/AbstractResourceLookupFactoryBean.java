package net.javacrumbs.springws.test.lookup;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.javacrumbs.springws.test.expression.XPathExpressionResolver;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public abstract class AbstractResourceLookupFactoryBean extends AbstractFactoryBean {

	private Map<String, String> namespaceMap;
	private String[] xPathExpressions;
	private String pathPrefix;
	private String pathSuffix;
	private String discriminatorDelimiter;
	private String payloadDelimiter;
	private Map<String, String[]> discriminators;

	public AbstractResourceLookupFactoryBean() {
		super();
	}

	protected ResourceLookup getResourceLookup() {
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		if (xPathExpressions!=null)
		{
			ExpressionBasedResourceLookup resourceLookup = new ExpressionBasedResourceLookup();
			resourceLookup.setExpressionResolver(expressionResolver);
			resourceLookup.setResourceExpressions(xPathExpressions);
			return resourceLookup;
		}
		else
		{
			PayloadRootBasedResourceLookup resourceLookup = new PayloadRootBasedResourceLookup();
			resourceLookup.setExpressionResolver(expressionResolver);
			resourceLookup.setPathPrefix(pathPrefix);
			resourceLookup.setPathSuffix(pathSuffix);
			resourceLookup.setDiscriminatorDelimiter(discriminatorDelimiter);
			resourceLookup.setPayloadDelimiter(payloadDelimiter);
			resourceLookup.setDiscriminators(discriminators);
			return resourceLookup;
		}
	
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

	public String getPathPrefix() {
		return pathPrefix;
	}

	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	public String getPathSuffix() {
		return pathSuffix;
	}

	public void setPathSuffix(String pathSuffix) {
		this.pathSuffix = pathSuffix;
	}

	public String getDiscriminatorDelimiter() {
		return discriminatorDelimiter;
	}

	public void setDiscriminatorDelimiter(String discriminatorDelimiter) {
		this.discriminatorDelimiter = discriminatorDelimiter;
	}

	public String getPayloadDelimiter() {
		return payloadDelimiter;
	}

	public void setPayloadDelimiter(String payloadDelimiter) {
		this.payloadDelimiter = payloadDelimiter;
	}

	public Map<String, String[]> getDiscriminatorsMap() {
		return discriminators;
	}

	public void setDiscriminatorsMap(Map<String, String[]> discriminators) {
		this.discriminators = discriminators;
	}
	public void setDiscriminators(Map<String, String> discriminators) {
		Map<String, String[]> disc = new HashMap<String, String[]>();
		for(Entry<String, String> entry:discriminators.entrySet())
		{
			disc.put(entry.getKey(), entry.getValue().split("\\s*;\\s*"));
		}
		this.discriminators = disc;
	}

}