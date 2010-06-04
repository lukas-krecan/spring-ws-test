package net.javacrumbs.springws.test.helper;

import static net.javacrumbs.springws.test.helper.DefaultStrategiesHelperFactory.getDefaultStrategiesHelper;

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
import org.springframework.ws.transport.WebServiceMessageReceiver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

/**
 * WebService template to be used when testing WS server.
 * @author Lukas Krecan
 *
 */
public class WsTestWebServiceTemplate extends WebServiceTemplate implements InitializingBean, ApplicationContextAware {
	
	private static final String DEFAULT_MESSAGE_RECEIVER_BEAN_NAME = MessageDispatcherServlet.DEFAULT_MESSAGE_RECEIVER_BEAN_NAME;
	
	private static final WebServiceMessageCallback DUMMY_REQUEST_CALLBACK = new WebServiceMessageCallback()
	{
		public void doWithMessage(WebServiceMessage message) throws IOException ,TransformerException {};
	};
	
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
		setMessageSender(new InMemoryWebServiceMessageSender(getMessageFactory(), createWebServiceMessageReceiver()));
	};
	
	
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


	/**
	 * Sends message directly.
	 * @param context
	 * @throws IOException
	 */
	public void send(MessageContext context) throws IOException {
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