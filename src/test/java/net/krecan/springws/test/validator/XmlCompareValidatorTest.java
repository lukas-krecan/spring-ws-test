package net.krecan.springws.test.validator;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.krecan.springws.test.WsTestException;
import net.krecan.springws.test.lookup.XPathResourceLookup;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;



public class XmlCompareValidatorTest extends AbstractValidatorTest {
	private XmlCompareValidator validator;
	
	public XmlCompareValidatorTest() throws Exception
	{
		validator = new XmlCompareValidator();
		
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("ns", "http://www.example.org/schema");
		namespaceMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		
		XPathResourceLookup resourceLookup = new XPathResourceLookup();
		XPathExpression resourceXpathExpression = XPathExpressionFactory.createXPathExpression("concat('xml/control-message-',name(//soapenv:Body/*[1]),'.xml')", namespaceMap);
		resourceLookup.setResourceXPathExpression(resourceXpathExpression);
		
		validator.setControlResourceLookup(resourceLookup);
	}
	
	@Test
	public void testValid() throws IOException
	{
		validator.validate(null, getValidMessage());
	}
	@Test
	public void testValidTest2() throws IOException
	{
		validator.validate(null, createMessage("xml/valid-message-test2.xml"));
	}
	@Test
	public void testValidDifferent() throws IOException
	{
		validator.validate(null,createMessage("xml/valid-message2.xml"));
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
