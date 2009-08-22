package net.javacrumbs.springws.test.xml;

import static org.junit.Assert.*;
import net.krecan.springws.test.MockWebServiceMessageSender;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MockMessageSenderBeanDefinitionParserTest {
	@Autowired
	
	@Test
	public void testParse()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:schema-based-context.xml");
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) context.getBean("mock-sender");
		assertNotNull(sender);
	}
}
