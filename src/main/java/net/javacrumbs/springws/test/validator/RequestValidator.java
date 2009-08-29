package net.javacrumbs.springws.test.validator;

import java.io.IOException;
import java.net.URI;

import net.javacrumbs.springws.test.WsTestException;

import org.springframework.ws.WebServiceMessage;

/**
 * Validates request.
 * @author Lukas Krecan
 *
 */
public interface RequestValidator {

	/**
	 * Validates the message. If it's not valid throws {@link WsTestException}. If the validator is not applicable, returns null.
	 * @param uri
	 * @param message
	 * @throws Exception 
	 */
	public void validateRequest(URI uri, WebServiceMessage message) throws IOException, WsTestException;
}
