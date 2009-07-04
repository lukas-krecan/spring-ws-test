package net.krecan.springws.test.validator;

import static org.junit.Assert.*;
import junit.framework.AssertionFailedError;

import net.krecan.springws.test.WsTestException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;


public class SchemaValidatorTest {
private SaajSoapMessageFactory messageFactory;
private SchemaValidator validator;
	
	public SchemaValidatorTest() throws Exception
	{
		messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
		
		validator = new SchemaValidator();
		validator.setSchema(new ClassPathResource("xml/schema.xsd"));
		validator.afterPropertiesSet();
	}
	
	@Test
	public void testValid() throws Exception
	{
		WebServiceMessage message = messageFactory.createWebServiceMessage(new ClassPathResource("xml/valid-message.xml").getInputStream());
		validator.validate(null, message );
	}
	@Test
	public void testInvalid() throws Exception
	{
		WebServiceMessage message = messageFactory.createWebServiceMessage(new ClassPathResource("xml/invalid-message.xml").getInputStream());
		try
		{
			validator.validate(null, message);
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			//ok
		}
	}
}
