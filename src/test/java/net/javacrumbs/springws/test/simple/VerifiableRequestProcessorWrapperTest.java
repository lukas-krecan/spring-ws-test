package net.javacrumbs.springws.test.simple;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.validator.AbstractValidatorTest;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;


public class VerifiableRequestProcessorWrapperTest extends AbstractValidatorTest{

	@Test
	public void testCall() throws Exception
	{
		RequestProcessor processor = createMock(RequestProcessor.class);
		WebServiceMessage request = getValidMessage();
		WebServiceMessage response = getInvalidMessage();
		expect(processor.processRequest(TEST_URI, messageFactory, request)).andReturn(response).atLeastOnce();
		replay(processor);
		
		VerifiableRequestProcessorWrapper wrapper = new VerifiableRequestProcessorWrapper(processor,"Test processor");

		try {
			wrapper.verify();
			fail("WsTestException expected");
		} catch (WsTestException e) {
			assertEquals("Test processor: Unexpected call, expected from 1 to 1 calls, was 0.",e.getMessage());
		}
		
		//first call
		assertSame(response, wrapper.processRequest(TEST_URI, messageFactory, request));
		assertEquals(1, wrapper.getNumberOfProcessedRequests());
		
		wrapper.verify();
		
		
		try {
			//second call
			wrapper.processRequest(TEST_URI, messageFactory, request);
			fail("WsTestException expected");
		} catch (WsTestException e) {
			assertEquals("Test processor: Unexpected call, expected from 1 to 1 calls, was 2.",e.getMessage());
		}
		
		verify(processor);
		
	}
	
	@Test
	public void testTwoTimesCall() throws Exception
	{
		RequestProcessor processor = createMock(RequestProcessor.class);
		WebServiceMessage request = getValidMessage();
		WebServiceMessage response = getInvalidMessage();
		expect(processor.processRequest(TEST_URI, messageFactory, request)).andReturn(response).atLeastOnce();
		replay(processor);
		
		VerifiableRequestProcessorWrapper wrapper = new VerifiableRequestProcessorWrapper(processor, "Test processor2");
		wrapper.setMinNumberOfProcessedRequests(2);
		wrapper.setMaxNumberOfProcessedRequests(2);
		
		//first call
		assertSame(response, wrapper.processRequest(TEST_URI, messageFactory, request));
		assertEquals(1, wrapper.getNumberOfProcessedRequests());
		try {
			wrapper.verify();
			fail("WsTestException expected");
		} catch (WsTestException e) {
			assertEquals("Test processor2: Unexpected call, expected from 2 to 2 calls, was 1.",e.getMessage());
		}
		
		//second call
		assertSame(response, wrapper.processRequest(TEST_URI, messageFactory, request));
		assertEquals(2, wrapper.getNumberOfProcessedRequests());
		wrapper.verify();
		
		try {
			//third call
			wrapper.processRequest(TEST_URI, messageFactory, request);
			wrapper.verify();
			fail("WsTestException expected");
		} catch (WsTestException e) {
			assertEquals("Test processor2: Unexpected call, expected from 2 to 2 calls, was 3.",e.getMessage());
		}
		
		verify(processor);
		
	}
}
