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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Web message sender that instead of sending request using HTTP sends data to  {@link MockWebServiceConnection}.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractMockWebServiceMessageSender implements WebServiceMessageSender{

	private List<EndpointInterceptor> interceptors = Collections.emptyList();

	
	/**
	 * Creates {@link MockWebServiceConnection}.
	 */
	public WebServiceConnection createConnection(URI uri) throws IOException {
		MockWebServiceConnection connection = new MockWebServiceConnection(uri);
		connection.setRequestProcessors(getRequestProcessors());
		connection.setInterceptors(interceptors);
		return connection;
	}

	/**
	 * Returns list of request processors. To be overriden.
	 * @return
	 */
	protected abstract List<RequestProcessor> getRequestProcessors();

	/**
	 * Supports all URIs.
	 */
	public boolean supports(URI uri) {
		return true;
	}

	public List<EndpointInterceptor> getInterceptors() {
		return Collections.unmodifiableList(interceptors);
	}

	/**
	 * Sets list of interceptors to be applied on request. 
	 * @param interceptors
	 */
	public void setInterceptors(List<? extends EndpointInterceptor> interceptors) {
		this.interceptors = new ArrayList<EndpointInterceptor>(interceptors);
	}

}