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

import static net.javacrumbs.springws.test.helper.DefaultStrategiesHelperFactory.createMessageFactory;
import static net.javacrumbs.springws.test.helper.DefaultStrategiesHelperFactory.getDefaultStrategiesHelper;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceMessageReceiver;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

/**
 * Sends messages locally in memory.  Cretes {@link InMemoryWebServiceConnection} that sends the message directly to {@link WebServiceMessageReceiver}.  Used for server side test.
 * @author Lukas Krecan
 *
 */
public class InMemoryWebServiceMessageSender implements WebServiceMessageSender, ApplicationContextAware, InitializingBean {

	private WebServiceMessageFactory messageFactory;
	
	private WebServiceMessageReceiver webServiceMessageReceiver;
	
	private ApplicationContext applicationContext;
	
	private static final String DEFAULT_MESSAGE_RECEIVER_BEAN_NAME = MessageDispatcherServlet.DEFAULT_MESSAGE_RECEIVER_BEAN_NAME;
	
	private final Log logger = LogFactory.getLog(getClass());

	
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
	
	/**
	 * Creates {@link WebServiceMessageReceiver}. If it's not in the applicationContext, uses {@link DefaultWsTestHelper} to create it.
	 * @return
	 */
	protected WebServiceMessageReceiver createWebServiceMessageReceiver() {
		if (applicationContext!=null && applicationContext.containsBean(DEFAULT_MESSAGE_RECEIVER_BEAN_NAME))
		{
			return (WebServiceMessageReceiver) applicationContext.getBean(DEFAULT_MESSAGE_RECEIVER_BEAN_NAME, WebServiceMessageReceiver.class);
		}
		else
		{
			logger.debug("No WebServiceMessageReceiver found, using default");
			return (WebServiceMessageReceiver) getDefaultStrategiesHelper().getDefaultStrategy(WebServiceMessageReceiver.class, applicationContext);		
		}
	}
	
	public void afterPropertiesSet(){
		initializeMessageReceiver();
		initializeMessageFactory();
	}

	protected void initializeMessageReceiver() {
		if (webServiceMessageReceiver==null)
		{
			webServiceMessageReceiver = createWebServiceMessageReceiver();
		}
	}

	protected void initializeMessageFactory() {
		if (messageFactory==null)
		{
			messageFactory = createMessageFactory(applicationContext);
		}
	}

	void setWebServiceMessageReceiver(WebServiceMessageReceiver webServiceMessageReceiver) {
		this.webServiceMessageReceiver = webServiceMessageReceiver;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	void setMessageFactory(WebServiceMessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}



}
