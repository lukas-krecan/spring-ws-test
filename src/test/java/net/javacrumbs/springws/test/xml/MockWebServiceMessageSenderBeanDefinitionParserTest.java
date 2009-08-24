package net.javacrumbs.springws.test.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import net.krecan.springws.test.MockWebServiceMessageSender;
import net.krecan.springws.test.generator.ResponseGenerator;
import net.krecan.springws.test.validator.RequestValidator;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MockWebServiceMessageSenderBeanDefinitionParserTest {
	
	@Test
	public void testSchemaBased()
	{
		configurationTest("classpath:context-schema-based.xml");
	}
	@Test
	public void testSchemaBasedAutowired()
	{
		configurationTest("classpath:context-schema-based-autowired.xml");
	}
	@Test
	public void testFactoryBased()
	{
		configurationTest("classpath:context-factory-based.xml");
	}

	private void configurationTest(String contextPath) {
		ApplicationContext context = new ClassPathXmlApplicationContext(contextPath);
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) context.getBean("mock-sender");
		assertNotNull(sender);
		Collection<ResponseGenerator> responseGenerators = sender.getResponseGenerators();
		assertNotNull(responseGenerators);
		assertEquals(1, responseGenerators.size());

		Collection<RequestValidator> requestValidators = sender.getRequestValidators();
		assertNotNull(requestValidators);
		assertEquals(2, requestValidators.size());
	}
}
