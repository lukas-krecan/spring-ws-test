package net.javacrumbs.springws.test.helper;

import java.io.IOException;
import java.net.URI;

import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceMessageReceiver;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Sends messages locally in memory. Used for server side test.
 * @author Lukas Krecan
 *
 */
class InMemoryWebServiceMessageSender implements WebServiceMessageSender {

	private final WebServiceMessageFactory messageFactory;
	
	private final WebServiceMessageReceiver webServiceMessageReceiver;

	public InMemoryWebServiceMessageSender(WebServiceMessageFactory messageFactory, WebServiceMessageReceiver webServiceMessageReceiver) {
		this.messageFactory = messageFactory;
		this.webServiceMessageReceiver = webServiceMessageReceiver;
	}
	
	public InMemoryWebServiceConnection createConnection(URI uri) throws IOException {
		return new InMemoryWebServiceConnection(uri, messageFactory, webServiceMessageReceiver);
	}

	public boolean supports(URI uri) {
		return true;
	}

	public WebServiceMessageFactory getMessageFactory() {
		return messageFactory;
	}

	public WebServiceMessageReceiver getWebServiceMessageReceiver() {
		return webServiceMessageReceiver;
	}

}
