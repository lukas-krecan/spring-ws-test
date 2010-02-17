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
package net.javacrumbs.springws.test.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import javax.xml.transform.stream.StreamResult;

import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.ResourceSource;
import org.w3c.dom.Document;

/**
 * Evaluates XSLT template.
 * @author Lukas Krecan
 *
 */
public class XsltTemplateProcessor implements TemplateProcessor {
	
	private static final String XSL_NAMESPACE = "http://www.w3.org/1999/XSL/Transform";
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	public Resource processTemplate(Resource resource, URI uri, WebServiceMessage message) throws IOException {
		if (resource!=null)
		{
			if (isTemplate(resource))
			{
				return transform(resource, message);
			}
			else
			{
				return resource;
			}
		}
		else
		{
			return null;
		}
	}
	
	protected boolean isTemplate(Resource resource) throws IOException {
		Document document = loadDocument(resource);
		return XSL_NAMESPACE.equals(document.getFirstChild().getNamespaceURI());
	}
	
	protected Document loadDocument(Resource resource) throws IOException {
		return getXmlUtil().loadDocument(resource);
	}

	protected Resource transform(Resource resource, WebServiceMessage message) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		getXmlUtil().transform(new ResourceSource(resource), getXmlUtil().getEnvelopeSource(message), new StreamResult(baos));
		if (logger.isDebugEnabled())
		{
			logger.debug("Transformation result:\n"+new String(baos.toByteArray(),"UTF-8"));
		}
		return new ByteArrayResource(baos.toByteArray());
	}

	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

}
