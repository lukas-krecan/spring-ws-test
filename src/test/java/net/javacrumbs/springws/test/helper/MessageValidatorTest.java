package net.javacrumbs.springws.test.helper;

import java.io.IOException;

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

}
