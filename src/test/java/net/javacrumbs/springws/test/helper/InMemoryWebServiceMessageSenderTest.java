package net.javacrumbs.springws.test.helper;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.net.URI;

import org.junit.Test;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceMessageReceiver;

public class InMemoryWebServiceMessageSenderTest {

	@Test
	public void testCreateConnection() throws Exception
	{

		WebServiceMessageFactory messageFactory = createMock(WebServiceMessageFactory.class);
		WebServiceMessageReceiver messageReceiver = createMock(WebServiceMessageReceiver.class);
		
		InMemoryWebServiceConnection connection = new InMemoryWebServiceMessageSender(messageFactory, messageReceiver).createConnection(new URI("http://localhost"));
		assertNotNull(connection);
		assertSame(messageFactory, connection.getMessageFactory());
		assertSame(messageReceiver, connection.getWebServiceMessageReceiver());
	}
}
