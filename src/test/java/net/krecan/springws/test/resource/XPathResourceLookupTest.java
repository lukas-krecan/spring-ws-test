package net.krecan.springws.test.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.krecan.springws.test.AbstractMessageTest;
import net.krecan.springws.test.resource.XPathResourceLookup;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;


public class XPathResourceLookupTest extends AbstractMessageTest {
	private XPathResourceLookup resourceLookup;
	public XPathResourceLookupTest() {
		
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("ns", "http://www.example.org/schema");
		namespaceMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		
		
		resourceLookup = new XPathResourceLookup();
		XPathExpression resourceXpathExpression = XPathExpressionFactory.createXPathExpression("concat('mock-responses/',name(//soapenv:Body/*[1]),'/',//ns:text,'-response.xml')", namespaceMap);
				
		XPathExpression defaultXPathExpression = XPathExpressionFactory.createXPathExpression("concat('mock-responses/',name(//soapenv:Body/*[1]),'/default-response.xml')", namespaceMap);
		resourceLookup.setResourceXPathExpressions(new XPathExpression[]{resourceXpathExpression, defaultXPathExpression});
	}
	
	@Test
	public void testEmpty() throws IOException
	{
		XPathResourceLookup emptyResourceLookup = new XPathResourceLookup();
		assertNull(emptyResourceLookup.lookupResource(null, createMessage("xml/valid-message.xml")));
	}
	@Test
	public void testNormal() throws IOException
	{
		Resource resource = resourceLookup.lookupResource(null, createMessage("xml/valid-message.xml"));
		assertEquals(new ClassPathResource("mock-responses/test/default-response.xml").getFile(), resource.getFile());
	}
	@Test
	public void testDifferent() throws IOException
	{
		Resource resource = resourceLookup.lookupResource(null, createMessage("xml/valid-message2.xml"));
		assertEquals(new ClassPathResource("mock-responses/test/different-response.xml").getFile(), resource.getFile());
	}
	@Test
	public void testDifferentTest2() throws IOException
	{
		Resource resource = resourceLookup.lookupResource(null, createMessage("xml/valid-message-test2.xml"));
		assertEquals(new ClassPathResource("mock-responses/test2/different-response.xml").getFile(), resource.getFile());
	}
	
}
