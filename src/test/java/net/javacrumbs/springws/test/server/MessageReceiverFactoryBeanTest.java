package net.javacrumbs.springws.test.server;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.server.MessageDispatcher;
import org.springframework.ws.transport.WebServiceMessageReceiver;

public class MessageReceiverFactoryBeanTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateMessageDispatcher()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:context/server/dispatcher.xml");
		Map<String, Object> beansOfType = context.getBeansOfType(WebServiceMessageReceiver.class);
		assertEquals(1, beansOfType.size());
		WebServiceMessageReceiver bean = (WebServiceMessageReceiver) beansOfType.values().iterator().next();
		assertNotNull(bean);
		assertTrue(bean instanceof MessageDispatcher);
	}
}
