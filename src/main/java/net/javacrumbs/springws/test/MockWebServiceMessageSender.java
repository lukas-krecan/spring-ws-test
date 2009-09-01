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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.javacrumbs.springws.test.generator.ResponseGenerator;
import net.javacrumbs.springws.test.validator.RequestValidator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Creates new {@link MockWebServiceConnection}. 
 * @author Lukas Krecan
 *
 */
public class MockWebServiceMessageSender implements WebServiceMessageSender, InitializingBean, ApplicationContextAware {
	
	private Collection<RequestValidator> requestValidators = new ArrayList<RequestValidator>();
		
	private Collection<ResponseGenerator> responseGenerators = new ArrayList<ResponseGenerator>();
	
	private boolean autowireRequestValidators = true;

	private boolean autowireResponseGenerators = true;
	
	private ApplicationContext applicationContext;
	
	public WebServiceConnection createConnection(URI uri) throws IOException {
		MockWebServiceConnection connection = new MockWebServiceConnection(uri);
		connection.setRequestValidators(requestValidators);
		connection.setResponseGenerators(responseGenerators);
		return connection;
	}

	public boolean supports(URI uri) {
		return true;
	}

	public Collection<RequestValidator> getRequestValidators() {
		return requestValidators;
	}
	
	
	public void setRequestValidator(RequestValidator requestValidator) {
		setRequestValidators(Collections.singleton(requestValidator));
	}

	/**
	 * Request validators to be called for every request.
	 * @param requestValidator
	 */
	public void setRequestValidators(Collection<RequestValidator> requestValidators) {
		this.requestValidators = requestValidators;
	}

	/**
	 * Response generators used to generate the response.
	 */
	public Collection<ResponseGenerator> getResponseGenerators() {
		return responseGenerators;
	}

	public void setResponseGenerators(Collection<ResponseGenerator> responseGenerators) {
		this.responseGenerators = responseGenerators;
	}
	
	public void setResponseGenerator(ResponseGenerator responseGenerator) {
		setResponseGenerators(Collections.singleton(responseGenerator));
	}

	public void afterPropertiesSet() throws Exception {
		autowireResponseGenerators();
		autowireRequestValidators();
		
	}

	@SuppressWarnings("unchecked")
	private void autowireResponseGenerators() {
		if (isAutowireResponseGenerators())
		{
			responseGenerators.addAll(applicationContext.getBeansOfType(ResponseGenerator.class).values());
		}
	}
	@SuppressWarnings("unchecked")
	private void autowireRequestValidators() {
		if (isAutowireRequestValidators())
		{
			requestValidators.addAll(applicationContext.getBeansOfType(RequestValidator.class).values());
		}
	}

	public boolean isAutowireResponseGenerators() {
		return autowireResponseGenerators;
	}

	public void setAutowireResponseGenerators(boolean autowireResponseGenerators) {
		this.autowireResponseGenerators = autowireResponseGenerators;
	}

	public boolean isAutowireRequestValidators() {
		return autowireRequestValidators;
	}

	public void setAutowireRequestValidators(boolean autowireRequestValidators) {
		this.autowireRequestValidators = autowireRequestValidators;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;		
	}
}
