package net.javacrumbs.springws.test.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.helper.WsTestHelper;

import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.MessageDispatcher;
import org.springframework.ws.transport.WebServiceMessageReceiver;

public class WsTestHelperTest {

	@Test
	public void testGetWebServiceMessageReceiver() throws Exception
	{
		WsTestHelper wsServerTestHelper = new WsTestHelper();
		wsServerTestHelper.afterPropertiesSet();
		assertTrue(wsServerTestHelper.getWebServiceMessageReceiver() instanceof WebServiceMessageReceiver);
	}
	@Test
	public void testGetWebServiceMessageReceiverSet() throws Exception
	{
		WsTestHelper wsServerTestHelper = new WsTestHelper();
		MessageDispatcher dispatcher = new MessageDispatcher();
		wsServerTestHelper.setWebServiceMessageReceiver(dispatcher);
		wsServerTestHelper.afterPropertiesSet();
		assertSame(dispatcher, wsServerTestHelper.getWebServiceMessageReceiver());
	}
	@Test
	public void testGetWebServiceMessageReceiverFromApplicationContext() throws Exception
	{
		
		WsTestHelper wsServerTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsServerTestHelper.setApplicationContext(applicationContext);
		wsServerTestHelper.afterPropertiesSet();
		
		assertTrue(wsServerTestHelper.getWebServiceMessageReceiver() instanceof WebServiceMessageReceiver);
		assertEquals(applicationContext.getBean("messageDispatcher"),wsServerTestHelper.getWebServiceMessageReceiver());
	}
	@Test(expected=NoSuchBeanDefinitionException.class)
	public void testGetWebServiceMessageReceiverFromApplicationContextTwoDispatchers() throws Exception
	{
		
		WsTestHelper wsServerTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/two-dispatchers.xml");
		wsServerTestHelper.setApplicationContext(applicationContext);
		wsServerTestHelper.afterPropertiesSet();
		wsServerTestHelper.getWebServiceMessageReceiver();
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
