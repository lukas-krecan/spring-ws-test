package net.javacrumbs.springws.test.validator;

import java.io.IOException;
import java.net.URI;

import net.javacrumbs.springws.test.generator.ResponseGenerator;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Wraps {@link RequestValidator} and exposes it as {@link ResponseGenerator}. To be used for backward compatibility with version 0.6.
 * @author Lukas Krecan
 *
 */
@SuppressWarnings("deprecation")
public class RequestValidatorResponseGeneratorWrapper implements ResponseGenerator {
	private final RequestValidator wrappedValidator;
	
	public RequestValidatorResponseGeneratorWrapper(RequestValidator wrappedValidator) {
		this.wrappedValidator = wrappedValidator;
	}

	public WebServiceMessage generateResponse(URI uri, WebServiceMessageFactory messageFactory,
			WebServiceMessage request) throws IOException {
		wrappedValidator.validateRequest(uri, request);
		return null;
	}

}
