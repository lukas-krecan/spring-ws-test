package net.javacrumbs.springws.test.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.easymock.EasyMock.*;

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
import org.springframework.xml.transform.StringResult;


public class WsMockControlTest extends AbstractMessageTest{
	
	@Test
	public void testExpectAndReturn() throws IOException
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new WsMockControl().expectRequest("xml/control-message-test.xml").returnResponse("mock-responses/test/default-response.xml");
		assertNotNull(sender);
		assertEquals(2, sender.getRequestProcessors().size());
		
		DefaultResourceLookup lookup1 = (DefaultResourceLookup)((XmlCompareRequestValidator)extractRequestProcessor(sender,0)).getControlResourceLookup();
		assertEquals("'xml/control-message-test.xml'", lookup1.getResourceExpressions()[0]);
		
		DefaultResourceLookup lookup2 = (DefaultResourceLookup)((DefaultResponseGenerator)extractRequestProcessor(sender,1)).getResourceLookup();
		assertEquals("'mock-responses/test/default-response.xml'", lookup2.getResourceExpressions()[0]);
		
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	
	@Test
	public void testWrapSimple()
	{
		RequestProcessor processor = createMock(RequestProcessor.class);
		replay(processor);
		
		WsMockControl control = new WsMockControl();
		control.addRequestProcessor(processor);
		VerifiableRequestProcessorWrapper processorWrapper = (VerifiableRequestProcessorWrapper)control.getRequestProcessors().get(0);
		assertEquals(processor.toString(),processorWrapper.getRequestProcessorDescription());
		assertEquals(1, processorWrapper.getMinNumberOfProcessedRequests());
		assertEquals(1, processorWrapper.getMaxNumberOfProcessedRequests());

		verify(processor);
		
	}
	@Test(expected=IllegalStateException.class)
	public void testTimesNoProcessor()
	{
		new WsMockControl().times(0, 1);
	}
	
	@Test
	public void testWrapTimes()
	{
		WsMockControl control = new WsMockControl();
		control.expectRequest("xml/does-not-exist.xml").times(0,5);
		VerifiableRequestProcessorWrapper processorWrapper = (VerifiableRequestProcessorWrapper)control.getRequestProcessors().get(0);
		assertEquals(0, processorWrapper.getMinNumberOfProcessedRequests());
		assertEquals(5, processorWrapper.getMaxNumberOfProcessedRequests());
		assertEquals("expectRequest(\"xml/does-not-exist.xml\")",processorWrapper.getRequestProcessorDescription());
	}
	
	private RequestProcessor extractRequestProcessor(MockWebServiceMessageSender sender, int index) {
		return ((VerifiableRequestProcessorWrapper)sender.getRequestProcessors().get(index)).getWrappedRequestProcessor();
	}
	
	@Test(expected=WsTestException.class)
	public void testExpectResourceNotFound() throws IOException
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new WsMockControl().expectRequest("xml/does-not-exist.xml").returnResponse("mock-responses/test/default-response.xml");
		assertNotNull(sender);
		assertEquals(2, sender.getRequestProcessors().size());
		
		
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	@Test(expected=WsTestException.class)
	public void testXPathValidation() throws IOException
	{
		Map<String, String> nsMap = Collections.singletonMap("ns", "http://www.example.org/schema");
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new WsMockControl().expectRequest("xml/control-message-test.xml")
																					.failIf("//ns:number!=1",nsMap).returnResponse("mock-responses/test/default-response.xml");
		assertNotNull(sender);
		assertEquals(3, sender.getRequestProcessors().size());
		
	
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	@Test(expected=WsTestException.class)
	public void testXPathAssertion() throws IOException
	{
		Map<String, String> nsMap = Collections.singletonMap("ns", "http://www.example.org/schema");
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new WsMockControl().expectRequest("xml/control-message-test.xml")
														.assertThat("//ns:number=1",nsMap).returnResponse("mock-responses/test/default-response.xml");
		assertNotNull(sender);
		assertEquals(3, sender.getRequestProcessors().size());
		
		
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	
	@Test(expected=WsTestException.class)
	public void testExpectAndThrow() throws IOException
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new WsMockControl().expectRequest("xml/control-message-test.xml").throwException(new WsTestException("Test error"));
		assertNotNull(sender);
		assertEquals(2, sender.getRequestProcessors().size());
		
		DefaultResourceLookup lookup1 = (DefaultResourceLookup)((XmlCompareRequestValidator)extractRequestProcessor(sender, 0)).getControlResourceLookup();
		assertEquals("'xml/control-message-test.xml'", lookup1.getResourceExpressions()[0]);
			
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMessageSender(sender);
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult("http://example.org",createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	
	
}
