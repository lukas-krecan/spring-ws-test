package net.javacrumbs.springws.test.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.junit.Test;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringResult;


public class TestSimpleMessageSender extends AbstractMessageTest{
	
	@Test
	public void testExpectAndReturn() throws IOException
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new SimpleMessageFactory().expectRequest("xml/control-message-test.xml").andReturnResponse("mock-responses/test/default-response.xml");
		assertNotNull(sender);
		assertEquals(2, sender.getRequestProcessors().size());
		
		DefaultResourceLookup lookup1 = (DefaultResourceLookup)((XmlCompareRequestValidator)sender.getRequestProcessors().get(0)).getControlResourceLookup();
		assertEquals("'xml/control-message-test.xml'", lookup1.getResourceExpressions()[0]);
		
		DefaultResourceLookup lookup2 = (DefaultResourceLookup)((DefaultResponseGenerator)sender.getRequestProcessors().get(1)).getResourceLookup();
		assertEquals("'mock-responses/test/default-response.xml'", lookup2.getResourceExpressions()[0]);
		
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	
	@Test(expected=WsTestException.class)
	public void testExpectAndThrow() throws IOException
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new SimpleMessageFactory().expectRequest("xml/control-message-test.xml").andThrow(new WsTestException("Test error"));
		assertNotNull(sender);
		assertEquals(2, sender.getRequestProcessors().size());
		
		DefaultResourceLookup lookup1 = (DefaultResourceLookup)((XmlCompareRequestValidator)sender.getRequestProcessors().get(0)).getControlResourceLookup();
		assertEquals("'xml/control-message-test.xml'", lookup1.getResourceExpressions()[0]);
			
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	
	
}
