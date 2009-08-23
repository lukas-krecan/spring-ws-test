package net.krecan.springws.test;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

import net.krecan.springws.test.generator.ResponseGenerator;
import net.krecan.springws.test.validator.RequestValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Creates new {@link MockWebServiceConnection}. 
 * @author Lukas Krecan
 *
 */
public class MockWebServiceMessageSender implements WebServiceMessageSender {
	
	private Collection<RequestValidator> requestValidators;
		
	private Collection<ResponseGenerator> responseGenerators;
	
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
	@Autowired(required=false)
	public void setRequestValidators(Collection<RequestValidator> requestValidators) {
		this.requestValidators = requestValidators;
	}

	/**
	 * Response generators used to generate the response.
	 */
	public Collection<ResponseGenerator> getResponseGenerators() {
		return responseGenerators;
	}

	@Autowired(required=false)
	public void setResponseGenerators(Collection<ResponseGenerator> responseGenerators) {
		this.responseGenerators = responseGenerators;
	}
	
	public void setResponseGenerator(ResponseGenerator responseGenerator) {
		setResponseGenerators(Collections.singleton(responseGenerator));
	}
}
