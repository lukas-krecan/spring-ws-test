package net.krecan.springws.test.validator;

import java.io.IOException;
import java.net.URI;

import net.krecan.springws.test.WsTestException;

import org.springframework.ws.WebServiceMessage;

/**
 * Validates request.
 * @author Lukas Krecan
 *
 */
public interface RequestValidator {

	/**
	 * Validates the message. If it's not valid throws {@link WsTestException}.
	 * @param uri
	 * @param message
	 * @throws Exception 
	 */
	public void validate(URI uri, WebServiceMessage message) throws IOException, WsTestException;
}
