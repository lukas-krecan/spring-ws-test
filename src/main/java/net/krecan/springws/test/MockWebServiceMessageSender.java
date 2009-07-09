package net.krecan.springws.test;

import java.io.IOException;
import java.net.URI;

import net.krecan.springws.test.generator.ResponseGenerator;
import net.krecan.springws.test.validator.RequestValidator;

import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Creates new {@link MockWebServiceConnection}. 
 * @author Lukas Krecan
 *
 */
public class MockWebServiceMessageSender implements WebServiceMessageSender {
	
	private RequestValidator[] requestValidators;
		
	private ResponseGenerator[] responseGenerators;
	
	public WebServiceConnection createConnection(URI uri) throws IOException {
		MockWebServiceConnection connection = new MockWebServiceConnection(uri);
		connection.setRequestValidators(requestValidators);
		connection.setResponseGenerators(responseGenerators);
		return connection;
	}

	public boolean supports(URI uri) {
		return true;
	}

	public RequestValidator[] getRequestValidators() {
		return requestValidators;
	}
	
	public void setRequestValidator(RequestValidator requestValidator) {
		setRequestValidators(new RequestValidator[]{requestValidator});
	}

	public void setRequestValidators(RequestValidator[] requestValidators) {
		this.requestValidators = requestValidators;
	}

	public ResponseGenerator[] getResponseGenerators() {
		return responseGenerators;
	}

	public void setResponseGenerators(ResponseGenerator[] responseGenerators) {
		this.responseGenerators = responseGenerators;
	}
	
	public void setResponseGenerator(ResponseGenerator responseGenerator) {
		setResponseGenerators(new ResponseGenerator[]{responseGenerator});
	}
	
	

}
