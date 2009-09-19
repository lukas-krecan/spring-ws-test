/**
 * Copyright 2006 the original author or authors.
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import net.javacrumbs.springws.test.generator.ResponseGenerator;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;


/**
 * Mock WS connection that instead of actually calling the WS uses {@link ResponseGenerator}s
 * to generate the response.
 * @author Lukas Krecan
 *
 */
public class MockWebServiceConnection implements WebServiceConnection {

	private final URI uri;

	private WebServiceMessage request;
	
	private Collection<ResponseGenerator> responseGenerators;
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public MockWebServiceConnection(URI uri) {
		this.uri = uri;
	}

	/**
	 * Stores the message.
	 */
	public void send(WebServiceMessage message) throws IOException {
		request = message;
	}

	/**
	 * Generates mock response
	 */
	public WebServiceMessage receive(WebServiceMessageFactory messageFactory) throws IOException {
		return generateResponse(messageFactory);
	}

	/**
	 * Calls all generators. If a generator returns <code>null</code>, the next generator is called. If all generators return  <code>null</code>
	 * {@link MockWebServiceConnection#handleResponseNotFound} method is called. In default implementation it throws {@link ResponseGeneratorNotSpecifiedException}.
	 * @param messageFactory
	 * @return
	 * @throws IOException
	 */
	protected WebServiceMessage generateResponse(WebServiceMessageFactory messageFactory) throws IOException {
		WebServiceMessage response = null;
		if (responseGenerators!=null)
		{
			for (ResponseGenerator responseGenerator: responseGenerators)
			{
				response = responseGenerator.generateResponse(uri, messageFactory, request);
				if (response!=null)
				{
					return response;
				}
			}
		}
		return handleResponseNotFound(messageFactory);
	}

	/**
	 * Throws {@link ResponseGeneratorNotSpecifiedException}. Can be overrriden.
	 * @param messageFactory
	 * @return
	 */
	protected WebServiceMessage handleResponseNotFound(WebServiceMessageFactory messageFactory) {
		throw new ResponseGeneratorNotSpecifiedException("No response generator configured for uri "+uri+".");
	}
	

	public void close() throws IOException {
		
	}

	public String getErrorMessage() throws IOException {
		return null;
	}

	public URI getUri() throws URISyntaxException {
		return uri;
	}

	public boolean hasError() throws IOException {
		return false;
	}


	
	public WebServiceMessage getRequest() {
		return request;
	}


	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

	public Collection<ResponseGenerator> getResponseGenerators() {
		return responseGenerators;
	}

	public void setResponseGenerators(Collection<ResponseGenerator> responseGenerators) {
		this.responseGenerators = responseGenerators;
	}
}
