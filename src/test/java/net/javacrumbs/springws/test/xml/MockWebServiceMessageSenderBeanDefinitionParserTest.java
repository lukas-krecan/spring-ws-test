package net.javacrumbs.springws.test.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.krecan.springws.test.MockWebServiceMessageSender;
import net.krecan.springws.test.generator.ResponseGenerator;
import net.krecan.springws.test.validator.RequestValidator;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MockWebServiceMessageSenderBeanDefinitionParserTest {
	@Autowired
	
	@Test
	public void testParse()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:schema-based-context.xml");
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) context.getBean("mock-sender");
		assertNotNull(sender);
		ResponseGenerator[] responseGenerators = sender.getResponseGenerators();
		assertNotNull(responseGenerators);
		assertEquals(1, responseGenerators.length);

		RequestValidator[] requestValidators = sender.getRequestValidators();
		assertNotNull(requestValidators);
		assertEquals(2, requestValidators.length);
	}
}
