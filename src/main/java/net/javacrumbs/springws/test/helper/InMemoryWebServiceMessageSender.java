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

package net.javacrumbs.springws.test.helper;

import java.io.IOException;
import java.net.URI;

import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceMessageReceiver;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Sends messages locally in memory. Used for server side test.
 * @author Lukas Krecan
 *
 */
class InMemoryWebServiceMessageSender implements WebServiceMessageSender {

	private final WebServiceMessageFactory messageFactory;
	
	private final WebServiceMessageReceiver webServiceMessageReceiver;

	public InMemoryWebServiceMessageSender(WebServiceMessageFactory messageFactory, WebServiceMessageReceiver webServiceMessageReceiver) {
		this.messageFactory = messageFactory;
		this.webServiceMessageReceiver = webServiceMessageReceiver;
	}
	
	public InMemoryWebServiceConnection createConnection(URI uri) throws IOException {
		return new InMemoryWebServiceConnection(uri, messageFactory, webServiceMessageReceiver);
	}

	public boolean supports(URI uri) {
		return true;
	}

	public WebServiceMessageFactory getMessageFactory() {
		return messageFactory;
	}

	public WebServiceMessageReceiver getWebServiceMessageReceiver() {
		return webServiceMessageReceiver;
	}

}
