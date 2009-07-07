package net.krecan.springws.test.xpath;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import javax.xml.namespace.QName;

import org.junit.Test;

public class TestWsXPathVariableResolverTest {

	@Test
	public void testUri() throws Exception
	{
		URI uri = new URI("http://example.org/context/path");
		TestWsXPathVariableResolver resolver = new TestWsXPathVariableResolver(uri);
		assertEquals(uri, resolver.resolveVariable(new QName("uri")));
		assertEquals(uri.getHost(), resolver.resolveVariable(new QName("uri.host")));
	}
}
