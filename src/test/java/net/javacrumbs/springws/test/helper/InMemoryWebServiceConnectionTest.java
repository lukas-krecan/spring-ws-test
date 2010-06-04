package net.javacrumbs.springws.test.helper;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.notNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import net.javacrumbs.springws.test.AbstractMessageTest;

import org.junit.Test;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.WebServiceMessageReceiver;


public class InMemoryWebServiceConnectionTest extends AbstractMessageTest{
	@Test
	public void testSendAndReceive() throws Exception
	{
		WebServiceMessageReceiver messageReceiver = createMock(WebServiceMessageReceiver.class);
		messageReceiver.receive((MessageContext)notNull());
		replay(messageReceiver);
		
		InMemoryWebServiceConnection connection = new InMemoryWebServiceConnection(null, messageFactory, messageReceiver);
		
		connection.send(createMessage("xml/valid-message.xml"));
		assertNotNull(connection.receive(messageFactory));
		
		verify(messageReceiver);
	}
}
