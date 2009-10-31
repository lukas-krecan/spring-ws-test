/**
 * Copyright 2006 the original author or authors.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Iterator;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.generator.WebServiceTransportExceptionGenerator;
import net.javacrumbs.springws.test.lookup.PayloadRootBasedResourceLookup;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;
import net.javacrumbs.springws.test.validator.XPathRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class PayloadRootMockWebServiceMessageSenderBeanDefinitionParserTest {
	
	@Test
	public void testFactoryBasedAutowired()
	{
		configurationTest("classpath:context/simple-context-factory-based-autowired.xml");
	}

	private void configurationTest(String contextPath) {
		ApplicationContext context = new ClassPathXmlApplicationContext(contextPath);
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) context.getBean("mock-sender");
		assertNotNull(sender);
		Collection<RequestProcessor> requestProcessors = sender.getRequestProcessors();
		assertNotNull(requestProcessors);
		assertEquals(5, requestProcessors.size());
		Iterator<RequestProcessor> iterator = requestProcessors.iterator();
		XmlCompareRequestValidator compareRequestValidator = (XmlCompareRequestValidator) iterator.next();
		PayloadRootBasedResourceLookup controlResourceLookup = (PayloadRootBasedResourceLookup) compareRequestValidator.getControlResourceLookup();
		assertNotNull(controlResourceLookup.getPayloadDelimiter());
		assertEquals("request.xml",controlResourceLookup.getPathSuffix());
		
		assertEquals(SchemaRequestValidator.class, iterator.next().getClass());
		
		XPathRequestValidator xpathRequestValidator = (XPathRequestValidator)iterator.next();
		assertEquals("Unsupported service class", xpathRequestValidator.getExceptionMapping().get("//ns:serviceClass != 'economy' and //ns:serviceClass != 'business'"));
		
		
		assertEquals(WebServiceTransportExceptionGenerator.class, iterator.next().getClass());
		assertEquals(DefaultResponseGenerator.class, iterator.next().getClass());
	}
}
