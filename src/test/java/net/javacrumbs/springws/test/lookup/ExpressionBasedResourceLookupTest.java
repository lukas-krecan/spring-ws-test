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
package net.javacrumbs.springws.test.lookup;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;

import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.expression.ExpressionResolver;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

public class ExpressionBasedResourceLookupTest extends AbstractMessageTest{

	
	@Test
	public void testNoExpressions() throws IOException
	{
		ExpressionBasedResourceLookup resourceLookup = new ExpressionBasedResourceLookup();
		
		assertNull(resourceLookup.lookupResource(null, createMessage("xml/valid-message.xml")));
	}
	@Test
	public void testFirstExpression() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		String responsePath = "mock-responses/test/default-response.xml";

		ExpressionBasedResourceLookup resourceLookup = new ExpressionBasedResourceLookup();
		resourceLookup.setResourceExpressions(new String[]{"expr1","expr2"});
		
		ExpressionResolver resolver = createMock(ExpressionResolver.class);
		expect(resolver.resolveExpression(eq("expr1"), (URI)isNull(), (Document)anyObject())).andReturn(responsePath);
		resourceLookup.setExpressionResolver(resolver);

		replay(resolver);
				
		Resource resource = resourceLookup.lookupResource(null, request);
		assertEquals(new ClassPathResource(responsePath), resource);
		verify(resolver);
	}
	@Test
	public void testSecondExpression() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		String responsePath = "mock-responses/test/default-response.xml";
		
		ExpressionBasedResourceLookup resourceLookup = new ExpressionBasedResourceLookup();
		resourceLookup.setResourceExpressions(new String[]{"expr1","expr2"});
		
		ExpressionResolver resolver = createMock(ExpressionResolver.class);
		expect(resolver.resolveExpression(eq("expr1"), (URI)isNull(), (Document)anyObject())).andReturn("wrongPath");
		expect(resolver.resolveExpression(eq("expr2"), (URI)isNull(), (Document)anyObject())).andReturn(responsePath);
		resourceLookup.setExpressionResolver(resolver);
		
		replay(resolver);
		
		Resource resource = resourceLookup.lookupResource(null, request);
		assertEquals(new ClassPathResource(responsePath), resource);
		verify(resolver);
	}
	@Test
	public void testNoExpression() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		
		ExpressionBasedResourceLookup resourceLookup = new ExpressionBasedResourceLookup();
		resourceLookup.setResourceExpressions(new String[]{"expr1","expr2"});
		
		ExpressionResolver resolve = createMock(ExpressionResolver.class);
		expect(resolve.resolveExpression(eq("expr1"), (URI)isNull(), (Document)anyObject())).andReturn("wrongPath");
		expect(resolve.resolveExpression(eq("expr2"), (URI)isNull(), (Document)anyObject())).andReturn("wrongPath2");
		resourceLookup.setExpressionResolver(resolve);
		
		replay(resolve);
		
		assertNull(resourceLookup.lookupResource(null, request));
		verify(resolve);
	}
}
