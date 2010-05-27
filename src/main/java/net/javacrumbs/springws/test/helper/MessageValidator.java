package net.javacrumbs.springws.test.helper;

import java.io.IOException;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.lookup.SimpleResourceLookup;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.WebServiceMessage;

public class MessageValidator {

	private final WebServiceMessage message;
	
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

	private TemplateProcessor templateProcessor = new XsltTemplateProcessor();
	
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
			throw new WsTestException("Error when comapring messages", e);
		}
		return this;
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
	public void validate(Resource schema, Resource... schemas) throws IOException{
		SchemaRequestValidator validator = new SchemaRequestValidator();
		Resource[] joinedSchemas = new Resource[schemas.length+1];
		joinedSchemas[0] = schema;
		System.arraycopy(schemas, 0, joinedSchemas, 1, schemas.length);
		validator.setSchemas(joinedSchemas);
		validator.afterPropertiesSet();
		
		validator.processRequest(null, null, message);
	}
	
	/**
	 * Validates if the message corresponds to given XSDs.
	 * @param message
	 */
	public void validate(String schemaPath, String... schemaPaths) throws IOException{
		Resource[] schemas = new Resource[schemaPaths.length];
		for (int i = 0; i < schemas.length; i++) {
			schemas[i] = resourceLoader.getResource(schemaPaths[i]);
		}
		validate(resourceLoader.getResource(schemaPath), schemas);
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

	public WebServiceMessage getMessage() {
		return message;
	}

	

}
