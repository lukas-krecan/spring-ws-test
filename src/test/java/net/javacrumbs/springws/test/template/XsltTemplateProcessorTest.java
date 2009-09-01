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
package net.javacrumbs.springws.test.template;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.context.WsTestContextHolder;

import org.custommonkey.xmlunit.Diff;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

public class XsltTemplateProcessorTest extends AbstractMessageTest {
	private XsltTemplateProcessor processor = new XsltTemplateProcessor();
	@Test
	public void testTemplate() throws IOException
	{
		WsTestContextHolder.getTestContext().setAttribute("number", 2);
		WebServiceMessage request = createMessage("xml/valid-message2.xml");
		ClassPathResource template = new ClassPathResource("mock-responses/test/different-response.xml");
		Resource resource = processor.processTemplate(template, null, request);
		
		
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/resolved-different-response.xml"));
		Document responseDocument = getXmlUtil().loadDocument(resource);
		Diff diff = new Diff(controlDocument, responseDocument);
		assertTrue(diff.toString(), diff.similar());
		
		WsTestContextHolder.getTestContext().clear();
	}
	
	@Test
	public void testIsTemplate() throws IOException
	{
		Resource resource = new ClassPathResource("mock-responses/test/different-response.xml");
		assertTrue(processor.isTemplate(resource));		
	}
	@Test
	public void testIsNotTemplate() throws IOException
	{
		Resource resource = new ClassPathResource("mock-responses/test/default-response.xml");
		assertFalse(processor.isTemplate(resource));		
	}
	@Test
	public void testNormalXml() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		Resource resource = processor.processTemplate(new ClassPathResource("mock-responses/test/default-response.xml"), null, request);
		
		
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("mock-responses/test/default-response.xml"));
		Document responseDocument = getXmlUtil().loadDocument(resource);
		Diff diff = new Diff(controlDocument, responseDocument);
		assertTrue(diff.toString(), diff.similar());
		
	}
}
