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
package net.javacrumbs.springws.test.validator;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.xml.transform.Source;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.support.interceptor.AbstractValidatingInterceptor;
import org.springframework.xml.validation.XmlValidator;
import org.springframework.xml.validation.XmlValidatorFactory;
import org.springframework.xml.xsd.XsdSchema;
import org.springframework.xml.xsd.XsdSchemaCollection;
import org.xml.sax.SAXParseException;

/**
 * Validates message using provided schema(s). Similar to the {@link AbstractValidatingInterceptor}.
 * @author Lukas Krecan
 *
 */
//TODO rename
public class SchemaRequestValidator implements InitializingBean, RequestProcessor, Ordered{

    private XmlValidator validator;
    
    private String schemaLanguage = XmlValidatorFactory.SCHEMA_W3C_XML;

    private Resource[] schemas;
    
    protected final Log logger = LogFactory.getLog(getClass());
    
	static final int DEFAULT_ORDER = 20;
	
	private int order = DEFAULT_ORDER;
    
    public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory,
    		WebServiceMessage request) throws IOException {
    	validateRequest(uri, request);
    	return null;
    }
    //TODO remove uri attribute and rename, maybe make public
	protected void validateRequest(URI uri, WebServiceMessage message) throws IOException{
		 Source requestSource = message.getPayloadSource();
         if (requestSource != null) {
             SAXParseException[] errors = validator.validate(requestSource);
             if (!ObjectUtils.isEmpty(errors)) {
                 handleRequestValidationErrors(message, errors);
             }
             logger.debug("Request message validated");
         }
         else
         {
        	 logger.warn("Request source is null");
         }
	}
	
    protected void handleRequestValidationErrors(WebServiceMessage message, SAXParseException[] errors) {
		throw new WsTestException("Request not valid. "+Arrays.toString(errors));
	}

	public void afterPropertiesSet() throws IOException {
        if (validator == null && !ObjectUtils.isEmpty(schemas)) {
            Assert.hasLength(schemaLanguage, "schemaLanguage is required");
            for (int i = 0; i < schemas.length; i++) {
                Assert.isTrue(schemas[i].exists(), "schema [" + schemas[i] + "] does not exist");
            }
            if (logger.isInfoEnabled()) {
                logger.info("Validating using \"" + StringUtils.arrayToCommaDelimitedString(schemas)+"\"");
            }
            validator = XmlValidatorFactory.createValidator(schemas, schemaLanguage);
        }
        Assert.notNull(validator, "Setting 'schema', 'schemas', 'xsdSchema', or 'xsdSchemaCollection' is required");
    }
	
	public String getSchemaLanguage() {
        return schemaLanguage;
    }

    /**
     * Sets the schema language. Default is the W3C XML Schema: <code>http://www.w3.org/2001/XMLSchema"</code>.
     *
     * @see org.springframework.xml.validation.XmlValidatorFactory#SCHEMA_W3C_XML
     * @see org.springframework.xml.validation.XmlValidatorFactory#SCHEMA_RELAX_NG
     */
    public void setSchemaLanguage(String schemaLanguage) {
        this.schemaLanguage = schemaLanguage;
    }

    /** Returns the schema resources to use for validation. */
    public Resource[] getSchemas() {
        return schemas;
    }

    /**
     * Sets the schema resource to use for validation. Setting this property, {@link
     * #setXsdSchemaCollection(XsdSchemaCollection) xsdSchemaCollection}, {@link #setSchema(Resource) schema}, or {@link
     * #setSchemas(Resource[]) schemas} is required.
     */
    public void setSchema(Resource schema) {
        setSchemas(new Resource[]{schema});
    }

    /**
     * Sets the schema resources to use for validation. Setting this property, {@link
     * #setXsdSchemaCollection(XsdSchemaCollection) xsdSchemaCollection}, {@link #setSchema(Resource) schema}, or {@link
     * #setSchemas(Resource[]) schemas} is required.
     */
    public void setSchemas(Resource[] schemas) {
        Assert.notEmpty(schemas, "schemas must not be empty or null");
        for (int i = 0; i < schemas.length; i++) {
            Assert.notNull(schemas[i], "schema must not be null");
            Assert.isTrue(schemas[i].exists(), "schema \"" + schemas[i] + "\" does not exit");
        }
        this.schemas = schemas;
    }

    /**
     * Sets the {@link XsdSchema} to use for validation. Setting this property, {@link
     * #setXsdSchemaCollection(XsdSchemaCollection) xsdSchemaCollection}, {@link #setSchema(Resource) schema}, or {@link
     * #setSchemas(Resource[]) schemas} is required.
     *
     * @param schema the xsd schema to use
     * @throws IOException in case of I/O errors
     */
    public void setXsdSchema(XsdSchema schema) throws IOException {
        this.validator = schema.createValidator();
    }

    /**
     * Sets the {@link XsdSchemaCollection} to use for validation. Setting this property, {@link
     * #setXsdSchema(XsdSchema) xsdSchema}, {@link #setSchema(Resource) schema}, or {@link #setSchemas(Resource[])
     * schemas} is required.
     *
     * @param schemaCollection the xsd schema collection to use
     * @throws IOException in case of I/O errors
     */
    public void setXsdSchemaCollection(XsdSchemaCollection schemaCollection) throws IOException {
        this.validator = schemaCollection.createValidator();
    }

	public XmlValidator getValidator() {
		return validator;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
