package net.javacrumbs.springws.test.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.javacrumbs.springws.test.AbstractMessageTest;

import org.junit.Test;


public class DefaultXmlUtilTest extends AbstractMessageTest{
	private XmlUtil xmlUtil = new DefaultXmlUtil();
	
	@Test
	public void testPayload() throws IOException
	{
		assertFalse(xmlUtil.isEnvelope(loadDocument("xml/request1.xml")));
	}
	@Test
	public void testEnvelope() throws IOException
	{
		assertTrue(xmlUtil.isEnvelope(loadDocument("xml/valid-message.xml")));
	}
	@Test
	public void testEnvelope12() throws IOException
	{
		assertTrue(xmlUtil.isEnvelope(loadDocument("xml/valid-message-soap12.xml")));
	}
	@Test
	public void testFault() throws IOException
	{
		assertFalse(xmlUtil.isEnvelope(loadDocument("xml/response-error.xml")));
	}
	
}
