package net.krecan.springws.test;

import java.io.IOException;
import java.net.URI;

import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Creates new {@link MockWebServiceConnection}. 
 * @author Lukas Krecan
 *
 */
public class MockWebServiceMessageSender implements WebServiceMessageSender {

	public WebServiceConnection createConnection(URI uri) throws IOException {
		return new MockWebServiceConnection(uri);
	}

	public boolean supports(URI uri) {
		return true;
	}

}
