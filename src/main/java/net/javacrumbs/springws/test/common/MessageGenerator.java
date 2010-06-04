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

package net.javacrumbs.springws.test.common;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.TransportInputStreamWrapper;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Generates SOAP message from given resource. Does NOT do template processing, it just loads resource, 
 * wraps it into SOAP envelope if necessary and that's all.
 * @author Lukas Krecan
 *
 */
public class MessageGenerator {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	private boolean alwaysCreateEnvelope = false; 

	private boolean neverCreateEnvelope = false; 
	
	public WebServiceMessage generateMessage(WebServiceMessageFactory messageFactory, Resource resource) throws IOException {
		if (shouldCreateSoapEnvelope(resource))
		{
			WebServiceMessage message = messageFactory.createWebServiceMessage();
			getXmlUtil().transform(new StreamSource(resource.getInputStream()), message.getPayloadResult());
			logMessage(message);
			return message;	
		}
		else
		{
			WebServiceMessage message = messageFactory.createWebServiceMessage(createInputStream(resource));
			logMessage(message);
			return message;
		}
	}
	

	/**
	 * Returns true if the soap envelope should be created.
	 * @param resultResource
	 * @return
	 * @throws IOException 
	 */
	protected boolean shouldCreateSoapEnvelope(Resource resultResource) throws IOException {
		return alwaysCreateEnvelope || (!neverCreateEnvelope && !getXmlUtil().isSoap(xmlUtil.loadDocument(resultResource)));
	}


	private void logMessage(WebServiceMessage message) {
		if (logger.isTraceEnabled())
		{
			logger.trace("Loaded message "+getXmlUtil().serializeDocument(message));
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


	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}


	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}


	public boolean isAlwaysCreateEnvelope() {
		return alwaysCreateEnvelope;
	}

	/**
	 * If true SOAP envelope is always created.
	 * @param alwaysCreateEnvelope
	 */
	public void setAlwaysCreateEnvelope(boolean alwaysCreateEnvelope) {
		this.alwaysCreateEnvelope = alwaysCreateEnvelope;
	}


	public boolean isNeverCreateEnvelope() {
		return neverCreateEnvelope;
	}

	/**
	 * If true SOAP envelope is never created.
	 * @param neverCreateEnvelope
	 */
	public void setNeverCreateEnvelope(boolean neverCreateEnvelope) {
		this.neverCreateEnvelope = neverCreateEnvelope;
	}
}
