package net.krecan.springws.test.validator;

import java.io.IOException;

import net.krecan.springws.test.AbstractMessageTest;

import org.springframework.ws.WebServiceMessage;

public abstract class AbstractValidatorTest extends AbstractMessageTest{

	protected WebServiceMessage getValidMessage() throws IOException {
		return createMessage("xml/valid-message.xml");
	}
	protected WebServiceMessage getInvalidMessage() throws IOException {
		return createMessage("xml/invalid-message.xml");
	}
}