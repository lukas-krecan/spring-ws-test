package net.javacrumbs.springws.test.validator;

import java.util.Map;

import net.javacrumbs.springws.test.expression.XPathExpressionResolver;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Simplifies creation of {@link DefaultResponseGenerator}.
 * @author Lukas Krecan
 *
 */
public class XmlCompareRequestValidatorFactoryBean extends AbstractFactoryBean {

	private Map<String, String> namespaceMap;
	
	private String[] xPathExpressions;

	@Override
	protected Object createInstance() throws Exception {
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		
		XmlCompareRequestValidator responseGenerator = new XmlCompareRequestValidator();
		DefaultResourceLookup reourceLookup = new DefaultResourceLookup();
		reourceLookup.setExpressionResolver(expressionResolver);
		reourceLookup.setResourceExpressions(xPathExpressions);
		responseGenerator.setControlResourceLookup(reourceLookup);
		return responseGenerator;
	}

	@Override
	public Class<?> getObjectType() {
		return XmlCompareRequestValidator.class;
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
