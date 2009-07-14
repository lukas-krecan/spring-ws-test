package net.krecan.springws.test.generator;

import java.io.IOException;
import java.net.URI;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Generates mock response.
 * @author Lukas Krecan
 *
 */
public interface ResponseGenerator {
	/**
	 * Generatest response corresponding to given uri and request. Can return <code>null</code> if it does not apply to given request.
	 * @param uri
	 * @param messageFactory
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public WebServiceMessage generateResponse(URI uri, WebServiceMessageFactory messageFactory, WebServiceMessage request) throws IOException;
}
