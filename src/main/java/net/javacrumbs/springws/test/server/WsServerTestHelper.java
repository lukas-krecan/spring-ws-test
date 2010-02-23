package net.javacrumbs.springws.test.server;

import java.io.IOException;
import java.util.Map;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.lookup.SimpleResourceLookup;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.support.DefaultStrategiesHelper;
import org.springframework.ws.transport.WebServiceMessageReceiver;
import org.springframework.ws.transport.http.HttpTransportException;

/**
 * Helper class to help you with server (Endpoint tests). You can throw a message to your server configuration 
 * and compare the response received.
 * @author Lukas Krecan
 *
 */
public class WsServerTestHelper implements ApplicationContextAware, InitializingBean, ResourceLoaderAware{

	public static final String DEFAULT_CONFIG_PATH = "classpath:net.javacrumbs.springws.test.server/default-helper-config.xml";
	
	private ApplicationContext applicationContext;
	
    private static final String DEFAULT_STRATEGIES_PATH = "MessageDispatcherServlet.properties";
       
    
    private SoapMessageFactory messageFactory;
    
    private WebServiceMessageReceiver webServiceMessageReceiver;   
    
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

	private TemplateProcessor templateProcessor = new XsltTemplateProcessor();

	
	/**
	 * Creates a {@link MessageContext} from the resource and calls  {@link WebServiceMessageReceiver#receive(MessageContext)}
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public MessageContext receiveMessage(Resource request) throws Exception {
		WebServiceMessage message = messageFactory.createWebServiceMessage(request.getInputStream());
		MessageContext context = createMessageContext(message);
		getWebServiceMessageReceiver().receive(context);		
		return context;
	}


	/**
	 * Creates a {@link MessageContext} from the resource on the requestPath and calls  {@link WebServiceMessageReceiver#receive(MessageContext)}
	 * @param requestPath
	 * @return
	 * @throws Exception
	 */
	public MessageContext receiveMessage(String requestPath) throws Exception {
		return receiveMessage(resourceLoader.getResource(requestPath));
	}
	
	/**
	 * Creates {@link MessageContext}. Can be overriden.
	 * @param message
	 * @return
	 */
	protected DefaultMessageContext createMessageContext(WebServiceMessage message) {
		return new DefaultMessageContext(message, messageFactory );
	}
	
	/**
	 * Compares message with the resource. 
	 * @param resource
	 * @param message
	 * @throws IOException
	 */
	public void compareMessage(Resource resource, WebServiceMessage message) throws IOException {
		RequestProcessor requestValidator = createRequestComparator(resource);
		requestValidator.processRequest(null, messageFactory, message);
	}


	/**
	 * Compares message with the resource.
	 * @param resourcePath
	 * @param message
	 * @throws IOException
	 */
	public void compareMessage(String resourcePath, WebServiceMessage message) throws IOException {
		compareMessage(resourceLoader.getResource(resourcePath), message);		
	}

	/**
	 * Creates {@link RequestProcessor} that will compare the response with the controlResource 
	 * @param controlResource
	 * @return
	 */
	protected RequestProcessor createRequestComparator(Resource controlResource) {
		XmlCompareRequestValidator requestValidator = new XmlCompareRequestValidator();
		SimpleResourceLookup controlResourceLookup = new SimpleResourceLookup(controlResource);
		controlResourceLookup.setTemplateProcessor(templateProcessor);
		requestValidator.setControlResourceLookup(controlResourceLookup);
		return requestValidator;
	}
	
	public WebServiceMessageReceiver getWebServiceMessageReceiver() {
		return webServiceMessageReceiver;
	}


	public void setWebServiceMessageReceiver(WebServiceMessageReceiver webServiceMessageReceiver) {
		this.webServiceMessageReceiver = webServiceMessageReceiver;
	}


	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}


	public SoapMessageFactory getMessageFactory() {
		return messageFactory;
	}


	public void setMessageFactory(SoapMessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}


	public void afterPropertiesSet() throws Exception {
		initializeWebServiceMessageReceiver();
		initializeMessageFactory();
	}


	protected void initializeMessageFactory() throws Exception {
		if (messageFactory==null)
		{
			SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
			messageFactory.afterPropertiesSet();
			this.messageFactory = messageFactory;
		}
	}


	@SuppressWarnings("unchecked")
	protected void initializeWebServiceMessageReceiver() {
		if (webServiceMessageReceiver==null)
		{
			//should be MessageDispatcherServlet.class but it would require servlet-api in the classpath. So we use HttpTransportException instead.
			DefaultStrategiesHelper defaultStrategiesHelper = new DefaultStrategiesHelper(new ClassPathResource(DEFAULT_STRATEGIES_PATH, HttpTransportException.class));
			if (applicationContext!=null)
			{
				Map messageReceivers = applicationContext.getBeansOfType(WebServiceMessageReceiver.class);
				if (messageReceivers.size()==1)
				{
					webServiceMessageReceiver = (WebServiceMessageReceiver) messageReceivers.values().iterator().next();
					return;
				}
				if (messageReceivers.size()>1)
				{
					throw new NoSuchBeanDefinitionException("expected single matching bean but found " + messageReceivers.size() + ": " + messageReceivers.keySet());
				}
			}
			webServiceMessageReceiver = (WebServiceMessageReceiver) defaultStrategiesHelper.getDefaultStrategy(WebServiceMessageReceiver.class, applicationContext);
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







}
