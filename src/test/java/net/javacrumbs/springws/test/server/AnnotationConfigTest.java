package net.javacrumbs.springws.test.server;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.context.MessageContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:context/server/dispatcher.xml", WsServerTestHelper.DEFAULT_CONFIG_PATH})
public class AnnotationConfigTest {

	@Autowired
	private WsServerTestHelper helper;
	
	@Test
	public void testCallWs() throws Exception
	{
		MessageContext message = helper.receiveMessage("xml/valid-message.xml");
		assertNotNull(message);
	}
}
