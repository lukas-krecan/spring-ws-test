package net.krecan.springws.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import net.krecan.springws.test.generator.ResponseGenerator;
import net.krecan.springws.test.util.DefaultXmlUtil;
import net.krecan.springws.test.util.XmlUtil;
import net.krecan.springws.test.validator.RequestValidator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;


/**
 * Mock WS connection that instead of actually calling the WS calls all {@link RequestValidator}s specified andf then uses {@link ResponseGenerator}s
 * to generate the response.
 * @author Lukas Krecan
 *
 */
public class MockWebServiceConnection implements WebServiceConnection {

	private final URI uri;

	private WebServiceMessage request;
	
	private RequestValidator[] requestValidators;
		
	private ResponseGenerator[] responseGenerators;
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public MockWebServiceConnection(URI uri) {
		this.uri = uri;
	}

	/**
	 * Validates and stores the message.
	 */
	public void send(WebServiceMessage message) throws IOException {
		if (logger.isDebugEnabled())
		{
			logger.debug("Processing message \""+xmlUtil.serializeDocument(message)+"\" for URI \""+uri+"\"");
		}
		validate(message);
		request = message;
	}

	/**
	 * Calls all validators.
	 * @param message
	 * @throws IOException
	 */
	protected void validate(WebServiceMessage message) throws IOException {
		if (requestValidators!=null)
		{
			for(RequestValidator requestValidator: requestValidators)
			requestValidator.validate(uri, message);
		}
	}
	/**
	 * Generates mock response
	 */
	public WebServiceMessage receive(WebServiceMessageFactory messageFactory) throws IOException {
		return generateResponse(messageFactory);
	}

	/**
	 * Calls all generators. If a generator returns <code>null</code>, the next generator is called. If all generators return  <code>null</code>
	 * {@link MockWebServiceConnection#handleResponseNotFound} method is called. In default implementation it throws {@link ResponseGeneratorNotSpecifiedException}.
	 * @param messageFactory
	 * @return
	 * @throws IOException
	 */
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

	/**
	 * Throws {@link ResponseGeneratorNotSpecifiedException}. Can be overrriden.
	 * @param messageFactory
	 * @return
	 */
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

	/**
	 * List of request validators to be used.
	 * @param requestValidators
	 */
	public void setRequestValidators(RequestValidator[] requestValidators) {
		this.requestValidators = requestValidators;
	}

	
	public WebServiceMessage getRequest() {
		return request;
	}

	public ResponseGenerator[] getResponseGenerators() {
		return responseGenerators;
	}

	/**
	 * List of request generators to be used.
	 * @param responseGenerators
	 */
	public void setResponseGenerators(ResponseGenerator[] responseGenerators) {
		this.responseGenerators = responseGenerators;
	}

	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}
}
