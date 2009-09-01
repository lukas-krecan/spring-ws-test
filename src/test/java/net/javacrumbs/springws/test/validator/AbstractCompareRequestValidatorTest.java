package net.javacrumbs.springws.test.validator;

import java.io.IOException;

import net.javacrumbs.springws.test.lookup.ResourceLookup;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

import static org.easymock.EasyMock.*; 
import static org.junit.Assert.*;

public class AbstractCompareRequestValidatorTest extends AbstractValidatorTest{

	
		
	@Test
	public void testValidateRequest() throws IOException
	{
		WebServiceMessage message = getValidMessage();
		final Document messageDoc = createMock(Document.class);
		final Document controlDoc = createMock(Document.class);
		ByteArrayResource controlResource = new ByteArrayResource(new byte[0]);

		XmlUtil xmlUtil = createMock(XmlUtil.class);
		expect(xmlUtil.loadDocument(message)).andReturn(messageDoc);
		expect(xmlUtil.loadDocument(controlResource)).andReturn(controlDoc);

		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, message)).andReturn(controlResource);
	
		AbstractCompareRequestValidator validator = new AbstractCompareRequestValidator(){
			@Override
			protected void compareDocuments(Document controlDocument, Document messageDocument) {
				assertSame(controlDoc, controlDocument);
				assertSame(messageDoc, messageDocument);
			}
			
		};
		validator.setXmlUtil(xmlUtil);
		validator.setControlResourceLookup(resourceLookup);
		
		replay(xmlUtil, resourceLookup);
		
		validator.validateRequest(null, message);
		
		verify(xmlUtil, resourceLookup);
	}
	@Test
	public void testEmptyControlDocument() throws IOException
	{
		WebServiceMessage message = getValidMessage();
		final Document messageDoc = createMock(Document.class);
		
		XmlUtil xmlUtil = createMock(XmlUtil.class);
		expect(xmlUtil.loadDocument(message)).andReturn(messageDoc);
		
		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, message)).andReturn(null);
		
		AbstractCompareRequestValidator validator = new AbstractCompareRequestValidator(){
			@Override
			protected void compareDocuments(Document controlDocument, Document messageDocument) {
				fail("Should not get here");
			}
			
		};
		validator.setXmlUtil(xmlUtil);
		validator.setControlResourceLookup(resourceLookup);
		
		replay(xmlUtil, resourceLookup);
		
		validator.validateRequest(null, message);
		
		verify(xmlUtil, resourceLookup);
	}
}
