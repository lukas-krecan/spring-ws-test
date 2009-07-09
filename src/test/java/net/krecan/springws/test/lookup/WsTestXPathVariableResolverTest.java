package net.krecan.springws.test.lookup;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import javax.xml.namespace.QName;

import net.krecan.springws.test.context.WsTestContextHolder;

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
		assertEquals("aaa", resolver.resolveVariable(new QName("context[name]")));
	}
}
