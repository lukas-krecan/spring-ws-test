/**
 * Copyright 2006 the original author or authors.
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
