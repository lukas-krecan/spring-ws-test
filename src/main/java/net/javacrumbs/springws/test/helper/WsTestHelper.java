package net.javacrumbs.springws.test.helper;

import java.io.IOException;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.SimpleResourceLookup;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.support.DefaultStrategiesHelper;
import org.springframework.ws.transport.WebServiceMessageReceiver;
import org.springframework.ws.transport.http.HttpTransportException;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

/**
 * Helper class to help with server (Endpoint tests). It can be used to simulate WS requests and to compare responses.
 * @author Lukas Krecan
 *
 */
public class WsTestHelper implements ApplicationContextAware, InitializingBean, ResourceLoaderAware{

	private static final String DEFAULT_MESSAGE_FACTORY_BEAN_NAME = MessageDispatcherServlet.DEFAULT_MESSAGE_FACTORY_BEAN_NAME;

	private static final String DEFAULT_MESSAGE_RECEIVER_BEAN_NAME = MessageDispatcherServlet.DEFAULT_MESSAGE_RECEIVER_BEAN_NAME;

	public static final String DEFAULT_CONFIG_PATH = "classpath:net.javacrumbs.springws.test.helper/default-helper-config.xml";
	
	private ApplicationContext applicationContext;
	
    private static final String DEFAULT_STRATEGIES_PATH = "MessageDispatcherServlet.properties";
       
    
    private WebServiceMessageFactory messageFactory;
    
    private WebServiceMessageReceiver webServiceMessageReceiver;   
    
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

	private TemplateProcessor templateProcessor = new XsltTemplateProcessor();
	
	private final Log logger = LogFactory.getLog(getClass());

	
	/**
	 * Creates a {@link MessageContext} from the resource and calls  {@link WebServiceMessageReceiver#receive(MessageContext)}
	 * @param request
	 * @return
	 * @throws Exception
	 */
	//TODO rename
	public MessageContext receiveMessage(Resource request) throws Exception {

		WebServiceMessage message = loadMessage(request);		
		
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
	//TODO rename
	public MessageContext receiveMessage(String requestPath) throws Exception {
		return receiveMessage(resourceLoader.getResource(requestPath));
	}
	

	/**
	 * Loads message from given resource. Does template processing.
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public WebServiceMessage loadMessage(Resource resource) throws IOException {
		DefaultResponseGenerator generator = new DefaultResponseGenerator();
		SimpleResourceLookup resourceLookup = new SimpleResourceLookup(resource);
		resourceLookup.setTemplateProcessor(templateProcessor);
		generator.setResourceLookup(resourceLookup);
	
		WebServiceMessage message = generator.processRequest(null, messageFactory, messageFactory.createWebServiceMessage());
		return message;
	}
	
	/**
	 * Loads message from given resource. Does template processing.
	 * @param resource
	 * @return
	 * @throws IOException
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

	/**
	 * Validates if the message corresponds to given XSD.
	 * @param message
	 */
	public void validateMessage(WebServiceMessage message, Resource schema, Resource... schemas) throws IOException{
		SchemaRequestValidator validator = new SchemaRequestValidator();
		Resource[] joinedSchemas = new Resource[schemas.length+1];
		joinedSchemas[0] = schema;
		System.arraycopy(schemas, 0, joinedSchemas, 1, schemas.length);
		validator.setSchemas(joinedSchemas);
		validator.afterPropertiesSet();
		
		validator.processRequest(null, messageFactory, message);
	}
	
	/**
	 * Validates if the message corresponds to given XSDs.
	 * @param message
	 */
	public void validateMessage(WebServiceMessage message, String schemaPath, String... schemaPaths) throws IOException{
		Resource[] schemas = new Resource[schemaPaths.length];
		for (int i = 0; i < schemas.length; i++) {
			schemas[i] = resourceLoader.getResource(schemaPaths[i]);
		}
		validateMessage(message, resourceLoader.getResource(schemaPath), schemas);
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


	public WebServiceMessageFactory getMessageFactory() {
		return messageFactory;
	}


	public void setMessageFactory(WebServiceMessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}


	public void afterPropertiesSet() throws Exception {
		initializeWebServiceMessageReceiver();
		initializeMessageFactory();
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


	protected void initializeWebServiceMessageReceiver() {
		if (webServiceMessageReceiver==null)
		{
			if (applicationContext!=null && applicationContext.containsBean(DEFAULT_MESSAGE_RECEIVER_BEAN_NAME))
			{
				webServiceMessageReceiver = (WebServiceMessageReceiver) applicationContext.getBean(DEFAULT_MESSAGE_RECEIVER_BEAN_NAME, WebServiceMessageReceiver.class);
			}
			else
			{
				logger.debug("No WebServiceMessageReceiver found, using default");
				webServiceMessageReceiver = (WebServiceMessageReceiver) getDefaultStrategiesHelper().getDefaultStrategy(WebServiceMessageReceiver.class, applicationContext);		
			}
		}
	}


	private DefaultStrategiesHelper getDefaultStrategiesHelper() {
		//should be MessageDispatcherServlet.class but it would require servlet-api in the classpath. So we use HttpTransportException instead.
		return new DefaultStrategiesHelper(new ClassPathResource(DEFAULT_STRATEGIES_PATH, HttpTransportException.class));
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
