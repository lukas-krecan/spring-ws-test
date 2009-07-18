package net.krecan.springws.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.krecan.springws.test.MockWebServiceMessageSender;
import net.krecan.springws.test.expression.XPathExpressionResolver;
import net.krecan.springws.test.generator.DefaultResponseGenerator;
import net.krecan.springws.test.lookup.DefaultResourceLookup;
import net.krecan.springws.test.validator.RequestValidator;
import net.krecan.springws.test.validator.SchemaRequestValidator;
import net.krecan.springws.test.validator.XmlCompareRequestValidator;

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

	private String[] controlRequestXPathExpressions;
	
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
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);

		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		DefaultResourceLookup responseResourceLookup = new DefaultResourceLookup();
		responseResourceLookup.setExpressionResolver(expressionResolver);
		responseResourceLookup.setResourceExpressions(responseXPathExpressions);
		responseGenerator.setResourceLookup(responseResourceLookup);
		sender.setResponseGenerator(responseGenerator);
		
		List<RequestValidator> validators = new ArrayList<RequestValidator>(2);
		if (requestValidationSchemas!=null)
		{
			SchemaRequestValidator requestValidator = new SchemaRequestValidator();
			requestValidator.setSchemas(requestValidationSchemas);
			requestValidator.afterPropertiesSet();
			validators.add(requestValidator);
		}
		
		if (controlRequestXPathExpressions!=null)
		{
			XmlCompareRequestValidator requestValidator = new XmlCompareRequestValidator();
			DefaultResourceLookup controlResourceLookup = new DefaultResourceLookup();
			controlResourceLookup.setResourceExpressions(controlRequestXPathExpressions);
			controlResourceLookup.setExpressionResolver(expressionResolver);
			requestValidator.setControlResourceLookup(controlResourceLookup );
			validators.add(requestValidator);
		}
		sender.setRequestValidators(validators.toArray(new RequestValidator[validators.size()]));
	}

	public String[] getResponseXPathExpressions() {
		return responseXPathExpressions;
	}

	/**
	 * XPath expression used to determine which resource use as the response. 
	 * For example <code>concat('mock-responses/',$uri.host, '/', local-name(//soapenv:Body/*[1]),'/',//ns:from,'-',//ns:to,'-response.xml')</code>.
	 * If no resource found with given name, next expression is resolved and resource looked-up.
	 * @param responseXPathExpressions
	 */
	public void setResponseXPathExpressions(String[] responseXPathExpressions) {
		this.responseXPathExpressions = responseXPathExpressions;
	}

	public Map<String, String> getNamespaceMap() {
		return namespaceMap;
	}
	/**
	 * Set's namespace map for the XPath expressions. Key is the namespace prefix and value is the namespace itself.
	 * @param namespaceMap
	 */
	public void setNamespaceMap(Map<String, String> namespaceMap) {
		this.namespaceMap = namespaceMap;
	}

	public Resource[] getRequestValidationSchemas() {
		return requestValidationSchemas;
	}

	/**
	 * Schemas used to validate requests.
	 * @param requestValidationSchemas
	 */
	public void setRequestValidationSchemas(Resource[] requestValidationSchemas) {
		this.requestValidationSchemas = requestValidationSchemas;
	}

	public String[] getControlRequestXPathExpressions() {
		return controlRequestXPathExpressions;
	}

	/**
	 *  XPath expression used to determine which resource use to compare validity of the request. 
	 * @param controlRequestXPathExpressions
	 */
	public void setControlRequestXPathExpressions(String[] controlRequestXPathExpressions) {
		this.controlRequestXPathExpressions = controlRequestXPathExpressions;
	}



}
