package net.javacrumbs.springws.test.util;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.WebServiceMessageSender;


public class MockMessageSenderInjectorTest {

	@Test
	public void testAuto()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("inject/inject-auto.xml");
		WebServiceTemplate wsTemplate = (WebServiceTemplate) context.getBean("ws-template");
		WebServiceMessageSender mockSender = (WebServiceMessageSender) context.getBean("mock-sender");
		assertSame(mockSender, wsTemplate.getMessageSenders()[0]);
	
		
	}
	@Test(expected=BeanCreationException.class)
	public void testNoTemplate()
	{
		new ClassPathXmlApplicationContext("inject/inject-no-template.xml");
	}
}
