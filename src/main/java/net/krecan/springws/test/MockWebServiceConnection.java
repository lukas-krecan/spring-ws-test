package net.krecan.springws.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import net.krecan.springws.test.generator.ResponseGenerator;
import net.krecan.springws.test.util.XmlUtil;
import net.krecan.springws.test.validator.RequestValidator;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;

public class MockWebServiceConnection implements WebServiceConnection {

	private final URI uri;

	private WebServiceMessage request;
	
	private RequestValidator[] requestValidators;
		
	private ResponseGenerator[] responseGenerators;
	
	public MockWebServiceConnection(URI uri) {
		this.uri = uri;
	}

	public void send(WebServiceMessage message) throws IOException {
		validate(message);
		request = message;
	}

	protected void validate(WebServiceMessage message) throws IOException {
		if (requestValidators!=null)
		{
			for(RequestValidator requestValidator: requestValidators)
			requestValidator.validate(uri, message);
		}
	}

	public WebServiceMessage receive(WebServiceMessageFactory messageFactory) throws IOException {
		return generateResponse(messageFactory);
	}

	protected WebServiceMessage generateResponse(WebServiceMessageFactory messageFactory) throws IOException {
		WebServiceMessage response = null;
		if (responseGenerators!=null)
		{
			for (ResponseGenerator responseGenerator: responseGenerators)
			{
				response = responseGenerator.generateResponse(uri, messageFactory, request);
				if (response!=null)
				{
					return response;
				}
			}
		}
		return handleResponseNotFound(messageFactory);
	}

	protected WebServiceMessage handleResponseNotFound(WebServiceMessageFactory messageFactory) {
		throw new ResponseGeneratorNotSpecifiedException("No response generator configured for uri "+uri+".");
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

	public RequestValidator[] getRequestValidators() {
		return requestValidators;
	}

	public void setRequestValidators(RequestValidator[] requestValidators) {
		this.requestValidators = requestValidators;
	}

	
	public WebServiceMessage getRequest() {
		return request;
	}

	public ResponseGenerator[] getResponseGenerators() {
		return responseGenerators;
	}

	public void setResponseGenerators(ResponseGenerator[] responseGenerators) {
		this.responseGenerators = responseGenerators;
	}
}
