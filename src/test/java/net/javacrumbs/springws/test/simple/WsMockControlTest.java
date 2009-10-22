package net.javacrumbs.springws.test.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.junit.Test;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.xml.transform.StringResult;


public class WsMockControlTest extends AbstractMessageTest{
	
	private static final Map<String, String> NS_MAP = Collections.singletonMap("ns", "http://www.example.org/schema");

	@Test
	public void testExpectAndReturn() throws IOException
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new WsMockControl().expectRequest("xml/control-message-test.xml").returnResponse("mock-responses/test/default-response.xml").createMock();
		assertNotNull(sender);
		assertEquals(3, sender.getRequestProcessors().size());
		
		DefaultResourceLookup lookup1 = (DefaultResourceLookup)((XmlCompareRequestValidator)extractRequestProcessor(sender,0)).getControlResourceLookup();
		assertEquals("'xml/control-message-test.xml'", lookup1.getResourceExpressions()[0]);
		
		DefaultResourceLookup lookup2 = (DefaultResourceLookup)((DefaultResponseGenerator)extractRequestProcessor(sender,1)).getResourceLookup();
		assertEquals("'mock-responses/test/default-response.xml'", lookup2.getResourceExpressions()[0]);
		
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	
			
	private RequestProcessor extractRequestProcessor(MockWebServiceMessageSender sender, int index) {
		//first processor is UsageValidator, hence index + 1
		return sender.getRequestProcessors().get(index+1);
	}
	
	@Test(expected=WsTestException.class)
	public void testExpectResourceNotFound() throws IOException
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new WsMockControl().expectRequest("xml/does-not-exist.xml").returnResponse("mock-responses/test/default-response.xml").createMock();
		assertNotNull(sender);
		assertEquals(3, sender.getRequestProcessors().size());
		
		
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	@Test(expected=WsTestException.class)
	public void testXPathValidation() throws IOException
	{
		Map<String, String> nsMap = NS_MAP;
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new WsMockControl().expectRequest("xml/control-message-test.xml")
																					.failIf("//ns:number!=1",nsMap).returnResponse("mock-responses/test/default-response.xml").createMock();
		assertNotNull(sender);
		assertEquals(4, sender.getRequestProcessors().size());
		
	
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	@Test(expected=WsTestException.class)
	public void testXPathAssertion() throws IOException
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new WsMockControl().expectRequest("xml/control-message-test.xml")
														.assertThat("//ns:number=1",NS_MAP).returnResponse("mock-responses/test/default-response.xml").createMock();
		assertNotNull(sender);
		assertEquals(4, sender.getRequestProcessors().size());
		
		
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	
	@Test(expected=WsTestException.class)
	public void testExpectAndThrow() throws IOException
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new WsMockControl().expectRequest("xml/control-message-test.xml").throwException(new WsTestException("Test error")).createMock();
		assertNotNull(sender);
		assertEquals(3, sender.getRequestProcessors().size());
		
		DefaultResourceLookup lookup1 = (DefaultResourceLookup)((XmlCompareRequestValidator)extractRequestProcessor(sender, 0)).getControlResourceLookup();
		assertEquals("'xml/control-message-test.xml'", lookup1.getResourceExpressions()[0]);
			
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	
	@Test
	public void testVerify() throws IOException
	{
		WsMockControl mockControl = new WsMockControl();
		try
		{
			mockControl.verify();
			fail("WsTestException expected");
		} 
		catch(WsTestException e)
		{
			assertEquals("Unexpected number of WebServiceTemplate calls, expected from 1 to 1 calls, was 0.",e.getMessage());
		}
		
		WebServiceMessageSender sender = mockControl.returnResponse("mock-responses/test/default-response.xml").createMock();
		
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), new StringResult() );
		
		mockControl.verify();
		try
		{
			template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), new StringResult() );
			fail("WsTestException expected");
		} 
		catch(WsTestException e)
		{
			assertEquals("Unexpected number of WebServiceTemplate calls, expected from 1 to 1 calls, was 2.",e.getMessage());
		}
	}
	
	@Test
	public void testUsageValidator()
	{
		WsMockControl mockControl = new WsMockControl();
		expectMinAndMaxUsage(mockControl, 1, 1);
	}
	@Test
	public void testUsageValidatorOnce()
	{
		WsMockControl mockControl = new WsMockControl().once();
		expectMinAndMaxUsage(mockControl, 1, 1);
	}
	@Test
	public void testUsageValidatorAnyTimes()
	{
		WsMockControl mockControl = new WsMockControl().anyTimes();
		expectMinAndMaxUsage(mockControl, 0, Integer.MAX_VALUE);
	}
	@Test
	public void testUsageValidatorTimes1()
	{
		WsMockControl mockControl = new WsMockControl().times(5);
		expectMinAndMaxUsage(mockControl, 5, 5);
	}
	@Test
	public void testUsageValidatorTimes()
	{
		WsMockControl mockControl = new WsMockControl().times(2,5);
		expectMinAndMaxUsage(mockControl, 2, 5);
	}
	@Test
	public void testUsageValidatorAtleastOnce()
	{
		WsMockControl mockControl = new WsMockControl().atLeastOnce();
		expectMinAndMaxUsage(mockControl, 1, Integer.MAX_VALUE);
	}



	private void expectMinAndMaxUsage(WsMockControl mockControl, int min, int max) {
		assertEquals(min, mockControl.getUsageValidator().getMinNumberOfProcessedRequests());
		assertEquals(max, mockControl.getUsageValidator().getMaxNumberOfProcessedRequests());
	}
		
}
