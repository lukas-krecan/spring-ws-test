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
package net.javacrumbs.springws.test;

import java.io.IOException;

import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.TransportInputStreamWrapper;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

public abstract class AbstractMessageTest {
	
	protected SoapMessageFactory messageFactory;

	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	public AbstractMessageTest() {
		XMLUnit.setIgnoreWhitespace(true);
		initializeMessageFactory();
	}

	protected void initializeMessageFactory() {
		try {
			SaajSoapMessageFactory newMessageFactory = new SaajSoapMessageFactory();
			newMessageFactory.afterPropertiesSet();
			messageFactory = newMessageFactory;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void initializeAxiomMessageFactory() {
		try {
			AxiomSoapMessageFactory newMessageFactory = new AxiomSoapMessageFactory();
			newMessageFactory.setPayloadCaching(false);
			newMessageFactory.afterPropertiesSet();
			messageFactory = newMessageFactory;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected WebServiceMessage createMessage(String path) throws IOException
	{
		return messageFactory.createWebServiceMessage(new TransportInputStreamWrapper(new ClassPathResource(path)));
	}
	protected XmlUtil getXmlUtil() {
		return xmlUtil;
	}
	protected void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}
}
