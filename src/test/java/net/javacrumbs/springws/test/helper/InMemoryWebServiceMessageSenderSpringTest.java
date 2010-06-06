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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

@RunWith(SpringJUnit4ClassRunner.class)
//your endpoint configuration + Default helper config
@ContextConfiguration(locations={"dispatcher.xml","message-sender-config.xml"})
public class InMemoryWebServiceMessageSenderSpringTest {

	@Autowired
	private InMemoryWebServiceMessageSender messageSender;
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	@Test
	public void testCreateConnection() throws Exception
	{
		assertNotNull(messageSender);
		assertSame(applicationContext.getBean(MessageDispatcherServlet.DEFAULT_MESSAGE_FACTORY_BEAN_NAME), messageSender.getMessageFactory());
		assertSame(applicationContext.getBean(MessageDispatcherServlet.DEFAULT_MESSAGE_RECEIVER_BEAN_NAME), messageSender.getWebServiceMessageReceiver());
	}
}
