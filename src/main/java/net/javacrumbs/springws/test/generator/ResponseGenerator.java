/**
 * Copyright 2006 the original author or authors.
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
package net.javacrumbs.springws.test.generator;

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
