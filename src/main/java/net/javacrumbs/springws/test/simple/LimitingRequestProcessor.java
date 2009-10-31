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
package net.javacrumbs.springws.test.simple;

import java.net.URI;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

public interface LimitingRequestProcessor extends RequestProcessor{

	/**
	 * Request processor wrapper that checks number of {@link #processRequest(URI, WebServiceMessageFactory, WebServiceMessage)} calls.
     * If number of calls is higher then {@link #maxNumberOfProcessedRequests} null is returned.
     * If number of calls is lower then {@link #minNumberOfProcessedRequests} and verify is called, {@link WsTestException} is thrown.  
	 */
	public abstract void verify() throws WsTestException;

	public abstract int getMinNumberOfProcessedRequests();

	public abstract void setMinNumberOfProcessedRequests(int minNumberOfProcessedRequests);

	public abstract int getMaxNumberOfProcessedRequests();

	public abstract void setMaxNumberOfProcessedRequests(int maxNumberOfProcessedRequests);

}