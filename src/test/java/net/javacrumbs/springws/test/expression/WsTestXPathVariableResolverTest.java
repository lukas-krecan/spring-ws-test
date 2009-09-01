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
package net.javacrumbs.springws.test.expression;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import javax.xml.namespace.QName;

import net.javacrumbs.springws.test.context.WsTestContextHolder;

import org.junit.Test;

public class WsTestXPathVariableResolverTest {

	@Test
	public void testUri() throws Exception
	{
		WsTestContextHolder.getTestContext().setAttribute("name", "aaa");
		
		URI uri = new URI("http://example.org/context/path");
		WsTestXPathVariableResolver resolver = new WsTestXPathVariableResolver(uri);
		assertEquals(uri, resolver.resolveVariable(new QName("uri")));
		assertEquals(uri.getHost(), resolver.resolveVariable(new QName("uri.host")));
		assertEquals("aaa", resolver.resolveVariable(new QName("context.name")));
	}
	@Test
	public void testNormalize() throws Exception
	{
		URI uri = new URI("http://example.org/context/path");
		WsTestXPathVariableResolver resolver = new WsTestXPathVariableResolver(uri);
		
		assertEquals("test",resolver.normalizeContextVariable("test"));
		assertEquals("context[name]",resolver.normalizeContextVariable("context.name"));
		assertEquals("context[name].test",resolver.normalizeContextVariable("context.name.test"));
	}
}
