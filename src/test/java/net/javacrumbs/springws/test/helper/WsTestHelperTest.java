package net.javacrumbs.springws.test.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.javacrumbs.springws.test.WsTestException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.MessageDispatcher;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.WebServiceMessageReceiver;

public class WsTestHelperTest {

	@Test
	public void testCreateDefault() throws Exception
	{
		WsTestHelper wsServerTestHelper = new WsTestHelper();
		wsServerTestHelper.afterPropertiesSet();
		assertTrue(wsServerTestHelper.getWebServiceMessageReceiver() instanceof WebServiceMessageReceiver);
		assertTrue(wsServerTestHelper.getMessageFactory() instanceof WebServiceMessageFactory);
	}
	@Test
	public void testInitializeSet() throws Exception
	{
		WsTestHelper wsServerTestHelper = new WsTestHelper();
		MessageDispatcher dispatcher = new MessageDispatcher();
		wsServerTestHelper.setWebServiceMessageReceiver(dispatcher);
		wsServerTestHelper.afterPropertiesSet();
		
		WebServiceMessageFactory messageFactory = new SaajSoapMessageFactory();
		wsServerTestHelper.setMessageFactory(messageFactory);
		
		assertSame(dispatcher, wsServerTestHelper.getWebServiceMessageReceiver());
		assertSame(messageFactory, wsServerTestHelper.getMessageFactory());
	}
	@Test
	public void testInitializeFromApplicationContext() throws Exception
	{
		
		WsTestHelper wsServerTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsServerTestHelper.setApplicationContext(applicationContext);
		wsServerTestHelper.afterPropertiesSet();
		
		assertTrue(wsServerTestHelper.getWebServiceMessageReceiver() instanceof WebServiceMessageReceiver);
		assertEquals(applicationContext.getBean("messageReceiver"),wsServerTestHelper.getWebServiceMessageReceiver());
		assertEquals(applicationContext.getBean("messageFactory"),wsServerTestHelper.getMessageFactory());
	}
	
	@Test
	public void testSendMessageAndCompareResponse() throws Exception
	{
		WsTestHelper wsServerTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsServerTestHelper.setApplicationContext(applicationContext);
		wsServerTestHelper.afterPropertiesSet();
		MessageContext message = wsServerTestHelper.receiveMessage("xml/valid-message.xml");
		assertNotNull(message);
		assertNotNull(message.getResponse().getPayloadSource());
		
		wsServerTestHelper.compareMessage("mock-responses/test/default-response.xml", message.getResponse());
	}
	@Test
	public void testSendMessageAndCompareResponseFail() throws Exception
	{
		WsTestHelper wsServerTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsServerTestHelper.setApplicationContext(applicationContext);
		wsServerTestHelper.afterPropertiesSet();
		MessageContext message = wsServerTestHelper.receiveMessage("xml/valid-message.xml");
		assertNotNull(message);
		assertNotNull(message.getResponse().getPayloadSource());
		
		try
		{
			wsServerTestHelper.compareMessage("mock-responses/test/different-response.xml", message.getResponse());
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			//ok
		}
	}
}
