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
import java.io.InputStream;
import java.net.URI;

import net.javacrumbs.springws.test.lookup.ResourceLookup;
import net.javacrumbs.springws.test.util.TransportInputStreamWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Looks-up resource using {@link ResourceLookup} and generates {@link WebServiceMessage} based on the resource. 
 * @author Lukas Krecan
 *
 */
public class DefaultResponseGenerator implements ResponseGenerator {

	protected final Log logger = LogFactory.getLog(getClass());

	private ResourceLookup resourceLookup;

	public WebServiceMessage generateResponse(URI uri, WebServiceMessageFactory messageFactory,	WebServiceMessage request) throws IOException {
		Resource resultResource = getResultResource(uri, request);
		if (resultResource == null) {
			logger.debug("Resource not found, returning null.");
			return null;
		} else {
			logger.debug("Creating response from "+resultResource);
			WebServiceMessage message = messageFactory.createWebServiceMessage(createInputStream(resultResource));
			postprocessMessage(message, uri, messageFactory, request);
			return message;
		}
	}

	/**
	 * Creates input stream from the resource.
	 * @param resultResource
	 * @return
	 * @throws IOException
	 */
	protected InputStream createInputStream(final Resource resultResource) throws IOException {
		return new TransportInputStreamWrapper(resultResource);
		
	}

	protected void postprocessMessage(WebServiceMessage message, URI uri, WebServiceMessageFactory messageFactory,	WebServiceMessage request) {
		
	}


	/**
	 * Looks for the resource that should be used for result generation.
	 * @param uri
	 * @param request
	 * @return
	 * @throws IOException
	 */
	protected Resource getResultResource(URI uri, WebServiceMessage request) throws IOException {
		return resourceLookup.lookupResource(uri, request);
	}

	public ResourceLookup getResourceLookup() {
		return resourceLookup;
	}

	public void setResourceLookup(ResourceLookup resourceLookup) {
		this.resourceLookup = resourceLookup;
	}
}
