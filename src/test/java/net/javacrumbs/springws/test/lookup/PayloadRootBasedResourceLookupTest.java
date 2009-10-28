package net.javacrumbs.springws.test.lookup;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.javacrumbs.springws.test.expression.XPathExpressionResolver;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.validator.AbstractValidatorTest;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DescriptiveResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.WebServiceMessage;



public class PayloadRootBasedResourceLookupTest extends AbstractValidatorTest{

	@Test
	public void testLookupSimple() throws IOException
	{
		WebServiceMessage message = getValidMessage();
		
		ResourceLoader resourceLoader = createMock(ResourceLoader.class);
		ByteArrayResource resource = new ByteArrayResource("Hi".getBytes());
		expect(resourceLoader.getResource("mock-xml/test/text-response.xml")).andReturn(resource);
		
		TemplateProcessor templateProcessor = createMock(TemplateProcessor.class);
		expect(templateProcessor.processTemplate(resource, TEST_URI, message)).andReturn(resource);

		XPathExpressionResolver resolver = new XPathExpressionResolver();
		resolver.setNamespaceMap(Collections.singletonMap("ns", "http://www.example.org/schema"));
		
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		Map<String, String[]> discriminators = new HashMap<String, String[]>();
		discriminators.put("test", new String[]{"//ns:text"});
		lookup.setDiscriminators(discriminators);
		lookup.setResourceLoader(resourceLoader);
		lookup.setExpressionResolver(resolver);
		lookup.setTemplateProcessor(templateProcessor);
				
		replay(resourceLoader, templateProcessor);
		
		Resource result = lookup.lookupResource(TEST_URI, message);
		assertSame(resource, result);
		
		verify(resourceLoader, templateProcessor);
	}
	@Test
	public void testLookupMoreExpressions() throws IOException
	{
		ResourceLoader resourceLoader = createMock(ResourceLoader.class);
		Resource resource = new ClassPathResource("mock-responses/test/default-response.xml");
		expect(resourceLoader.getResource("mock-xml/test/text-0-response.xml")).andReturn(resource);
		
		XPathExpressionResolver resolver = new XPathExpressionResolver();
		resolver.setNamespaceMap(Collections.singletonMap("ns", "http://www.example.org/schema"));
		
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		Map<String, String[]> discriminators = new HashMap<String, String[]>();
		discriminators.put("test", new String[]{"//ns:text","//ns:number"});
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
		Resource resource = new ClassPathResource("mock-responses/test/default-response.xml");
		expect(resourceLoader.getResource("mock-xml/test/text-0-response.xml")).andReturn(new DescriptiveResource("No resource"));
		expect(resourceLoader.getResource("mock-xml/test/text-response.xml")).andReturn(null);
		expect(resourceLoader.getResource("mock-xml/test/response.xml")).andReturn(resource);
		
		XPathExpressionResolver resolver = new XPathExpressionResolver();
		resolver.setNamespaceMap(Collections.singletonMap("ns", "http://www.example.org/schema"));
		
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		Map<String, String[]> discriminators = new HashMap<String, String[]>();
		discriminators.put("test",new String[]{"//ns:text","//ns:number"});
		lookup.setDiscriminators(discriminators);
		lookup.setResourceLoader(resourceLoader);
		lookup.setExpressionResolver(resolver);
		
		replay(resourceLoader);
		
		Resource result = lookup.lookupResource(TEST_URI, getValidMessage());
		assertSame(resource, result);
		
		verify(resourceLoader);
	}
	@Test
	public void testUnknownPayload() throws IOException
	{
		ResourceLoader resourceLoader = createMock(ResourceLoader.class);
		Resource resource = new ClassPathResource("mock-responses/test/default-response.xml");
		expect(resourceLoader.getResource("mock-xml/test/response.xml")).andReturn(resource);
		
		XPathExpressionResolver resolver = new XPathExpressionResolver();
		resolver.setNamespaceMap(Collections.singletonMap("ns", "http://www.example.org/schema"));
		
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		Map<String, String[]> discriminators = new HashMap<String, String[]>();
		discriminators.put("known",new String[]{"//ns:text","//ns:number"});
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
		
		assertEquals("mock-xml/test/text-response.xml",lookup.getResourceName(TEST_URI, "test", new String[]{"//ns:text","garbage"}, 2, getXmlUtil().loadDocument(getValidMessage())));
		assertEquals("mock-xml/test/text-response.xml",lookup.getResourceName(TEST_URI, "test", new String[]{"garbage","//ns:text"}, 2, getXmlUtil().loadDocument(getValidMessage())));
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
		
		assertEquals("mock-request/test-localhost_text_request.xml",lookup.getResourceName(TEST_URI, "test", new String[]{"$uri.host","//ns:text"}, 2, getXmlUtil().loadDocument(getValidMessage())));
	}
	@Test
	public void testPrependUri() throws IOException
	{
		XPathExpressionResolver resolver = new XPathExpressionResolver();
		resolver.setNamespaceMap(Collections.singletonMap("ns", "http://www.example.org/schema"));
		
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		lookup.setExpressionResolver(resolver);
		lookup.setPathPrefix("mock-request/");
		lookup.setPathSuffix("request.xml");
		lookup.setPrependUri(true);
		
		assertEquals("mock-request/localhost/test/text-request.xml",lookup.getResourceName(TEST_URI, "test", new String[]{"//ns:text"}, 1, getXmlUtil().loadDocument(getValidMessage())));
	}

}
