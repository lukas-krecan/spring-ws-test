package net.krecan.springws.test.generator;

import static net.krecan.springws.test.util.XmlUtil.loadDocument;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.krecan.springws.test.AbstractMessageTest;
import net.krecan.springws.test.resource.XPathResourceLookup;

import org.custommonkey.xmlunit.Diff;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.Document;


public class DefaultResponseGeneratorTest extends AbstractMessageTest{
	
	private DefaultResponseGenerator generator;
	public DefaultResponseGeneratorTest() {
		generator = new DefaultResponseGenerator();
		
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("ns", "http://www.example.org/schema");
		namespaceMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		
		
		XPathResourceLookup resourceLookup = new XPathResourceLookup();
		XPathExpression resourceXpathExpression = XPathExpressionFactory.createXPathExpression("concat('mock-responses/',name(//soapenv:Body/*[1]),'/',//ns:text,'-response.xml')", namespaceMap);
		resourceLookup.setResourceXPathExpression(resourceXpathExpression);
		
		XPathExpression defaultXPathExpression = XPathExpressionFactory.createXPathExpression("concat('mock-responses/',name(//soapenv:Body/*[1]),'/default-response.xml')", namespaceMap);
		resourceLookup.setDefaultXPathExpression(defaultXPathExpression);
		
		generator.setResourceLookup(resourceLookup);
	}
	@Test
	public void testDefaultResponse() throws IOException
	{
		WebServiceMessage response = generator.generateResponse(null, messageFactory, createMessage("xml/valid-message.xml"));
		assertNotNull(response);
		
		Document controlDocument = loadDocument(new ResourceSource(new ClassPathResource("mock-responses/test/default-response.xml")));
		Diff diff = new Diff(controlDocument, loadDocument(response));
		assertTrue(diff.toString(), diff.similar());
	}
	@Test
	public void testDefaultResponseTest2() throws IOException
	{
		WebServiceMessage response = generator.generateResponse(null, messageFactory, createMessage("mock-responses/test2/default-response.xml"));
		assertNotNull(response);
		
		Document controlDocument = loadDocument(new ResourceSource(new ClassPathResource("mock-responses/test2/default-response.xml")));
		Diff diff = new Diff(controlDocument, loadDocument(response));
		assertTrue(diff.toString(), diff.similar());
	}
	@Test
	public void testDifferentResponse() throws IOException
	{
		WebServiceMessage response = generator.generateResponse(null, messageFactory, createMessage("xml/valid-message2.xml"));
		assertNotNull(response);
		
		Document controlDocument = loadDocument(new ResourceSource(new ClassPathResource("mock-responses/test/different-response.xml")));
		Diff diff = new Diff(controlDocument, loadDocument(response));
		assertTrue(diff.toString(), diff.similar());
	}
	@Test
	public void testDifferentResponseTest2() throws IOException
	{
		WebServiceMessage response = generator.generateResponse(null, messageFactory, createMessage("mock-responses/test2/default-response.xml"));
		assertNotNull(response);
		
		Document controlDocument = loadDocument(new ResourceSource(new ClassPathResource("mock-responses/test2/default-response.xml")));
		Diff diff = new Diff(controlDocument, loadDocument(response));
		assertTrue(diff.toString(), diff.similar());
	}
}
