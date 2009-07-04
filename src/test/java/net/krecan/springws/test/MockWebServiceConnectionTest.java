package net.krecan.springws.test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.net.URI;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;


public class MockWebServiceConnectionTest {

	@Test
	public void testSendWithoutValidator() throws Exception
	{
		URI uri = new URI("http://example.org/");
		
		MockWebServiceConnection connection = new MockWebServiceConnection(uri);
		
		WebServiceMessage message = createMock(WebServiceMessage.class);
		
		replay(message);
		
		connection.send(message);
		
		assertSame(message, connection.getRequest());
		verify(message);
	}
	@Test
	public void testSendAndValidate() throws Exception
	{
		URI uri = new URI("http://example.org/");
		
		MockWebServiceConnection connection = new MockWebServiceConnection(uri);

		WebServiceMessage message = createMock(WebServiceMessage.class);
		
		RequestValidator requestValidator = createMock(RequestValidator.class);
		requestValidator.validate(uri, message);
		connection.setRequestValidator(requestValidator);
		
		
		replay(message, requestValidator);
		
		connection.send(message);
		
		assertSame(message, connection.getRequest());
		verify(message, requestValidator);
	}
}
