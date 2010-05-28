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
package net.javacrumbs.springws.test.generator;

import java.io.IOException;
import java.net.URI;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.common.MessageGenerator;
import net.javacrumbs.springws.test.lookup.ResourceLookup;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Looks-up resource using {@link ResourceLookup} and generates {@link WebServiceMessage} based on the resource. 
 * If the resource does not contain SOAP envelope, an envelope is created. You can influence this behaviour by setting {@link #alwaysCreateEnvelope} and {@link #neverCreateEnvelope}.
 * @author Lukas Krecan
 *
 */
public class DefaultResponseGenerator implements RequestProcessor, Ordered {

	static final int DEFAULT_ORDER = 50;

	protected final Log logger = LogFactory.getLog(getClass());

	private ResourceLookup resourceLookup;
	
	private int order = DEFAULT_ORDER;
			
	private MessageGenerator messageGenerator = new MessageGenerator();

	public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory, WebServiceMessage request) throws IOException {
		Resource resultResource = getResultResource(uri, request);
		if (resultResource == null) {
			logger.debug("Resource not found, returning null.");
			return null;
		} else {
			WebServiceMessage message = generateMessage(messageFactory, resultResource);
			postprocessMessage(message, uri, messageFactory, request);
			return message;
		}
	}


	/**
	 * Generates message from the resource.
	 * @param messageFactory
	 * @param resultResource
	 * @return
	 * @throws IOException
	 */
	protected WebServiceMessage generateMessage(WebServiceMessageFactory messageFactory, Resource resultResource) throws IOException {
		return getMessageGenerator().generateMessage(messageFactory, resultResource);
	}


	/**
	 * Can be overriden to postprocess generated message.
	 * @param message
	 * @param uri
	 * @param messageFactory
	 * @param request
	 */
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public XmlUtil getXmlUtil() {
		return getMessageGenerator().getXmlUtil();
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		getMessageGenerator().setXmlUtil(xmlUtil);
	}

	public boolean isAlwaysCreateEnvelope() {
		return getMessageGenerator().isAlwaysCreateEnvelope();
	}

	/**
	 * If true SOAP envelope is always created.
	 * @param alwaysCreateEnvelope
	 */
	public void setAlwaysCreateEnvelope(boolean alwaysCreateEnvelope) {
		this.getMessageGenerator().setAlwaysCreateEnvelope(alwaysCreateEnvelope);
	}

	public boolean isNeverCreateEnvelope() {
		return getMessageGenerator().isNeverCreateEnvelope();
	}

	/**
	 * If true SOAP envelope is never created.
	 * @param neverCreateEnvelope
	 */
	public void setNeverCreateEnvelope(boolean neverCreateEnvelope) {
		this.getMessageGenerator().setNeverCreateEnvelope(neverCreateEnvelope);
	}


	public MessageGenerator getMessageGenerator() {
		return messageGenerator;
	}


	public void setMessageGenerator(MessageGenerator messageGenerator) {
		this.messageGenerator = messageGenerator;
	}
}
