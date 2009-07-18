package net.krecan.springws.test.util;

import java.util.Map;

import net.krecan.springws.test.MockWebServiceMessageSender;
import net.krecan.springws.test.expression.XPathExpressionEvaluator;
import net.krecan.springws.test.generator.DefaultResponseGenerator;
import net.krecan.springws.test.lookup.DefaultResourceLookup;
import net.krecan.springws.test.validator.SchemaRequestValidator;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Simplifies creation of {@link MockWebServiceMessageSender}.
 * @author Lukas Krecan
 *
 */
public class MockWebServiceMessageSenderFactory implements FactoryBean, InitializingBean {

	private MockWebServiceMessageSender sender;
	
	private String[] responseXPathExpressions;
	
	private Map<String,String> namespaceMap;
	
	private Resource[] requestValidationSchemas;
	
	public Object getObject() throws Exception {
		return sender;
	}

	public Class<?> getObjectType() {
		return WebServiceMessageSender.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		sender = new MockWebServiceMessageSender();
		Assert.notNull(namespaceMap, "namespaceMap has to be specified.");
		XPathExpressionEvaluator expressionEvaluator = new XPathExpressionEvaluator();
		expressionEvaluator.setNamespaceMap(namespaceMap);

		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		DefaultResourceLookup responseResourceLookup = new DefaultResourceLookup();
		responseResourceLookup.setExpressionEvaluator(expressionEvaluator);
		responseResourceLookup.setResourceExpressions(responseXPathExpressions);
		responseGenerator.setResourceLookup(responseResourceLookup);
		sender.setResponseGenerator(responseGenerator);
		
		if (requestValidationSchemas!=null)
		{
			SchemaRequestValidator requestValidator = new SchemaRequestValidator();
			requestValidator.setSchemas(requestValidationSchemas);
			requestValidator.afterPropertiesSet();
			sender.setRequestValidator(requestValidator);
		}
	}

	public String[] getResponseXPathExpressions() {
		return responseXPathExpressions;
	}

	public void setResponseXPathExpressions(String[] responseXPathExpressions) {
		this.responseXPathExpressions = responseXPathExpressions;
	}

	public Map<String, String> getNamespaceMap() {
		return namespaceMap;
	}

	public void setNamespaceMap(Map<String, String> namespaceMap) {
		this.namespaceMap = namespaceMap;
	}

	public Resource[] getRequestValidationSchemas() {
		return requestValidationSchemas;
	}

	public void setRequestValidationSchemas(Resource[] requestValidationSchemas) {
		this.requestValidationSchemas = requestValidationSchemas;
	}



}
