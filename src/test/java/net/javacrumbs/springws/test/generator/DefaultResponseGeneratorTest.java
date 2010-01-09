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
package net.javacrumbs.springws.test.generator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.lookup.ResourceLookup;

import org.custommonkey.xmlunit.Diff;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;


public class DefaultResponseGeneratorTest extends AbstractMessageTest{
	
	@Test
	public void testDefaultResponse() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		ClassPathResource responseResource = new ClassPathResource("mock-responses/test/default-response.xml");

		DefaultResponseGenerator generator = new DefaultResponseGenerator();
		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, request)).andReturn(responseResource);
		
		generator.setResourceLookup(resourceLookup);

		replay(resourceLookup);
		
		WebServiceMessage response = generator.processRequest(null, messageFactory, request);
		assertNotNull(response);
		
		Document controlDocument = loadDocument(responseResource);
		Diff diff = new Diff(controlDocument, loadDocument(response));
		assertTrue(diff.toString(), diff.similar());
		
		verify(resourceLookup);
	}
	@Test
	public void testDefaultResponsePayload() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		
		DefaultResponseGenerator generator = new DefaultResponseGenerator();
		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, request)).andReturn(new ClassPathResource("mock-responses/test/default-response-payload.xml"));
		
		generator.setResourceLookup(resourceLookup);
		
		replay(resourceLookup);
		
		WebServiceMessage response = generator.processRequest(null, messageFactory, request);
		assertNotNull(response);
		
		Document controlDocument = loadDocument(new ClassPathResource("mock-responses/test/default-response.xml"));
		Diff diff = new Diff(controlDocument, loadDocument(response));
		assertTrue(diff.toString(), diff.similar());
		
		verify(resourceLookup);
	}
	@Test
	public void testNoResourceFound() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		
		DefaultResponseGenerator generator = new DefaultResponseGenerator();
		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, request)).andReturn(null);
		
		generator.setResourceLookup(resourceLookup);
		
		replay(resourceLookup);
		
		WebServiceMessage response = generator.processRequest(null, messageFactory, request);
		assertNull(response);
		
		
		verify(resourceLookup);
	}
	
}
