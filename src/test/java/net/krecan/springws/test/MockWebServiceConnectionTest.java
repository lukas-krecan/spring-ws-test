package net.krecan.springws.test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;

import net.krecan.springws.test.generator.ResponseGenerator;
import net.krecan.springws.test.util.DefaultXmlUtil;
import net.krecan.springws.test.validator.RequestValidator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;


public class MockWebServiceConnectionTest {
	protected SaajSoapMessageFactory messageFactory;
	private MockWebServiceConnection connection;
	private URI uri;

	
	@Before
	public void setUp() throws URISyntaxException
	{
		uri = new URI("http://example.org/");
		connection = new MockWebServiceConnection(uri);
		connection.setXmlUtil(
				new DefaultXmlUtil()
				{
					@Override
					public String serializeDocument(WebServiceMessage message) {
						try {
							return serializeDocument(getEnvelopeSource(message));
						} catch (UnsupportedOperationException e) {
							return "Mock";
						}
					}
				}
		);
	}
	
	public MockWebServiceConnectionTest() throws Exception {
		messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
	}
	
	@Test
	public void testSendReceiveWithoutValidatorAndGenerator() throws Exception
	{
		WebServiceMessage message = createMock(WebServiceMessage.class);
		
		replay(message);
		
		connection.send(message);
		assertSame(message, connection.getRequest());

		try
		{
			connection.receive(messageFactory);
			fail("Exception expected here");
		}
		catch (ResponseGeneratorNotSpecifiedException e)
		{
			//ok
		}
		verify(message);
	}
	@Test
	public void testSendReceive() throws Exception
	{
		WebServiceMessage request = createMock(WebServiceMessage.class);
		
		RequestValidator requestValidator = createMock(RequestValidator.class);
		requestValidator.validate(uri, request);
		connection.setRequestValidators(Collections.singleton(requestValidator));
		
		ResponseGenerator responseGenerator = createMock(ResponseGenerator.class);
		WebServiceMessage response = createMock(WebServiceMessage.class);
		connection.setResponseGenerators(Collections.singleton(responseGenerator));
		expect(responseGenerator.generateResponse(uri, messageFactory, request)).andReturn(response);
		
		
		replay(request, requestValidator, responseGenerator);

		connection.send(request);
		assertSame(request, connection.getRequest());
		
		assertSame(response, connection.receive(messageFactory));
		
		verify(request, requestValidator, responseGenerator);
	}
	@Test
	public void testSendReceiveWithTwoGenerators() throws Exception
	{
		WebServiceMessage request = createMock(WebServiceMessage.class);
		WebServiceMessage response = createMock(WebServiceMessage.class);
		
		RequestValidator requestValidator = createMock(RequestValidator.class);
		requestValidator.validate(uri, request);
		connection.setRequestValidators(Collections.singleton(requestValidator));
		
		ResponseGenerator responseGenerator1 = createMock(ResponseGenerator.class);
		expect(responseGenerator1.generateResponse(uri, messageFactory, request)).andReturn(null);
		
		ResponseGenerator responseGenerator2 = createMock(ResponseGenerator.class);
		expect(responseGenerator2.generateResponse(uri, messageFactory, request)).andReturn(response);
		connection.setResponseGenerators(Arrays.asList(responseGenerator1, responseGenerator2));
		
		replay(request, requestValidator, responseGenerator1, responseGenerator2);
		
		connection.send(request);
		assertSame(request, connection.getRequest());
		
		assertSame(response, connection.receive(messageFactory));
		
		verify(request, requestValidator, responseGenerator1, responseGenerator2);
	}
	
	@Test
	public void testSendWithTwoValidatorsSecondFails() throws Exception
	{
		WebServiceMessage request = createMock(WebServiceMessage.class);
		
		RequestValidator requestValidator1 = createMock(RequestValidator.class);
		requestValidator1.validate(uri, request);
		
		RequestValidator requestValidator2 = createMock(RequestValidator.class);
		requestValidator2.validate(uri, request);
		expectLastCall().andThrow(new WsTestException("Do not panick, this is just a test"));
		
		connection.setRequestValidators(Arrays.asList(requestValidator1, requestValidator2));
		
		
		replay(request, requestValidator1, requestValidator2);

		try
		{
			connection.send(request);
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			//ok
		}
		
		verify(request, requestValidator1, requestValidator2);
	}
	@Test
	public void testSendWithTwoValidatorsFirstFails() throws Exception
	{
		WebServiceMessage request = createMock(WebServiceMessage.class);
		
		RequestValidator requestValidator1 = createMock(RequestValidator.class);
		requestValidator1.validate(uri, request);
		expectLastCall().andThrow(new WsTestException("Do not panick, this is just a test"));
		
		RequestValidator requestValidator2 = createMock(RequestValidator.class);
		
		connection.setRequestValidators(Arrays.asList(requestValidator1, requestValidator2));
		
		
		replay(request, requestValidator1, requestValidator2);
		
		try
		{
			connection.send(request);
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			//ok
		}
		
		verify(request, requestValidator1, requestValidator2);
	}
}
