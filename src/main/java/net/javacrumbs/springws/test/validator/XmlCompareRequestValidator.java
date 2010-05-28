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
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.common.DefaultMessageComparator;
import net.javacrumbs.springws.test.common.MessageComparator;
import net.javacrumbs.springws.test.lookup.ResourceLookup;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Compares message with the control message.
 * 
 * @author Lukas Krecan
 * 
 */
public class XmlCompareRequestValidator implements Ordered, RequestProcessor
{
	
	static final int DEFAULT_ORDER = 10;
	
	private int order = DEFAULT_ORDER;
	
	private ResourceLookup controlResourceLookup;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	private boolean failIfControlResourceNotFound;
	
	private MessageComparator messageComparator = new DefaultMessageComparator();

	public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory,	WebServiceMessage request) throws IOException {
		validateRequest(uri, request);
		return null;
	}
	
	/**
	 * Loads controleResource and compares it with the message. If controlResource is not SOAP message, only payload is compared.
	 * @param uri
	 * @param message
	 * @throws IOException
	 */
	protected void validateRequest(URI uri, WebServiceMessage message) throws IOException {
	
		Resource controlResource = controlResourceLookup.lookupResource(uri, message);
		if (controlResource!=null)
		{
			compareMessage(message, controlResource);
		}
		else
		{
			onControlResourceNotFound(uri, message);
		}
	}

	/**
	 * Compares message with control resource.
	 * @param message
	 * @param controlResource
	 * @throws IOException
	 */
	protected void compareMessage(WebServiceMessage message, Resource controlResource) throws IOException {
		messageComparator.compareMessage(message, controlResource);
	}


	/**
	 * Called if control resource was not found.
	 * @param uri
	 * @param message
	 */
	protected void onControlResourceNotFound(URI uri, WebServiceMessage message) {
		if (failIfControlResourceNotFound)
		{
			throw new WsTestException("Control resource not found for "+uri+" and "+getXmlUtil().serializeDocument(message));
		}
		else
		{
			logger.warn("Can not find resource to validate with.");
		}
	}


	public void afterPropertiesSet(){
		Assert.notNull(controlResourceLookup, "ControlResourceLookup has to be set");		
	}
	

	public ResourceLookup getControlResourceLookup() {
		return controlResourceLookup;
	}

	public void setControlResourceLookup(ResourceLookup controlResourceLookup) {
		this.controlResourceLookup = controlResourceLookup;
	}

	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
		messageComparator.setXmlUtil(xmlUtil);
	}

	public boolean isFailIfControlResourceNotFound() {
		return failIfControlResourceNotFound;
	}

	public void setFailIfControlResourceNotFound(boolean failIfControlResourceNotFound) {
		this.failIfControlResourceNotFound = failIfControlResourceNotFound;
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public void setIgnoreWhitespace(boolean ignoreWhitespace) {
		messageComparator.setIgnoreWhitespace(ignoreWhitespace);
	}
	

	public boolean isIgnoreWhitespace() {
		return messageComparator.isIgnoreWhitespace();
	}
	
	public MessageComparator getMessageComparator() {
		return messageComparator;
	}

	public void setMessageComparator(DefaultMessageComparator messageComparator) {
		this.messageComparator = messageComparator;
	}


}
