package net.krecan.springws.test.validator;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

public abstract class AbstractValidatorTest {

	protected SaajSoapMessageFactory messageFactory;

	public AbstractValidatorTest() throws Exception {
		messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
	}
	protected WebServiceMessage getValidMessage() throws IOException {
		return messageFactory.createWebServiceMessage(new ClassPathResource("xml/valid-message.xml").getInputStream());
	}
	protected WebServiceMessage getInvalidMessage() throws IOException {
		return messageFactory.createWebServiceMessage(new ClassPathResource("xml/invalid-message.xml").getInputStream());
	}


}