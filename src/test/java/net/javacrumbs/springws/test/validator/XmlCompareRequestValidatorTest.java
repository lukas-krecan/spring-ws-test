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

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.javacrumbs.springws.test.WsTestException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;



public class XmlCompareRequestValidatorTest extends AbstractValidatorTest {
	private AbstractCompareRequestValidator validator;
	
	public XmlCompareRequestValidatorTest()
	{
		validator = new XmlCompareRequestValidator();
	}
	

	@Test
	public void testValid() throws IOException
	{
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/control-message-test.xml"));
		assertTrue(validator.isSoap(controlDocument));
		validator.compareDocuments(controlDocument, getXmlUtil().loadDocument(getValidMessage()));
	}
	@Test
	public void testValidTest2() throws IOException
	{
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/control-message-test2.xml"));
		validator.compareDocuments(controlDocument, getXmlUtil().loadDocument(createMessage("xml/valid-message-test2.xml")));
	}
	@Test
	public void testValidDifferent() throws IOException
	{
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/control-message-test.xml"));
		validator.compareDocuments(controlDocument, getXmlUtil().loadDocument(createMessage("xml/valid-message2.xml")));
	}
	@Test(expected=WsTestException.class)
	public void testInvalid() throws Exception
	{
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/control-message-test.xml"));
		validator.compareDocuments(controlDocument, getXmlUtil().loadDocument(getInvalidMessage()));
	}
}
