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
