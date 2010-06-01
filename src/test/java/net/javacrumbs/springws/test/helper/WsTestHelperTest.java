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

package net.javacrumbs.springws.test.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.context.WsTestContextHolder;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;

import org.custommonkey.xmlunit.Diff;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.MessageDispatcher;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.WebServiceMessageReceiver;
import org.w3c.dom.Document;

public class WsTestHelperTest extends AbstractMessageTest{

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
	
	@SuppressWarnings("deprecation")
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
	@SuppressWarnings("deprecation")
	@Test
	public void testSendPayloadMessageAndCompareResponse() throws Exception
	{
		WsTestHelper wsServerTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsServerTestHelper.setApplicationContext(applicationContext);
		wsServerTestHelper.afterPropertiesSet();
		MessageContext message = wsServerTestHelper.receiveMessage("xml/valid-message-payload.xml");
		assertNotNull(message);
		assertNotNull(message.getResponse().getPayloadSource());
		
		wsServerTestHelper.compareMessage("mock-responses/test/default-response.xml", message.getResponse());
	}
	@SuppressWarnings("deprecation")
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
	@Test
	public void testLoadMessage() throws Exception
	{
		WsTestHelper wsTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsTestHelper.setApplicationContext(applicationContext);
		wsTestHelper.afterPropertiesSet();
		
		WebServiceMessage message = wsTestHelper.loadMessage("xml/valid-message.xml");
		assertNotNull(message);
		Document document = DefaultXmlUtil.getInstance().loadDocument(message.getPayloadSource());
		assertNotNull(document);
	}
	@Test
	public void testLoadTemplate() throws Exception
	{
		
		WsTestHelper wsTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsTestHelper.setApplicationContext(applicationContext);
		wsTestHelper.afterPropertiesSet();
		
		WsTestContextHolder.getTestContext().setAttribute("a", 1);
		WsTestContextHolder.getTestContext().setAttribute("b", 2);

		WebServiceMessage message = wsTestHelper.loadMessage("xml/request-context-xslt.xml");
		Document controlDocument = loadDocument("xml/request1-envelope.xml");
		Diff diff = new Diff(controlDocument, loadDocument(message));
		assertTrue(diff.toString(), diff.similar());
	}
	
	@SuppressWarnings("deprecation")
	@Test(expected=WsTestException.class)
	public void testValidateResponseFail() throws Exception
	{
		WsTestHelper wsTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsTestHelper.setApplicationContext(applicationContext);
		wsTestHelper.afterPropertiesSet();
		
		WebServiceMessage message = wsTestHelper.loadMessage("xml/invalid-message.xml");
		wsTestHelper.validateMessage(message, "xml/schema.xsd");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testValidateResponseOk() throws Exception
	{
		WsTestHelper wsTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsTestHelper.setApplicationContext(applicationContext);
		wsTestHelper.afterPropertiesSet();
		
		WebServiceMessage message = wsTestHelper.loadMessage("xml/valid-message.xml");
		wsTestHelper.validateMessage(message, "xml/schema.xsd");
	}
	@SuppressWarnings("deprecation")
	@Test(expected=WsTestException.class)
	public void testValidateResponseMultipleSchemesFail() throws Exception
	{
		WsTestHelper wsTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsTestHelper.setApplicationContext(applicationContext);
		wsTestHelper.afterPropertiesSet();
		
		WebServiceMessage message = wsTestHelper.loadMessage("xml/invalid-message.xml");
		wsTestHelper.validateMessage(message, "xml/calc.xsd", "xml/schema.xsd");
	}
	@SuppressWarnings("deprecation")
	@Test(expected=WsTestException.class)
	public void testValidateResponseMultipleSchemesFail2() throws Exception
	{
		WsTestHelper wsTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsTestHelper.setApplicationContext(applicationContext);
		wsTestHelper.afterPropertiesSet();
		
		WebServiceMessage message = wsTestHelper.loadMessage("xml/invalid-message.xml");
		wsTestHelper.validateMessage(message, "xml/schema.xsd", "xml/calc.xsd");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testValidateResponseMultipleSchemesOk() throws Exception
	{
		WsTestHelper wsTestHelper = new WsTestHelper();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/server/dispatcher.xml");
		wsTestHelper.setApplicationContext(applicationContext);
		wsTestHelper.afterPropertiesSet();
		
		WebServiceMessage message = wsTestHelper.loadMessage("xml/valid-message.xml");
		wsTestHelper.validateMessage(message, "xml/calc.xsd", "xml/schema.xsd");
		wsTestHelper.validateMessage(message, "xml/schema.xsd", "xml/calc.xsd");
	}
	
}
