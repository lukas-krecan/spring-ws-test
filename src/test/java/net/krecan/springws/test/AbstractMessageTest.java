package net.krecan.springws.test;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

public class AbstractMessageTest {
	protected SaajSoapMessageFactory messageFactory;

	public AbstractMessageTest() {
		XMLUnit.setIgnoreWhitespace(true);
		try {
			messageFactory = new SaajSoapMessageFactory();
			messageFactory.afterPropertiesSet();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected WebServiceMessage createMessage(String path) throws IOException
	{
		return messageFactory.createWebServiceMessage(new ClassPathResource(path).getInputStream());
	}
}
