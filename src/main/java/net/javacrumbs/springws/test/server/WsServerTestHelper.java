package net.javacrumbs.springws.test.server;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.support.DefaultStrategiesHelper;
import org.springframework.ws.transport.WebServiceMessageReceiver;
import org.springframework.ws.transport.http.HttpTransportException;

public class WsServerTestHelper implements ApplicationContextAware, InitializingBean{


	private ApplicationContext applicationContext;
	
    private static final String DEFAULT_STRATEGIES_PATH = "MessageDispatcherServlet.properties";
    
    private SoapMessageFactory messageFactory;
    
    private WebServiceMessageReceiver webServiceMessageReceiver;    

	
	public WebServiceMessageReceiver getWebServiceMessageReceiver() {
		return webServiceMessageReceiver;
	}


	public void setWebServiceMessageReceiver(WebServiceMessageReceiver webServiceMessageReceiver) {
		this.webServiceMessageReceiver = webServiceMessageReceiver;
	}


	public MessageContext receiveMessage(Resource request) throws Exception {
		WebServiceMessage message = messageFactory.createWebServiceMessage(request.getInputStream());
		DefaultMessageContext context = new DefaultMessageContext(message, messageFactory );
		getWebServiceMessageReceiver().receive(context);		
		return context;
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




}
