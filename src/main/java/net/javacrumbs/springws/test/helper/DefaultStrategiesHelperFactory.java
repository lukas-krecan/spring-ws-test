package net.javacrumbs.springws.test.helper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.support.DefaultStrategiesHelper;
import org.springframework.ws.transport.http.HttpTransportException;

/**
 * Creates Default strategies helper
 * @author Lukas Krecan
 *
 */
class DefaultStrategiesHelperFactory {
	private static final String DEFAULT_STRATEGIES_PATH = "MessageDispatcherServlet.properties";
	
	public static DefaultStrategiesHelper getDefaultStrategiesHelper() {
		//should be MessageDispatcherServlet.class but it would require servlet-api in the classpath. So we use HttpTransportException instead.
		return new DefaultStrategiesHelper(new ClassPathResource(DEFAULT_STRATEGIES_PATH, HttpTransportException.class));
	}
}
