package net.krecan.springws.test.generator;

import static net.krecan.springws.test.util.XmlUtil.*;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.transform.dom.DOMSource;

import net.krecan.springws.test.AbstractMessageTest;
import net.krecan.springws.test.MessageLookup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.ResourceSource;
import org.w3c.dom.Document;


public class XsltResponseGeneratorTest extends AbstractMessageTest{
	protected final Log logger = LogFactory.getLog(getClass());
	@Test
	public void testTransformation() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		ClassPathResource responseResource = new ClassPathResource("mock-responses/test/different-response.xml");

		XsltResponseGenerator generator = new XsltResponseGenerator();
		MessageLookup resourceLookup = createMock(MessageLookup.class);
		expect(resourceLookup.lookupResource(null, request)).andReturn(responseResource);
		
		generator.setResourceLookup(resourceLookup);

		replay(resourceLookup);
		
		WebServiceMessage response = generator.generateResponse(null, messageFactory, request);
		assertNotNull(response);
		
		Document controlDocument = loadDocument(new ResourceSource(new ClassPathResource("xml/resolved-different-response.xml")));
		Document responseDocument = loadDocument(response);
		logger.trace("Comapring "+serializeDocument(controlDocument)+"\n to \n"+serializeDocument(responseDocument));
		Diff diff = new Diff(controlDocument, responseDocument);
		assertTrue(diff.toString(), diff.similar());
		
		verify(resourceLookup);
	}
}
