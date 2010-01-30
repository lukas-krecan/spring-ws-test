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

public abstract class AbstractMockWebServiceMessageSender implements WebServiceMessageSender{

	private List<EndpointInterceptor> interceptors = Collections.emptyList();

	public AbstractMockWebServiceMessageSender() {
		super();
	}

	public WebServiceConnection createConnection(URI uri) throws IOException {
		MockWebServiceConnection connection = new MockWebServiceConnection(uri);
		connection.setRequestProcessors(getRequestProcessors());
		connection.setInterceptors(interceptors);
		return connection;
	}

	protected abstract List<RequestProcessor> getRequestProcessors();

	public boolean supports(URI uri) {
		return true;
	}

	public List<EndpointInterceptor> getInterceptors() {
		return Collections.unmodifiableList(interceptors);
	}

	public void setInterceptors(List<? extends EndpointInterceptor> interceptors) {
		this.interceptors = new ArrayList<EndpointInterceptor>(interceptors);
	}

}