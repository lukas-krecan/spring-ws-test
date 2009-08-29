package net.javacrumbs.springws.test.validator;

import static org.junit.Assert.fail;
import net.javacrumbs.springws.test.WsTestException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;


public class SchemaValidatorTest extends AbstractValidatorTest {
	private SchemaRequestValidator validator;
	
	public SchemaValidatorTest() throws Exception
	{
		validator = new SchemaRequestValidator();
		validator.setSchema(new ClassPathResource("xml/schema.xsd"));
		validator.afterPropertiesSet();
	}
	
	@Test
	public void testValid() throws Exception
	{
		WebServiceMessage message = getValidMessage();
		validator.validate(null, message );
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
