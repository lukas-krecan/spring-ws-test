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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import net.javacrumbs.springws.test.lookup.ResourceLookup;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

public class AbstractCompareRequestValidatorTest extends AbstractValidatorTest{

	
		
	@Test
	public void testValidateRequest() throws Exception
	{
		WebServiceMessage message = getValidMessage();
		final Document messageDoc = createMock(Document.class);
		final Document controlDoc = createMock(Document.class);
		ByteArrayResource controlResource = new ByteArrayResource(new byte[0]);

		XmlUtil xmlUtil = createMock(XmlUtil.class);
		expect(xmlUtil.loadDocument(message)).andReturn(messageDoc);
		expect(xmlUtil.loadDocument(controlResource)).andReturn(controlDoc);

		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, message)).andReturn(controlResource);
	
		AbstractCompareRequestValidator validator = new AbstractCompareRequestValidator(){
			@Override
			protected void compareDocuments(Document controlDocument, Document messageDocument) {
				assertSame(controlDoc, controlDocument);
				assertSame(messageDoc, messageDocument);
			}
			
		};
		validator.setXmlUtil(xmlUtil);
		validator.setControlResourceLookup(resourceLookup);
		assertSame(resourceLookup, validator.getControlResourceLookup());
		validator.afterPropertiesSet();
		
		replay(xmlUtil, resourceLookup);
		
		validator.validateRequest(null, message);
		
		verify(xmlUtil, resourceLookup);
	}
	@Test
	public void testEmptyControlDocument() throws Exception
	{
		WebServiceMessage message = getValidMessage();
		final Document messageDoc = createMock(Document.class);
		
		XmlUtil xmlUtil = createMock(XmlUtil.class);
		expect(xmlUtil.loadDocument(message)).andReturn(messageDoc);
		
		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, message)).andReturn(null);
		
		AbstractCompareRequestValidator validator = new AbstractCompareRequestValidator(){
			@Override
			protected void compareDocuments(Document controlDocument, Document messageDocument) {
				fail("Should not get here");
			}
			
		};
		validator.setXmlUtil(xmlUtil);
		validator.setControlResourceLookup(resourceLookup);
		validator.afterPropertiesSet();
		
		replay(xmlUtil, resourceLookup);
		
		validator.validateRequest(null, message);
		
		verify(xmlUtil, resourceLookup);
	}
}
