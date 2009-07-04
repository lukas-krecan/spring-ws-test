package net.krecan.springws.test.validator;

import static org.junit.Assert.fail;

import java.io.IOException;

import net.krecan.springws.test.WsTestException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;



public class XmlCompareValidatorTest extends AbstractValidatorTest {
	private XmlCompareValidator validator;
	
	public XmlCompareValidatorTest() throws Exception
	{
		validator = new XmlCompareValidator();
		validator.setControlResource(new ClassPathResource("xml/valid-message.xml"));
	}
	
	@Test
	public void testValid() throws IOException
	{
		validator.validate(null, getValidMessage());
	}
	@Test
	public void testInvalid() throws Exception
	{
		WebServiceMessage message = getInvalidMessage();
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
