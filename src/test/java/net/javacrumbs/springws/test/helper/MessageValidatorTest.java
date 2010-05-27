package net.javacrumbs.springws.test.helper;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.xml.transform.Source;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.validator.AbstractValidatorTest;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.validation.XmlValidator;
import org.xml.sax.SAXParseException;

import static org.easymock.EasyMock.*;


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
	public void testAssertSoapFaultFail() throws Exception
	{
		WebServiceMessage message = createMessage("xml/fault.xml");
		new MessageValidator(message).assertSoapFault();
	}

}
