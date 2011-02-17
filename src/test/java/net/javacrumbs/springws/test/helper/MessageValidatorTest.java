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

package net.javacrumbs.springws.test.helper;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.xml.transform.Source;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.context.WsTestContextHolder;
import net.javacrumbs.springws.test.template.FreeMarkerTemplateProcessor;
import net.javacrumbs.springws.test.validator.AbstractValidatorTest;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.validation.XmlValidator;
import org.xml.sax.SAXParseException;


public class MessageValidatorTest extends AbstractValidatorTest{

	
	@Test
	public void testCompareMessage() throws IOException
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).compare("xml/valid-message.xml");
	}
	
	@Test(expected=WsTestException.class)
	public void testCompareMessageFail() throws IOException
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).compare("xml/invalid-message.xml");
	}
	
	@Test
	public void testValidateMessage() throws IOException
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).validate("xml/schema.xsd");
	}
	@Test(expected=WsTestException.class)
	public void testValidateMessageFail() throws IOException
	{
		WebServiceMessage message = createMessage("xml/invalid-message.xml");
		new MessageValidator(message).validate("xml/schema.xsd");
	}
	@Test
	public void testValidateGenericOk() throws IOException
	{
		XmlValidator validator = createMock(XmlValidator.class);
		expect(validator.validate((Source)anyObject())).andReturn(new SAXParseException[0]);
		replay(validator);
		
		WebServiceMessage message = createMessage("xml/invalid-message.xml");
		new MessageValidator(message).validate(validator);
		
		verify(validator);
	}
	@Test(expected=WsTestException.class)
	public void testValidateGenericFail() throws IOException
	{
		XmlValidator validator = createMock(XmlValidator.class);
		expect(validator.validate((Source)anyObject())).andReturn(new SAXParseException[]{new SAXParseException("Test message", null)});
		replay(validator);
		
		WebServiceMessage message = createMessage("xml/invalid-message.xml");
		new MessageValidator(message).validate(validator);
		
		verify(validator);
	}

	@Test(expected=WsTestException.class)
	public void testValidateResponseMultipleSchemesFail() throws Exception
	{
		WebServiceMessage message = createMessage("xml/invalid-message.xml");
		new MessageValidator(message).validate("xml/calc.xsd", "xml/schema.xsd");
	}
	
	@Test(expected=WsTestException.class)
	public void testValidateResponseMultipleSchemesFail2() throws Exception
	{
		WebServiceMessage message = createMessage("xml/invalid-message.xml");
		new MessageValidator(message).validate("xml/schema.xsd", "xml/calc.xsd");
	}
	@Test
	public void testValidateResponseMultipleSchemesOk() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).validate("xml/schema.xsd", "xml/calc.xsd");
		new MessageValidator(message).validate("xml/calc.xsd", "xml/schema.xsd");
	}
	@Test
	public void testAssertThatOk() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		Map<String, String> nsMap = Collections.singletonMap("ns", "http://www.example.org/schema");
		new MessageValidator(message).useNamespaceMapping(nsMap).assertXPath("//ns:number=0");
	}

	@Test(expected=WsTestException.class)
	public void testAssertThatFail() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).addNamespaceMapping("ns", "http://www.example.org/schema").assertXPath("//ns:number=1");
	}
	
	@Test
	public void testContainElement() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertContainElement("number");
	}
	@Test(expected=WsTestException.class)
	public void testContainElementFail() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertContainElement("xxx");
	}
	@Test
	public void testNotContainElement() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertNotContainElement("xxx");
	}
	@Test(expected=WsTestException.class)
	public void testNotContainElementFail() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertNotContainElement("number");
	}
	
	
	@Test
	public void testContain() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertContain("<number>0</");
	}
	@Test(expected=WsTestException.class)
	public void testContainFail() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertContain("<number>1</");
	}
	@Test
	public void testNotContain() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertNotContain("<number>\\s*1</");
	}
	@Test(expected=WsTestException.class)
	public void testNotContainFail() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertNotContain("<number>0</");
	}
	
	
	@Test
	public void testAssertNotSoapFault() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertNotSoapFault();
	}
	@Test(expected=WsTestException.class)
	public void testAssertNotSoapFaultFail() throws Exception
	{
		WebServiceMessage message = createMessage("xml/fault.xml");
		new MessageValidator(message).assertNotSoapFault();
	}
	
	@Test(expected=WsTestException.class)
	public void testAssertSoapFault() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertSoapFault();
	}

	@Test
	public void testAssertSoapMessageFault() throws Exception
	{
		WebServiceMessage message = createMessage("xml/fault.xml");
		new MessageValidator(message).assertSoapMessage().assertSoapFault().assertFaultCode("FaultCode").assertFaultStringOrReason("FaultString").assertFaultActorOrRole("FaultActor");
	}

	@Test(expected=WsTestException.class)
	public void testAssertSoapMessageFaultDiffernentCode() throws Exception
	{
		WebServiceMessage message = createMessage("xml/fault.xml");
		new MessageValidator(message).assertSoapMessage().assertFaultCode("XXX");
	}
	@Test(expected=WsTestException.class)
	public void testAssertSoapMessageFaultDifferentString() throws Exception
	{
		WebServiceMessage message = createMessage("xml/fault.xml");
		new MessageValidator(message).assertSoapMessage().assertFaultCode("FaultCode").assertFaultStringOrReason("XXX");
	}
	@Test(expected=WsTestException.class)
	public void testAssertSoapMessageFaultDifferentActor() throws Exception
	{
		WebServiceMessage message = createMessage("xml/fault.xml");
		new MessageValidator(message).assertSoapMessage().assertFaultCode("FaultCode").assertFaultStringOrReason("FaultString").assertFaultActorOrRole("XXX");
	}
	@Test
	public void testAssertSoapMessage() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message.xml");
		new MessageValidator(message).assertSoapMessage();
	}
	@Test(expected=WsTestException.class)
	public void testAssertSoapMessageFail() throws Exception
	{
		WebServiceMessage message = createMessage("xml/valid-message-payload.xml");
		new MessageValidator(message).assertSoapMessage();
	}
	@Test
	public void testUseFreeMarker() throws Exception
	{
		WsTestContextHolder.getTestContext().setAttribute("a", 1);
		WsTestContextHolder.getTestContext().setAttribute("b", 2);
		
		WebServiceMessage message = createMessage("xml/request1-envelope.xml");
		MessageValidator validator = new MessageValidator(message).useFreeMarkerTemplateProcessor();
		assertEquals(FreeMarkerTemplateProcessor.class, validator.getTemplateProcessor().getClass());
		validator.compare("xml/request-context.xml");
		
		WsTestContextHolder.getTestContext().clear();
	}
}
