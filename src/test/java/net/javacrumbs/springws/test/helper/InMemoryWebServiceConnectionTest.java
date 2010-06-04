/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
