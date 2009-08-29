package net.javacrumbs.springws.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.Test;
import org.springframework.ws.transport.WebServiceConnection;


public class MockWebServiceMessageSenderTest {

	@Test
	public void testCreateConnection() throws Exception
	{
		MockWebServiceMessageSender sender = new MockWebServiceMessageSender();
		URI uri = new URI("http://example.org/");
		assertTrue(sender.supports(uri));
		WebServiceConnection connection = sender.createConnection(uri);
		assertNotNull(connection);
	}
}
