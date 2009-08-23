package net.krecan.springws.test.generator;

import java.util.Map;

import net.krecan.springws.test.expression.XPathExpressionResolver;
import net.krecan.springws.test.lookup.DefaultResourceLookup;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Simplifies creation of {@link DefaultResponseGenerator}.
 * @author Lukas Krecan
 *
 */
public class DefaultResponseGeneratorFactroyBean extends AbstractFactoryBean {

	private Map<String, String> namespaceMap;
	
	private String[] xPathExpressions;

	@Override
	protected Object createInstance() throws Exception {
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		
		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		DefaultResourceLookup responseResourceLookup = new DefaultResourceLookup();
		responseResourceLookup.setExpressionResolver(expressionResolver);
		responseResourceLookup.setResourceExpressions(xPathExpressions);
		responseGenerator.setResourceLookup(responseResourceLookup);
		return responseGenerator;
	}

	@Override
	public Class<?> getObjectType() {
		return DefaultResponseGenerator.class;
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
