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

import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.WebServiceConnection;

/**
 * WebService template to be used when testing WS server.
 * @author Lukas Krecan
 *
 */
public class WsTestWebServiceTemplate extends WebServiceTemplate implements InitializingBean, ApplicationContextAware {
	
	/**
	 * Empty {@link WebServiceMessageCallback}.
	 */
	private static final WebServiceMessageCallback DUMMY_REQUEST_CALLBACK = new WebServiceMessageCallback()
	{
		public void doWithMessage(WebServiceMessage message) throws IOException ,TransformerException {}
	};
	
	/**
	 * Empty {@link WebServiceMessageExtractor}.
	 */
	private static final WebServiceMessageExtractor DUMMY_MESSAGE_EXTRACTOR = new WebServiceMessageExtractor() {
		public Object extractData(WebServiceMessage message) throws IOException, TransformerException {
			return null;
		}
	};
	
	private ApplicationContext applicationContext;
	

	@Override
	public void afterPropertiesSet(){
		super.afterPropertiesSet();
		if (getDefaultUri()==null)
		{
			setDefaultUri("http://test-uri-from-spring-ws-test-should-not-be-vissible");
		}
		InMemoryWebServiceMessageSender messageSender = new InMemoryWebServiceMessageSender();
		messageSender.setApplicationContext(applicationContext);
		messageSender.setMessageFactory(getMessageFactory());
		messageSender.afterPropertiesSet();
		setMessageSender(messageSender);
	}
	
	
	/**
	 * Sends message directly.
	 * @param context
	 * @throws IOException
	 */
	protected void send(MessageContext context) throws IOException {
		WebServiceConnection connection = getMessageSenders()[0].createConnection(URI.create(getDefaultUri()));
		doSendAndReceive(context, connection, DUMMY_REQUEST_CALLBACK, DUMMY_MESSAGE_EXTRACTOR); 			
	}


	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
