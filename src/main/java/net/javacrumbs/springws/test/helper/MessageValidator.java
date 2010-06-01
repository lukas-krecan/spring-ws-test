/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.javacrumbs.springws.test.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.common.DefaultMessageComparator;
import net.javacrumbs.springws.test.common.MessageComparator;
import net.javacrumbs.springws.test.common.SchemaValidator;
import net.javacrumbs.springws.test.expression.XPathExpressionResolver;
import net.javacrumbs.springws.test.template.FreeMarkerTemplateProcessor;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.validator.ExpressionAssertRequestValidator;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.FaultAwareWebServiceMessage;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.validation.XmlValidator;
import org.springframework.xml.validation.XmlValidatorFactory;

public class MessageValidator {

	private final WebServiceMessage message;
	
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

	private TemplateProcessor templateProcessor = new XsltTemplateProcessor();

	private Map<String, String> namespaceMapping = new HashMap<String, String>();
	
	private MessageComparator messageComparator = new DefaultMessageComparator();
	
	private SchemaValidator schemaValidator = new SchemaValidator();

	private String schemaLanguage = XmlValidatorFactory.SCHEMA_W3C_XML;
	
	public MessageValidator(WebServiceMessage message) {
		this.message = message;
	}
	
	public MessageValidator compare(String controlResourcePath) {
		return compare(getResource(controlResourcePath));
	}

	public MessageValidator compare(Resource controlResource) {
		try {
			messageComparator.compareMessage(message, preprocessResource(controlResource));
		} catch (IOException e) {
			processIOException(e);
		}
		return this;
	}

	protected void processIOException(IOException e) {
		throw new WsTestException("Error when comparing messages", e);
	}
		
	/**
	 * Validates if the message corresponds to given XSDs.
	 * @param message
	 */
	public MessageValidator validate(String schemaPath, String... schemaPaths) throws IOException {
		Resource[] schemas = new Resource[schemaPaths.length];
		for (int i = 0; i < schemas.length; i++) {
			schemas[i] = resourceLoader.getResource(schemaPaths[i]);
		}
		return validate(resourceLoader.getResource(schemaPath), schemas);
	}
	
	/**
	 * Validates if the message corresponds to given XSD.
	 * @param message
	 */
	public MessageValidator validate(Resource schema, Resource... schemas) throws IOException {
		
		Resource[] joinedSchemas = new Resource[schemas.length+1];
		joinedSchemas[0] = schema;
		System.arraycopy(schemas, 0, joinedSchemas, 1, schemas.length);
		
		validate(schemaValidator.createValidatorFromSchemas(joinedSchemas, schemaLanguage ));
		return this;
	}
	
	
	
	
	/**
	 * Validates message using generic {@link XmlValidator}. See {@link XmlValidatorFactory} for more details.
	 * @param validator
	 */
	public MessageValidator validate(XmlValidator xmlValidator) {
		try {
			schemaValidator.validate(message, xmlValidator);		
		} catch (IOException e) {
			processIOException(e);
		}
		return this;
	}
	
	/**
	 * Sets namespace mapping for all XPath validations like {@link #assertXPath(String)}
	 * @param namespaceMapping
	 * @return
	 */
	public MessageValidator useNamespaceMapping(Map<String, String> namespaceMapping) {
		this.namespaceMapping  = new HashMap<String, String>(namespaceMapping);
		return this;
	}
	
	/**
	 * Adds mapping between prefix and namespace.
	 * @param prefix
	 * @param namespace
	 * @return
	 */
	public MessageValidator addNamespaceMapping(String prefix, String namespace)
	{
		this.namespaceMapping.put(prefix, namespace);
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
	
	/**
	 * Does resource preprocessing. In default implementation just evaluates a template.
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	protected Resource preprocessResource(Resource resource) throws IOException {
		return templateProcessor.processTemplate(resource, null);
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
	
	/**
	 * From now on use FreeMarker for templates.
	 * @return
	 */
	public MessageValidator useFreeMarkerTemplateProcessor() {
		FreeMarkerTemplateProcessor freemarkerTemplateProcessor = new FreeMarkerTemplateProcessor();
		freemarkerTemplateProcessor.setResourceLoader(resourceLoader);
		freemarkerTemplateProcessor.afterPropertiesSet();
		return useTemplateProcessor(freemarkerTemplateProcessor);
	}
	
	/**
	 * From now on use XSLT for templates.
	 * @return
	 */
	public MessageValidator useXsltTemplateProcessor() {
		return useTemplateProcessor(new XsltTemplateProcessor());
	}

	public WebServiceMessage getMessage() {
		return message;
	}

	public MessageComparator getMessageComparator() {
		return messageComparator;
	}

	public void setMessageComparator(MessageComparator messageComparator) {
		this.messageComparator = messageComparator;
	}

	public MessageValidator useMessageComparator(MessageComparator messageComparator) {
		setMessageComparator(messageComparator);
		return this;
	}

	public SchemaValidator getSchemaValidator() {
		return schemaValidator;
	}

	public void setSchemaValidator(SchemaValidator schemaValidator) {
		this.schemaValidator = schemaValidator;
	}

	public MessageValidator useSchemaValidator(SchemaValidator schemaValidator) {
		setSchemaValidator(schemaValidator);
		return this;
	}

	public String getSchemaLanguage() {
		return schemaLanguage;
	}

	public void setSchemaLanguage(String schemaLanguage) {
		this.schemaLanguage = schemaLanguage;
	}
	public MessageValidator useSchemaLanguage(String schemaLanguage) {
		setSchemaLanguage(schemaLanguage);
		return this;		
	}




}
