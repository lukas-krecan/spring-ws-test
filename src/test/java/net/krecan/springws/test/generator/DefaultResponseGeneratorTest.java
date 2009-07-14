package net.krecan.springws.test.generator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.krecan.springws.test.AbstractMessageTest;
import net.krecan.springws.test.lookup.ResourceLookup;
import net.krecan.springws.test.util.DefaultXmlUtil;

import org.custommonkey.xmlunit.Diff;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;


public class DefaultResponseGeneratorTest extends AbstractMessageTest{
	
	@Test
	public void testDefaultResponse() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		ClassPathResource responseResource = new ClassPathResource("mock-responses/test/default-response.xml");

		DefaultResponseGenerator generator = new DefaultResponseGenerator();
		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, request)).andReturn(responseResource);
		
		generator.setResourceLookup(resourceLookup);

		replay(resourceLookup);
		
		WebServiceMessage response = generator.generateResponse(null, messageFactory, request);
		assertNotNull(response);
		
		Document controlDocument = loadDocument(responseResource);
		Diff diff = new Diff(controlDocument, loadDocument(response));
		assertTrue(diff.toString(), diff.similar());
		
		verify(resourceLookup);
	}
	private Document loadDocument(WebServiceMessage message) {
		return DefaultXmlUtil.getInstance().loadDocument(message);
	}
	private Document loadDocument(Resource resource) throws IOException {
		return DefaultXmlUtil.getInstance().loadDocument(resource);
	}
	@Test
	public void testNoResourceFound() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		
		DefaultResponseGenerator generator = new DefaultResponseGenerator();
		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, request)).andReturn(null);
		
		generator.setResourceLookup(resourceLookup);
		
		replay(resourceLookup);
		
		WebServiceMessage response = generator.generateResponse(null, messageFactory, request);
		assertNull(response);
		
		
		verify(resourceLookup);
	}
}
