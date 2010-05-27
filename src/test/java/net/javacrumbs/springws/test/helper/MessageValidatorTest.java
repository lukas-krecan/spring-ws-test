package net.javacrumbs.springws.test.helper;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.validator.AbstractValidatorTest;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;


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
		Map<String, String> nsMap = Collections.singletonMap("ns", "http://www.example.org/schema");
		new MessageValidator(message).useNamespaceMapping(nsMap).assertXPath("//ns:number=1");
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
