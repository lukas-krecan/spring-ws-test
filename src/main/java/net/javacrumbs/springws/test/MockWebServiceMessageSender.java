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
