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
package net.javacrumbs.springws.test.xml;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.PayloadRootBasedResourceLookup;
import net.javacrumbs.springws.test.template.FreeMarkerTemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.util.MockMessageSenderInjector;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SimpleSchemaBasedTest {

	@Test
	public void testSimpleSchema()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("context/simple-schema-based-context.xml");
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) context.getBean("mock-sender");
		assertNotNull(sender);
		assertFalse(sender.isAutowireRequestProcessors());
		
		List<RequestProcessor> requestProcessors = sender.getRequestProcessors();
		assertNotNull(requestProcessors);
		
		assertEquals(3, requestProcessors.size());
		
		XmlCompareRequestValidator xmlCompareValidator = (XmlCompareRequestValidator) requestProcessors.get(0);
		assertFalse(xmlCompareValidator.isIgnoreWhitespace());
		PayloadRootBasedResourceLookup controlResourceLookup = (PayloadRootBasedResourceLookup) xmlCompareValidator.getControlResourceLookup();
		assertEquals("request.xml",controlResourceLookup.getPathSuffix());
		assertEquals("mock/", controlResourceLookup.getPathPrefix());
		assertArrayEquals(new String[]{"//ns:from","//ns:to"},controlResourceLookup.getDiscriminatorsMap().get("getFlightsRequest"));
		assertTrue(controlResourceLookup.isPrependUri());
		assertEquals(FreeMarkerTemplateProcessor.class, controlResourceLookup.getTemplateProcessor().getClass());

		SchemaRequestValidator schemaValidator = (SchemaRequestValidator) requestProcessors.get(1);
		assertEquals(1, schemaValidator.getSchemas().length);

		DefaultResponseGenerator generator = (DefaultResponseGenerator) requestProcessors.get(2);
		PayloadRootBasedResourceLookup resourceLookup = (PayloadRootBasedResourceLookup)generator.getResourceLookup();
		assertEquals("response.xml",resourceLookup.getPathSuffix());
		assertEquals("mock/", resourceLookup.getPathPrefix());
		assertArrayEquals(new String[]{"//ns:from","//ns:to"},resourceLookup.getDiscriminatorsMap().get("getFlightsRequest"));
		assertTrue(resourceLookup.isPrependUri());
		assertEquals(FreeMarkerTemplateProcessor.class, resourceLookup.getTemplateProcessor().getClass());
		
		assertEquals(0, context.getBeansOfType(MockMessageSenderInjector.class).size());
		
		assertEquals(1, sender.getInterceptors().size());

	}
	@Test
	public void testMinimalSchema()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("context/minimal-schema-based-context.xml");
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) context.getBean("mock-sender");
		assertNotNull(sender);
		assertTrue(sender.isAutowireRequestProcessors());
		
		List<RequestProcessor> requestProcessors = sender.getRequestProcessors();
		assertNotNull(requestProcessors);
		
		assertEquals(2, requestProcessors.size());
		
		XmlCompareRequestValidator xmlCompareValidator = (XmlCompareRequestValidator) requestProcessors.get(0);
		assertTrue(xmlCompareValidator.isIgnoreWhitespace());
		PayloadRootBasedResourceLookup controlResourceLookup = (PayloadRootBasedResourceLookup) xmlCompareValidator.getControlResourceLookup();
		assertEquals("request.xml",controlResourceLookup.getPathSuffix());
		assertEquals("mock-xml/", controlResourceLookup.getPathPrefix());
		assertFalse(controlResourceLookup.isPrependUri());
		assertEquals(XsltTemplateProcessor.class, controlResourceLookup.getTemplateProcessor().getClass());
			
		DefaultResponseGenerator generator = (DefaultResponseGenerator) requestProcessors.get(1);
		PayloadRootBasedResourceLookup resourceLookup = (PayloadRootBasedResourceLookup)generator.getResourceLookup();
		assertEquals("response.xml",resourceLookup.getPathSuffix());
		assertEquals("mock-xml/", resourceLookup.getPathPrefix());
		assertFalse(resourceLookup.isPrependUri());
		assertEquals(XsltTemplateProcessor.class, resourceLookup.getTemplateProcessor().getClass());
		
		assertEquals(1, context.getBeansOfType(MockMessageSenderInjector.class).size());
		
	}
}
