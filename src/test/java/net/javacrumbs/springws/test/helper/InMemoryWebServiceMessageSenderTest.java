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

import java.net.URI;

import org.junit.Test;

public class InMemoryWebServiceMessageSenderTest {

	@Test
	public void testCreateConnection() throws Exception
	{

		InMemoryWebServiceMessageSender messageSender = new InMemoryWebServiceMessageSender();
		messageSender.afterPropertiesSet();
		InMemoryWebServiceConnection connection = messageSender.createConnection(new URI("http://localhost"));
		assertNotNull(connection);
		assertNotNull(connection.getMessageFactory());
		assertSame(messageSender.getMessageFactory(), connection.getMessageFactory());
		assertNotNull(connection.getWebServiceMessageReceiver());
	}
}
