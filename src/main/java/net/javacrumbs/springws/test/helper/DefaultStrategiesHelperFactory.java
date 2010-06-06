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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.support.DefaultStrategiesHelper;
import org.springframework.ws.transport.http.HttpTransportException;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

/**
 * Creates Default strategies helper
 * @author Lukas Krecan
 *
 */
class DefaultStrategiesHelperFactory {
	private static final String DEFAULT_STRATEGIES_PATH = "MessageDispatcherServlet.properties";
	
	private static final Log logger = LogFactory.getLog(DefaultStrategiesHelperFactory.class);
	
	private static final String DEFAULT_MESSAGE_FACTORY_BEAN_NAME = MessageDispatcherServlet.DEFAULT_MESSAGE_FACTORY_BEAN_NAME;
	
	public static DefaultStrategiesHelper getDefaultStrategiesHelper() {
		//should be MessageDispatcherServlet.class but it would require servlet-api in the classpath. So we use HttpTransportException instead.
		return new DefaultStrategiesHelper(new ClassPathResource(DEFAULT_STRATEGIES_PATH, HttpTransportException.class));
	}
	
	public static WebServiceMessageFactory createMessageFactory(ApplicationContext applicationContext)
	{
		if (applicationContext!=null && applicationContext.containsBean(DEFAULT_MESSAGE_FACTORY_BEAN_NAME))
		{
			return (WebServiceMessageFactory)applicationContext.getBean(DEFAULT_MESSAGE_FACTORY_BEAN_NAME, WebServiceMessageFactory.class);
		}
		else
		{
			logger.debug("No WebServiceMessageFactory found, using default");
			return (WebServiceMessageFactory) getDefaultStrategiesHelper().getDefaultStrategy(WebServiceMessageFactory.class, applicationContext);
		}
	}
}
