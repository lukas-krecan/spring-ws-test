package net.javacrumbs.springws.test.helper;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.expression.XPathExpressionResolver;
import net.javacrumbs.springws.test.lookup.SimpleResourceLookup;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.validator.ExpressionAssertRequestValidator;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.FaultAwareWebServiceMessage;
import org.springframework.ws.WebServiceMessage;

public class MessageValidator {

	private final WebServiceMessage message;
	
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

	private TemplateProcessor templateProcessor = new XsltTemplateProcessor();

	private Map<String, String> namespaceMapping = Collections.emptyMap();
	
	public MessageValidator(WebServiceMessage message) {
		this.message = message;
	}
	
	public MessageValidator compare(String controlResourcePath) {
		return compare(getResource(controlResourcePath));
	}


	public MessageValidator compare(Resource controlResource) {
		RequestProcessor requestValidator = createRequestComparator(controlResource);
		try {
			requestValidator.processRequest(null, null, message);
		} catch (IOException e) {
			processIOException(e);
		}
		return this;
	}

	protected void processIOException(IOException e) {
		throw new WsTestException("Error when comapring messages", e);
	}
		
	/**
	 * Creates {@link RequestProcessor} that will compare the response with the controlResource 
	 * @param controlResource
	 * @return
	 */
	protected RequestProcessor createRequestComparator(Resource controlResource) {
		XmlCompareRequestValidator requestValidator = new XmlCompareRequestValidator();
		SimpleResourceLookup controlResourceLookup = new SimpleResourceLookup(controlResource);
		controlResourceLookup.setTemplateProcessor(templateProcessor);
		requestValidator.setControlResourceLookup(controlResourceLookup);
		return requestValidator;
	}
	
	/**
	 * Validates if the message corresponds to given XSD.
	 * @param message
	 */
	public MessageValidator validate(Resource schema, Resource... schemas) throws IOException{
		SchemaRequestValidator validator = new SchemaRequestValidator();
		Resource[] joinedSchemas = new Resource[schemas.length+1];
		joinedSchemas[0] = schema;
		System.arraycopy(schemas, 0, joinedSchemas, 1, schemas.length);
		validator.setSchemas(joinedSchemas);
		validator.afterPropertiesSet();
		
		validator.processRequest(null, null, message);
		return this;
	}
	
	/**
	 * Validates if the message corresponds to given XSDs.
	 * @param message
	 */
	public MessageValidator validate(String schemaPath, String... schemaPaths) throws IOException{
		Resource[] schemas = new Resource[schemaPaths.length];
		for (int i = 0; i < schemas.length; i++) {
			schemas[i] = resourceLoader.getResource(schemaPaths[i]);
		}
		return validate(resourceLoader.getResource(schemaPath), schemas);
	}
	
	/**
	 * Sets namespace mapping for all XPath validations like {@link #assertXPath(String)}
	 * @param namespaceMapping
	 * @return
	 */
	public MessageValidator useNamespaceMapping(Map<String, String> namespaceMapping) {
		this.namespaceMapping  = namespaceMapping;
		return this;
	}
	
	/**
	 * Asserts XPath expression. If the expression is not evaluated to true, throws {@link WsTestException}.
	 * It is possible to set namespace mapping using {@link #useNamespaceMapping(Map)}.
	 * @param xpath
	 * @return
	 */
	public MessageValidator assertXPath(String xpath) {
		ExpressionAssertRequestValidator validator = new ExpressionAssertRequestValidator();
		validator.setAssertExpression(xpath);
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMapping);
		validator.setExpressionResolver(expressionResolver);
		try {
			validator.processRequest(null, null, message);
		} catch (IOException e) {
			processIOException(e);
		}
		return this;
	}
	

	/**
	 * Asserts that message is not SOAP fault.
	 * @return
	 */
	public MessageValidator assertNotSoapFault() {
		if (isSoapFault())
		{
			throw new WsTestException("Message is SOAP fault");
		}
		return this;
	}
	
	/**
	 * Assert that message is SOAP fault.
	 */
	public MessageValidator assertSoapFault() {
		if (!isSoapFault())
		{
			throw new WsTestException("Message is not SOAP fault");
		}
		return this;
	}

	/**
	 * Returns true if message is SOAP fault.
	 * @return
	 */
	protected boolean isSoapFault() {
		if (message instanceof FaultAwareWebServiceMessage)
		{
			FaultAwareWebServiceMessage faMessage = (FaultAwareWebServiceMessage)message;
			if (faMessage.hasFault()) 
			{
				return true;
			}
		}
		return false;
	}


	protected Resource getResource(String resourcePath) {
		return resourceLoader.getResource(resourcePath);
	}
	
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public TemplateProcessor getTemplateProcessor() {
		return templateProcessor;
	}

	public void setTemplateProcessor(TemplateProcessor templateProcessor) {
		this.templateProcessor = templateProcessor;
	}
	
	public MessageValidator useTemplateProcessor(TemplateProcessor templateProcessor) {
		setTemplateProcessor(templateProcessor);
		return this;
	}

	public WebServiceMessage getMessage() {
		return message;
	}


}
