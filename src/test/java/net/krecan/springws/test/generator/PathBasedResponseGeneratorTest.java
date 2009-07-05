package net.krecan.springws.test.generator;

import static net.krecan.springws.test.util.XmlUtil.loadDocument;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;

import net.krecan.springws.test.AbstractMessageTest;

import org.custommonkey.xmlunit.Diff;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.Document;


public class PathBasedResponseGeneratorTest extends AbstractMessageTest{
	
	private PathBasedResponseGenerator generator;
	public PathBasedResponseGeneratorTest() {
		generator = new PathBasedResponseGenerator();
		XPathExpression resourceXpathExpression = XPathExpressionFactory.createXPathExpression("concat('mock-responses/test/',//ns:text,'-response.xml')", Collections.singletonMap("ns", "http://www.example.org/schema"));
		generator.setResourceXPathExpression(resourceXpathExpression);
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
	public void testDifferentResponse() throws IOException
	{
		WebServiceMessage response = generator.generateResponse(null, messageFactory, createMessage("xml/valid-message2.xml"));
		assertNotNull(response);
		
		Document controlDocument = loadDocument(new ResourceSource(new ClassPathResource("mock-responses/test/different-response.xml")));
		Diff diff = new Diff(controlDocument, loadDocument(response));
		assertTrue(diff.toString(), diff.similar());
	}
}
