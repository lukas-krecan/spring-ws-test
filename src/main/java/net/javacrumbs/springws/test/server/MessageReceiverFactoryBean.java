package net.javacrumbs.springws.test.server;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.support.DefaultStrategiesHelper;
import org.springframework.ws.transport.WebServiceMessageReceiver;
import org.springframework.ws.transport.http.HttpTransportException;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

/**
 * Factory bean to be used, if message receiver is not explicitely declared. For
 * internal use only.
 * 
 * @author Lukas Krecan
 * 
 */
class MessageReceiverFactoryBean extends AbstractFactoryBean implements ApplicationContextAware {

	private DefaultStrategiesHelper defaultStrategiesHelper;
	
	private ApplicationContext applicationContext;
	/**
     * Name of the class path resource (relative to the {@link MessageDispatcherServlet} class) that defines
     * <code>MessageDispatcherServlet's</code> default strategy names.
     */
    private static final String DEFAULT_STRATEGIES_PATH = "MessageDispatcherServlet.properties";
	
	public MessageReceiverFactoryBean()
	{
		defaultStrategiesHelper = new DefaultStrategiesHelper(new ClassPathResource(DEFAULT_STRATEGIES_PATH, HttpTransportException.class));
	}
	
	@Override
	protected Object createInstance() throws Exception {
		return (WebServiceMessageReceiver) defaultStrategiesHelper.getDefaultStrategy(WebServiceMessageReceiver.class, applicationContext);
	}

	@Override
	public Class<?> getObjectType() {
		return WebServiceMessageReceiver.class;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
