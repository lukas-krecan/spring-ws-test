package net.javacrumbs.springws.test.validator;

import java.io.IOException;

import net.javacrumbs.springws.test.WsTestException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;



public class XmlCompareRequestValidatorTest extends AbstractValidatorTest {
	private AbstractCompareRequestValidator validator;
	
	public XmlCompareRequestValidatorTest()
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
