package net.krecan.springws.test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.net.URI;

import net.krecan.springws.test.generator.ResponseGenerator;
import net.krecan.springws.test.validator.RequestValidator;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;


public class MockWebServiceConnectionTest {
	protected SaajSoapMessageFactory messageFactory;

	public MockWebServiceConnectionTest() throws Exception {
		messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
	}
	
	@Test
	public void testSendReceiveWithoutValidatorAndGenerator() throws Exception
	{
		URI uri = new URI("http://example.org/");
		
		MockWebServiceConnection connection = new MockWebServiceConnection(uri);
		
		WebServiceMessage message = createMock(WebServiceMessage.class);
		
		replay(message);
		
		connection.send(message);
		assertSame(message, connection.getRequest());

		WebServiceMessage response = connection.receive(messageFactory);
		assertNotNull(response);
		
		verify(message);
	}
	@Test
	public void testSendReceive() throws Exception
	{
		URI uri = new URI("http://example.org/");
		
		MockWebServiceConnection connection = new MockWebServiceConnection(uri);

		WebServiceMessage request = createMock(WebServiceMessage.class);
		
		RequestValidator requestValidator = createMock(RequestValidator.class);
		requestValidator.validate(uri, request);
		connection.setRequestValidator(requestValidator);
		
		ResponseGenerator responseGenerator = createMock(ResponseGenerator.class);
		WebServiceMessage response = createMock(WebServiceMessage.class);
		connection.setResponseGenerator(responseGenerator);
		expect(responseGenerator.generateResponse(uri, messageFactory, request)).andReturn(response);
		
		
		replay(request, requestValidator, responseGenerator);

		connection.send(request);
		assertSame(request, connection.getRequest());
		
		assertSame(response, connection.receive(messageFactory));
		
		verify(request, requestValidator, responseGenerator);
	}
}
