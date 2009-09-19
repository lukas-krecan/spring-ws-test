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

import net.javacrumbs.springws.test.generator.ResponseGenerator;
import net.javacrumbs.springws.test.lookup.ResourceLookup;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.w3c.dom.Document;

public abstract class AbstractCompareRequestValidator implements InitializingBean, ResponseGenerator{

	private ResourceLookup controlResourceLookup;
	protected final Log logger = LogFactory.getLog(getClass());
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();

	public AbstractCompareRequestValidator() {
		super();
	}

	public WebServiceMessage generateResponse(URI uri, WebServiceMessageFactory messageFactory,
			WebServiceMessage request) throws IOException {
		validateRequest(uri, request);
		return null;
	}
	
	public void validateRequest(URI uri, WebServiceMessage message) throws IOException {
		Document messageDocument = loadDocument(message);
	
		Resource controlResource = controlResourceLookup.lookupResource(uri, message);
		if (controlResource!=null)
		{
			Document controlDocument = loadDocument(controlResource);
	
			compareDocuments(controlDocument, messageDocument);
		}
		else
		{
			logger.warn("Can not find resource to validate with.");
		}
	}

	/**
	 * Does the actual comparison.
	 * @param controlDocument
	 * @param messageDocument
	 */
	protected abstract void compareDocuments(Document controlDocument, Document messageDocument);
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(controlResourceLookup, "ControlResourceLookup has to be set");		
	}

	protected Document loadDocument(WebServiceMessage message) throws IOException {
		return getXmlUtil().loadDocument(message);
	}

	protected Document loadDocument(Resource resource) throws IOException {
		return getXmlUtil().loadDocument(resource);
	}
	
	protected String serializeDocument(Document document) {
		return getXmlUtil().serializeDocument(document);
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
	}

}