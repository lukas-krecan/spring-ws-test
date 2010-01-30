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
package net.javacrumbs.springws.test;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.javacrumbs.springws.test.util.DefaultXmlUtil;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;


public class MockWebServiceConnectionTest extends AbstractMessageTest{
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
		catch (NoResponseGeneratorSpecifiedException e)
		{
			//ok
		}
		verify(message);
	}
	@Test
	public void testSendReceive() throws Exception
	{
		WebServiceMessage request = createMock(WebServiceMessage.class);
		
		RequestProcessor responseGenerator = createMock(RequestProcessor.class);
		WebServiceMessage response = createMock(WebServiceMessage.class);
		connection.setRequestProcessors(Collections.singletonList(responseGenerator));
		expect(responseGenerator.processRequest(uri, messageFactory, request)).andReturn(response);
		
		
		replay(request, responseGenerator);

		connection.send(request);
		assertSame(request, connection.getRequest());
		
		assertSame(response, connection.receive(messageFactory));
		
		verify(request, responseGenerator);
	}
	@Test
	public void testSendReceiveWithTwoGenerators() throws Exception
	{
		WebServiceMessage request = createMock(WebServiceMessage.class);
		WebServiceMessage response = createMock(WebServiceMessage.class);
		
		RequestProcessor responseGenerator1 = createMock(RequestProcessor.class);
		expect(responseGenerator1.processRequest(uri, messageFactory, request)).andReturn(null);
		
		RequestProcessor responseGenerator2 = createMock(RequestProcessor.class);
		expect(responseGenerator2.processRequest(uri, messageFactory, request)).andReturn(response);
		connection.setRequestProcessors(Arrays.asList(responseGenerator1, responseGenerator2));
		
		replay(request, responseGenerator1, responseGenerator2);
		
		connection.send(request);
		assertSame(request, connection.getRequest());
		
		assertSame(response, connection.receive(messageFactory));
		
		verify(request, responseGenerator1, responseGenerator2);
	}
	
	@Test
	public void testSendWithTwoValidatorsSecondFails() throws Exception
	{
		WebServiceMessage request = createMock(WebServiceMessage.class);
		
		RequestProcessor requestValidator1 = createMock(RequestProcessor.class);
		expect(requestValidator1.processRequest(uri, messageFactory, request)).andReturn(null);
		
		RequestProcessor requestValidator2 = createMock(RequestProcessor.class);
		expect(requestValidator2.processRequest(uri, messageFactory, request)).andThrow(new WsTestException("Do not panick, this is just a test"));
	
		connection.setRequestProcessors(Arrays.asList(requestValidator1, requestValidator2));
		
		
		replay(request, requestValidator1, requestValidator2);

		try
		{
			connection.send(request);
			connection.receive(messageFactory);
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
		
		RequestProcessor requestValidator1 = createMock(RequestProcessor.class);
		expect(requestValidator1.processRequest(uri, messageFactory, request)).andThrow(new WsTestException("Do not panick, this is just a test"));
		
		RequestProcessor requestValidator2 = createMock(RequestProcessor.class);
		
		connection.setRequestProcessors(Arrays.asList(requestValidator1, requestValidator2));
		
		
		replay(request, requestValidator1, requestValidator2);
		
		try
		{
			connection.send(request);
			connection.receive(messageFactory);
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			//ok
		}
		
		verify(request, requestValidator1, requestValidator2);
	}
	
	@Test
	public void testInterceptorsOk() throws Exception
	{
		WebServiceMessage request = createMock(WebServiceMessage.class);
		
		List<EndpointInterceptor> interceptors = new ArrayList<EndpointInterceptor>();
		EndpointInterceptor interceptor1 = createMock(EndpointInterceptor.class);
		expect(interceptor1.handleRequest((MessageContext)anyObject(), isNull())).andReturn(true);
		expect(interceptor1.handleResponse((MessageContext)anyObject(), isNull())).andReturn(true);
		
		interceptors.add(interceptor1);
		
		connection.setInterceptors(interceptors);
		
		RequestProcessor requestProcessor = createMock(RequestProcessor.class);
		WebServiceMessage response = createMock(WebServiceMessage.class);
		connection.setRequestProcessors(Collections.singletonList(requestProcessor));
		expect(requestProcessor.processRequest(uri, messageFactory, request)).andReturn(response);
		
		
		replay(request, response, interceptor1, requestProcessor);
		
		connection.send(request);
		assertSame(response, connection.receive(messageFactory));
		
		verify(request, response, interceptor1, requestProcessor);
	}
	@Test
	public void testHandleFault() throws Exception
	{
		WebServiceMessage request = createMock(WebServiceMessage.class);
		
		List<EndpointInterceptor> interceptors = new ArrayList<EndpointInterceptor>();
		EndpointInterceptor interceptor1 = createMock(EndpointInterceptor.class);
		expect(interceptor1.handleRequest((MessageContext)anyObject(), isNull())).andReturn(true);
		expect(interceptor1.handleFault((MessageContext)anyObject(), isNull())).andReturn(true);
		
		interceptors.add(interceptor1);
		
		connection.setInterceptors(interceptors);
		
		RequestProcessor requestProcessor = createMock(RequestProcessor.class);
		WebServiceMessage response = createMessage("xml/fault.xml");
		connection.setRequestProcessors(Collections.singletonList(requestProcessor));
		expect(requestProcessor.processRequest(uri, messageFactory, request)).andReturn(response);
		
		
		replay(request,  interceptor1, requestProcessor);
		
		connection.send(request);
		assertSame(response, connection.receive(messageFactory));
		
		verify(request, interceptor1, requestProcessor);
	}
	@Test
	public void testInterceptorsBlockOnRequest() throws Exception
	{
		final WebServiceMessage request = createMock(WebServiceMessage.class);
		final List<Boolean> handleResponseCalled = new ArrayList<Boolean>();
		
		List<EndpointInterceptor> interceptors = new ArrayList<EndpointInterceptor>();
		final WebServiceMessage response = createMock(WebServiceMessage.class);
		EndpointInterceptor interceptor1 = new EndpointInterceptor() {
			public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
				assertSame(request, messageContext.getRequest());
				assertSame(response, messageContext.getResponse());
				handleResponseCalled.add(true);
				return false;
			}
			
			public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
				messageContext.setResponse(response);
				return false;
			}
			
			public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
				throw new AssertionError("Should not be called");
			}
		};
		interceptors.add(interceptor1);
		
		EndpointInterceptor interceptor2 = createMock(EndpointInterceptor.class);
		interceptors.add(interceptor2);
		
		
		connection.setInterceptors(interceptors);
		
		RequestProcessor requestProcessor = createMock(RequestProcessor.class);
		connection.setRequestProcessors(Collections.singletonList(requestProcessor));
		
		
		replay(request, response, requestProcessor, interceptor2);
		
		connection.send(request);
		assertSame(response, connection.receive(messageFactory));
		assertTrue(handleResponseCalled.size()>0);
		
		verify(request, response, requestProcessor, interceptor2);
	}
}
 