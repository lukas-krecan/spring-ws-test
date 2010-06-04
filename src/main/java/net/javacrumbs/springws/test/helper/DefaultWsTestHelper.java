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

import static net.javacrumbs.springws.test.helper.DefaultStrategiesHelperFactory.getDefaultStrategiesHelper;

import java.io.IOException;

import net.javacrumbs.springws.test.common.MessageGenerator;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.FaultMessageResolver;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

/**
 * Default {@link WsTestHelper} implementation.
 * @author Lukas Krecan
 *
 */
public class DefaultWsTestHelper implements ApplicationContextAware, InitializingBean, ResourceLoaderAware, WsTestHelper{

	private static final String DEFAULT_MESSAGE_FACTORY_BEAN_NAME = MessageDispatcherServlet.DEFAULT_MESSAGE_FACTORY_BEAN_NAME;

	private ApplicationContext applicationContext;
       
    private WebServiceMessageFactory messageFactory;
    
    private ResourceLoader resourceLoader;

	private TemplateProcessor templateProcessor = new XsltTemplateProcessor();
	
	private final Log logger = LogFactory.getLog(getClass());

	private MessageGenerator generator = new MessageGenerator();
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	private WsTestWebServiceTemplate webServiceTemplate;
	
	private ClientInterceptor[] interceptors;
	
	private static final FaultMessageResolver DUMMY_FAULT_MESSAGE_RESOLVER = new FaultMessageResolver()
	{
		public void resolveFault(WebServiceMessage message) throws IOException {}
	};
	
	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.helper.WsTestHelper#receiveMessage(org.springframework.ws.WebServiceMessage)
	 */
	public MessageContext receiveMessage(WebServiceMessage message) throws Exception {
		MessageContext context = createMessageContext(message);
		webServiceTemplate.send(context);
		if (logger.isTraceEnabled())
		{
			logger.trace("Retreived message: \""+getXmlUtil().serializeDocument(context.getResponse())+"\"");
		}
		return context;
	}
	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.helper.WsTestHelper#receiveMessage(org.springframework.core.io.Resource)
	 */
	//TODO rename
	public MessageContext receiveMessage(Resource request) throws Exception {
		Assert.notNull(request, "Request can not be null.");
		WebServiceMessage message = loadMessage(request);		
		return receiveMessage(message);
	}


	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.helper.WsTestHelper#receiveMessage(java.lang.String)
	 */
	//TODO rename
	public MessageContext receiveMessage(String requestPath) throws Exception {
		return receiveMessage(resourceLoader.getResource(requestPath));
	}
	

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.helper.WsTestHelper#loadMessage(org.springframework.core.io.Resource)
	 */
	public WebServiceMessage loadMessage(Resource resource) throws IOException {
		Resource processedResource = preprocessResource(resource);
		WebServiceMessage message = generator.generateMessage(messageFactory, processedResource);
		return message;
	}


	/**
	 * Does resource preprocessing. In default implementation just evaluates a template.
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	protected Resource preprocessResource(Resource resource) throws IOException {
		return templateProcessor.processTemplate(resource, null);
	}
	
	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.helper.WsTestHelper#loadMessage(java.lang.String)
	 */
	public WebServiceMessage loadMessage(String resourcePath) throws IOException {
		return loadMessage(resourceLoader.getResource(resourcePath));
	}
	
	/**
	 * Creates {@link MessageContext}. Can be overriden.
	 * @param message
	 * @return
	 */
	protected DefaultMessageContext createMessageContext(WebServiceMessage message) {
		return new DefaultMessageContext(message, messageFactory );
	}
	
	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.helper.WsTestHelper#createMessageValidator(org.springframework.ws.WebServiceMessage)
	 */
	public MessageValidator createMessageValidator(WebServiceMessage message)
	{
		MessageValidator messageValidator = new MessageValidator(message);
		messageValidator.setResourceLoader(resourceLoader);
		messageValidator.setTemplateProcessor(templateProcessor);
		return messageValidator;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}


	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.helper.WsTestHelper#getMessageFactory()
	 */
	public WebServiceMessageFactory getMessageFactory() {
		return messageFactory;
	}


	public void setMessageFactory(WebServiceMessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}


	public void afterPropertiesSet() throws Exception {
		initializeMessageFactory();
		initializeWebServiceTemplate();
		initializeResourceLoader();
	}


	protected void initializeResourceLoader() {
		if (resourceLoader==null)
		{
			if (applicationContext!=null)
			{
				resourceLoader = applicationContext;
			}
			else
			{
				resourceLoader = new DefaultResourceLoader();
			}
		}
	}
	
	protected void initializeWebServiceTemplate() {
		if (webServiceTemplate==null)
		{
			webServiceTemplate = new WsTestWebServiceTemplate();
			//we are not interested in client SOAP faults.
			webServiceTemplate.setFaultMessageResolver(DUMMY_FAULT_MESSAGE_RESOLVER);
			webServiceTemplate.setApplicationContext(applicationContext);
			//Replace default message sender.
			webServiceTemplate.setInterceptors(interceptors);
			webServiceTemplate.afterPropertiesSet();
		}
	}
	
	protected void initializeMessageFactory() throws Exception {
		if (messageFactory==null)
		{
			if (applicationContext!=null && applicationContext.containsBean(DEFAULT_MESSAGE_FACTORY_BEAN_NAME))
			{
				messageFactory = (WebServiceMessageFactory)applicationContext.getBean(DEFAULT_MESSAGE_FACTORY_BEAN_NAME, WebServiceMessageFactory.class);
			}
			else
			{
				logger.debug("No WebServiceMessageFactory found, using default");
				messageFactory = (WebServiceMessageFactory) getDefaultStrategiesHelper().getDefaultStrategy(WebServiceMessageFactory.class, applicationContext);
			}
		}
	}


	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}


	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}


	public TemplateProcessor getTemplateProcessor() {
		return templateProcessor;
	}


	public void setTemplateProcessor(TemplateProcessor templateProcessor) {
		this.templateProcessor = templateProcessor;
	}



	public MessageGenerator getGenerator() {
		return generator;
	}



	public void setGenerator(MessageGenerator generator) {
		this.generator = generator;
	}
	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.helper.WsTestHelper#getXmlUtil()
	 */
	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}
	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}
	public ClientInterceptor[] getInterceptors() {
		return interceptors;
	}
	public void setInterceptors(ClientInterceptor[] interceptors) {
		this.interceptors = interceptors;
	}
	public WsTestWebServiceTemplate getWebServiceTemplate() {
		return webServiceTemplate;
	}
	public void setWebServiceTemplate(WsTestWebServiceTemplate webServiceTemplate) {
		this.webServiceTemplate = webServiceTemplate;
	}
}
