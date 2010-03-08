/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.springws.test.validator;

import java.io.IOException;
import java.net.URI;

import net.javacrumbs.springws.test.RequestProcessor;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Wraps {@link RequestValidator} and exposes it as {@link RequestProcessor}. To be used for backward compatibility with version 0.6.
 * @author Lukas Krecan
 *
 */
@Deprecated
public class RequestValidatorResponseGeneratorWrapper implements RequestProcessor {
	private final RequestValidator wrappedValidator;
	
	public RequestValidatorResponseGeneratorWrapper(RequestValidator wrappedValidator) {
		this.wrappedValidator = wrappedValidator;
	}

	public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory,
			WebServiceMessage request) throws IOException {
		wrappedValidator.validateRequest(uri, request);
		return null;
	}

}
