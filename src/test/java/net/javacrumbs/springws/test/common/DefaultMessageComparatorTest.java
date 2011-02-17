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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.validator.AbstractValidatorTest;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;


public class DefaultMessageComparatorTest extends AbstractValidatorTest {
	private DefaultMessageComparator comparator = new DefaultMessageComparator();
	
	@Test
	public void testValid() throws IOException
	{
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/control-message-test.xml"));
		assertTrue(comparator.isSoap(controlDocument));
		comparator.compareDocuments(controlDocument, getXmlUtil().loadDocument(getValidMessage()));
	}
	
	public void compareDocuments(String control, String test) throws IOException
	{
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource(control));
		comparator.compareDocuments(controlDocument, getXmlUtil().loadDocument(createMessage(test)));
	}
	@Test
	public void testValidTest2() throws IOException
	{
		compareDocuments("xml/control-message-test2.xml","xml/valid-message-test2.xml");
	}
	@Test
	public void testValidTestDifferentNsPrefixes() throws IOException
	{
		compareDocuments("xml/namespace-message1.xml","xml/namespace-message2.xml");
	}
	@Test
	public void testValidTestDifferentNsPrefixesNoPrefix() throws IOException
	{
		compareDocuments("xml/namespace-message1.xml","xml/namespace-message4-no-prefix.xml");
	}
	@Test
	public void testValidTestDifferentNsPrefixesNoPrefixNoDefaultNamespace() throws IOException
	{
		try
		{
			compareDocuments("xml/namespace-message1.xml","xml/namespace-message6-no-prefix-no-default-namespace.xml");
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			assertTrue(e.getMessage().contains("Source message:"));
			assertTrue(e.getMessage().contains("Control message:"));
		}
		
	}
	@Test
	public void testValidTestDifferentNsPrefixesNotResolved() throws IOException
	{
		try
		{
			compareDocuments("xml/namespace-message1.xml","xml/namespace-message3-ns-not-resolved.xml");
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			assertTrue(e.getMessage().contains("Source message:"));
			assertTrue(e.getMessage().contains("Control message:"));
		}
	}
	@Test
	public void testValidTestDifferentNsPrefixesNotResolvedinBothFiles() throws IOException
	{
		try
		{
			compareDocuments("xml/namespace-message5-ns-not-resolved.xml","xml/namespace-message3-ns-not-resolved.xml");
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			assertTrue(e.getMessage().contains("Source message:"));
			assertTrue(e.getMessage().contains("Control message:"));
		}
	}
	@Test
	public void testValidDifferent() throws IOException
	{
		compareDocuments("xml/control-message-test.xml","xml/valid-message2.xml");
	}
	@Test
	public void testValidBug37() throws IOException
	{
		comparator.compareDocuments(getXmlUtil().loadDocument(new ClassPathResource("xml/request1.xml")), getXmlUtil().loadDocument(new ClassPathResource("xml/request2.xml")));
	}
	@Test
	public void testInvalid() throws Exception
	{
		try
		{		
			compareDocuments("xml/control-message-test.xml","xml/invalid-message.xml");
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			assertFalse(e.getMessage().contains("[not identical]"));
		}
	}
	
	@Test
	public void testValidateRequest() throws Exception
	{
		WebServiceMessage message = getValidMessage();
		final Document messageDoc = getXmlUtil().loadDocument(message);
		Resource controlResource = new ClassPathResource("xml/control-message-test.xml");
		final Document controlDoc = getXmlUtil().loadDocument(controlResource);
		
		DefaultMessageComparator comparator = new DefaultMessageComparator(){
			@Override
			protected void compareDocuments(Document controlDocument, Document messageDocument) {
				assertTrue(XMLUnit.compareXML(controlDoc, controlDocument).identical());
				assertTrue(XMLUnit.compareXML(messageDoc, messageDocument).identical());
			}
			
		};
		assertTrue(comparator.isSoap(controlDoc));
		
		comparator.compareMessage(message, controlResource);
	}
	
	@Test
	public void testValidatePayload() throws Exception
	{
		WebServiceMessage message = getValidMessage();
		final Document messagePayloadDoc = getXmlUtil().loadDocument(new ClassPathResource("xml/valid-message-payload.xml"));
		
		Resource controlResource = new ClassPathResource("xml/control-message-payload-test.xml");
		final Document controlDoc = getXmlUtil().loadDocument(controlResource);
		
		DefaultMessageComparator comparator = new DefaultMessageComparator(){
			@Override
			protected void compareDocuments(Document controlDocument, Document messageDocument) {
				assertTrue(XMLUnit.compareXML(controlDoc, controlDocument).identical());
				
				Diff diff = new Diff(messagePayloadDoc, messageDocument);
				assertTrue(diff.toString(), diff.similar());
			}
			
		};
		assertFalse(comparator.isSoap(controlDoc));
			
		comparator.compareMessage(message, controlResource);
	}
}
