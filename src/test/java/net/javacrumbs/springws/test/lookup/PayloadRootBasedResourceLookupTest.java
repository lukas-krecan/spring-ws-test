package net.javacrumbs.springws.test.lookup;

import static java.util.Arrays.asList;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.javacrumbs.springws.test.expression.XPathExpressionResolver;
import net.javacrumbs.springws.test.validator.AbstractValidatorTest;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;



public class PayloadRootBasedResourceLookupTest extends AbstractValidatorTest{

	@Test
	public void testLookupSimple() throws IOException
	{
		ResourceLoader resourceLoader = createMock(ResourceLoader.class);
		ByteArrayResource resource = new ByteArrayResource("Hi".getBytes());
		expect(resourceLoader.getResource("mock-xml/test/text-response.xml")).andReturn(resource);
		
		XPathExpressionResolver resolver = new XPathExpressionResolver();
		resolver.setNamespaceMap(Collections.singletonMap("ns", "http://www.example.org/schema"));
		
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		Map<String, List<String>> discriminators = new HashMap<String, List<String>>();
		discriminators.put("test", asList("//ns:text"));
		lookup.setDiscriminators(discriminators);
		lookup.setResourceLoader(resourceLoader);
		lookup.setExpressionResolver(resolver);
				
		replay(resourceLoader);
		
		Resource result = lookup.lookupResource(TEST_URI, getValidMessage());
		assertSame(resource, result);
		
		verify(resourceLoader);
	}
	@Test
	public void testLookupMoreExpressions() throws IOException
	{
		ResourceLoader resourceLoader = createMock(ResourceLoader.class);
		ByteArrayResource resource = new ByteArrayResource("Hi".getBytes());
		expect(resourceLoader.getResource("mock-xml/test/text-0-response.xml")).andReturn(resource);
		
		XPathExpressionResolver resolver = new XPathExpressionResolver();
		resolver.setNamespaceMap(Collections.singletonMap("ns", "http://www.example.org/schema"));
		
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		Map<String, List<String>> discriminators = new HashMap<String, List<String>>();
		discriminators.put("test", asList("//ns:text","//ns:number"));
		lookup.setDiscriminators(discriminators);
		lookup.setResourceLoader(resourceLoader);
		lookup.setExpressionResolver(resolver);
		
		replay(resourceLoader);
		
		Resource result = lookup.lookupResource(TEST_URI, getValidMessage());
		assertSame(resource, result);
		
		verify(resourceLoader);
	}
	@Test
	public void testResourceNotFound() throws IOException
	{
		ResourceLoader resourceLoader = createMock(ResourceLoader.class);
		ByteArrayResource resource = new ByteArrayResource("Hi".getBytes());
		expect(resourceLoader.getResource("mock-xml/test/text-0-response.xml")).andReturn(null);
		expect(resourceLoader.getResource("mock-xml/test/text-response.xml")).andReturn(null);
		expect(resourceLoader.getResource("mock-xml/test/response.xml")).andReturn(resource);
		
		XPathExpressionResolver resolver = new XPathExpressionResolver();
		resolver.setNamespaceMap(Collections.singletonMap("ns", "http://www.example.org/schema"));
		
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		Map<String, List<String>> discriminators = new HashMap<String, List<String>>();
		discriminators.put("test", asList("//ns:text","//ns:number"));
		lookup.setDiscriminators(discriminators);
		lookup.setResourceLoader(resourceLoader);
		lookup.setExpressionResolver(resolver);
		
		replay(resourceLoader);
		
		Resource result = lookup.lookupResource(TEST_URI, getValidMessage());
		assertSame(resource, result);
		
		verify(resourceLoader);
	}
	@Test
	public void testNull() throws IOException
	{
		XPathExpressionResolver resolver = new XPathExpressionResolver();
		resolver.setNamespaceMap(Collections.singletonMap("ns", "http://www.example.org/schema"));
		
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		lookup.setExpressionResolver(resolver);
		
		assertEquals("mock-xml/test/text-response.xml",lookup.getResourceName(TEST_URI, "test", asList("//ns:text","garbage"), 2, getXmlUtil().loadDocument(getValidMessage())));
		assertEquals("mock-xml/test/text-response.xml",lookup.getResourceName(TEST_URI, "test", asList("garbage","//ns:text"), 2, getXmlUtil().loadDocument(getValidMessage())));
	}
	@Test
	public void testUri() throws IOException
	{
		XPathExpressionResolver resolver = new XPathExpressionResolver();
		resolver.setNamespaceMap(Collections.singletonMap("ns", "http://www.example.org/schema"));
		
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		lookup.setExpressionResolver(resolver);
		lookup.setPathPrefix("mock-request/");
		lookup.setPathSuffix("request.xml");
		lookup.setDiscriminatorDelimiter("_");
		lookup.setPayloadDelimiter("-");
		
		assertEquals("mock-request/test-localhost_text_request.xml",lookup.getResourceName(TEST_URI, "test", asList("$uri.host","//ns:text"), 2, getXmlUtil().loadDocument(getValidMessage())));
	}
	
	
	//TODO template
}
