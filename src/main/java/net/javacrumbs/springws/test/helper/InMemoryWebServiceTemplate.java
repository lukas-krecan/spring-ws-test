package net.javacrumbs.springws.test.helper;

import java.io.IOException;
import java.net.URI;

import javax.xml.transform.TransformerException;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.WebServiceConnection;

/**
 * WebService template altered to be easy to use when sending messages in memory.
 * @author Lukas Krecan
 *
 */
class InMemoryWebServiceTemplate extends WebServiceTemplate {
	private static final WebServiceMessageCallback DUMMY_REQUEST_CALLBACK = new WebServiceMessageCallback()
	{
		public void doWithMessage(WebServiceMessage message) throws IOException ,TransformerException {};
	};
	
	private static final WebServiceMessageExtractor DUMMY_MESSAGE_EXTRACTOR = new WebServiceMessageExtractor() {
		public Object extractData(WebServiceMessage message) throws IOException, TransformerException {
			return null;
		}
	};
	
	public void send(MessageContext context) throws IOException {
		WebServiceConnection connection = getMessageSenders()[0].createConnection(URI.create(getDefaultUri()));
		doSendAndReceive(context, connection, DUMMY_REQUEST_CALLBACK, DUMMY_MESSAGE_EXTRACTOR); 			
	}
}
