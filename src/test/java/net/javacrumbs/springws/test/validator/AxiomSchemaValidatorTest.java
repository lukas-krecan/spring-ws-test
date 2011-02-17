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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.javacrumbs.springws.test.WsTestException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

public class AxiomSchemaValidatorTest extends SchemaRequestValidatorTest {

	public AxiomSchemaValidatorTest() throws Exception {
		super();
	}

	@Override
	protected void initializeMessageFactory() {
		initializeAxiomMessageFactory();
	}
	
	@Test(expected=WsTestException.class)
	public void testInvalid() throws Exception
	{
		WebServiceMessage message = getInvalidMessage();
		createValidator().validateRequest(null, message);
	}
	
	@Test(expected=WsTestException.class)
	public void testSetSchema() throws Exception
	{
		SchemaRequestValidator validator = new SchemaRequestValidator();
		validator.setXsdSchema(new SimpleXsdSchema(new ClassPathResource("xml/schema.xsd")));
		validator.afterPropertiesSet();
		assertNotNull(validator.getValidator());
		
		WebServiceMessage message = getInvalidMessage();
		createValidator().validateRequest(null, message);
	}
	@Test(expected=WsTestException.class)
	public void testSetSchemaCollection() throws Exception
	{
		SchemaRequestValidator validator = new SchemaRequestValidator();
		CommonsXsdSchemaCollection schemaCollection = new CommonsXsdSchemaCollection(new Resource[]{new ClassPathResource("xml/schema.xsd")});
		schemaCollection.afterPropertiesSet();
		validator.setXsdSchemaCollection(schemaCollection);
		validator.afterPropertiesSet();
		assertNotNull(validator.getValidator());
		
		WebServiceMessage message = getInvalidMessage();
		createValidator().validateRequest(null, message);
	}
}
