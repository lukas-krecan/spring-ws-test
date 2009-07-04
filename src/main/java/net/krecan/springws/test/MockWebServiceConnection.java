package net.krecan.springws.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;

public class MockWebServiceConnection implements WebServiceConnection {

	private final URI uri;
	
	private RequestValidator requestValidator;
	
	private WebServiceMessage request;
	
	public MockWebServiceConnection(URI uri) {
		this.uri = uri;
	}

	public void send(WebServiceMessage message) throws IOException {
		if (requestValidator!=null)
		{
			requestValidator.validate(uri, message);
		}
		request = message;
	}

	public WebServiceMessage receive(WebServiceMessageFactory messageFactory)
	throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	

	public void close() throws IOException {
		
	}

	public String getErrorMessage() throws IOException {
		return null;
	}

	public URI getUri() throws URISyntaxException {
		return uri;
	}

	public boolean hasError() throws IOException {
		return false;
	}

	public RequestValidator getRequestValidator() {
		return requestValidator;
	}

	public void setRequestValidator(RequestValidator requestValidator) {
		this.requestValidator = requestValidator;
	}

	public WebServiceMessage getRequest() {
		return request;
	}


}
