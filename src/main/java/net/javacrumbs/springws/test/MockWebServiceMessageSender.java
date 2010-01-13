/**
 * Copyright 2006 the original author or authors.
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
package net.javacrumbs.springws.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Creates new {@link MockWebServiceConnection}. 
 * @author Lukas Krecan
 *
 */
public class MockWebServiceMessageSender extends AbstractMockWebServiceMessageSender implements WebServiceMessageSender, InitializingBean, ApplicationContextAware {
	
	private List<RequestProcessor> requestProcessors = new ArrayList<RequestProcessor>();
	
	private boolean autowireRequestProcessors = true;
	
	private ApplicationContext applicationContext;
	
	/**
	 * Response generators used to generate the response.
	 */
	public List<RequestProcessor> getRequestProcessors() {
		return Collections.unmodifiableList(requestProcessors);
	}

	public void setRequestProcessors(List<? extends RequestProcessor> requestProcessors) {
		this.requestProcessors = new ArrayList<RequestProcessor>(requestProcessors);
	}
	
	public void afterPropertiesSet() {
		autowireRequestProcessors();
	}

	@SuppressWarnings("unchecked")
	private void autowireRequestProcessors() {
		if (isAutowireRequestProcessors())
		{
			List<RequestProcessor> processors = new ArrayList<RequestProcessor>(applicationContext.getBeansOfType(RequestProcessor.class).values());
			if (!processors.isEmpty())
			{
				processors.addAll(requestProcessors);
				Collections.sort(processors, new OrderComparator());
				requestProcessors = processors;
			}
		}
	}

	public boolean isAutowireRequestProcessors() {
		return autowireRequestProcessors;
	}

	public void setAutowireRequestProcessors(boolean autowireResponseGenerators) {
		this.autowireRequestProcessors = autowireResponseGenerators;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;		
	}
}
