package net.krecan.springws.test.validator;

import java.io.IOException;

import net.krecan.springws.test.WsTestException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;



public class XmlCompareValidatorTest extends AbstractValidatorTest {
	private AbstractCompareRequestValidator validator;
	
	public XmlCompareValidatorTest() throws Exception
	{
		validator = new XmlCompareRequestValidator();
	}
	
	@Test
	public void testValid() throws IOException
	{
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/control-message-test.xml"));
		validator.compareDocuments(controlDocument, getXmlUtil().loadDocument(getValidMessage()));
	}
	@Test
	public void testValidTest2() throws IOException
	{
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/control-message-test2.xml"));
		validator.compareDocuments(controlDocument, getXmlUtil().loadDocument(createMessage("xml/valid-message-test2.xml")));
	}
	@Test
	public void testValidDifferent() throws IOException
	{
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/control-message-test.xml"));
		validator.compareDocuments(controlDocument, getXmlUtil().loadDocument(createMessage("xml/valid-message2.xml")));
	}
	@Test(expected=WsTestException.class)
	public void testInvalid() throws Exception
	{
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/control-message-test.xml"));
		validator.compareDocuments(controlDocument, getXmlUtil().loadDocument(getInvalidMessage()));
	}
}
