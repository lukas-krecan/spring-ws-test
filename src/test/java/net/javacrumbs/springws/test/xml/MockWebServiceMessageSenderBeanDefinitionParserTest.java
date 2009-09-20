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
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.generator.ResponseGenerator;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MockWebServiceMessageSenderBeanDefinitionParserTest {
	
	@Test
	public void testSchemaBased()
	{
		configurationTest("classpath:context/context-schema-based.xml");
	}
	@Test
	public void testSchemaBasedAutowired()
	{
		configurationTest("classpath:context/context-schema-based-autowired.xml");
	}
	@Test
	public void testSchemaBasedAutowiredPlaceholder()
	{
		configurationTest("classpath:context/context-schema-based-autowired-placeholder.xml");
	}
	@Test
	public void testFactoryBased()
	{
		configurationTest("classpath:context/context-factory-based.xml");
	}
	@Test
	public void testFactoryBasedAutowired()
	{
		configurationTest("classpath:context/context-factory-based-autowired.xml");
	}
	@Test
	public void testContextComplex()
	{
		configurationTest("classpath:context/context-complex.xml");
	}

	private void configurationTest(String contextPath) {
		ApplicationContext context = new ClassPathXmlApplicationContext(contextPath);
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) context.getBean("mock-sender");
		assertNotNull(sender);
		Collection<ResponseGenerator> responseGenerators = sender.getResponseGenerators();
		assertNotNull(responseGenerators);
		assertEquals(3, responseGenerators.size());
		Iterator<ResponseGenerator> iterator = responseGenerators.iterator();
		assertEquals(XmlCompareRequestValidator.class, iterator.next().getClass());
		assertEquals(SchemaRequestValidator.class, iterator.next().getClass());
		assertEquals(DefaultResponseGenerator.class, iterator.next().getClass());
	}
}
