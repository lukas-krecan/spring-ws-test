package net.krecan.springws.test.lookup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.krecan.springws.test.AbstractMessageTest;
import net.krecan.springws.test.context.WsTestContextHolder;
import net.krecan.springws.test.util.DefaultXmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;


public class TemplateProcessingXPathResourceLookupTest extends AbstractMessageTest {
	private TemplateProcessingXPathResourceLookup resourceLookup;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public TemplateProcessingXPathResourceLookupTest() {
		
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("ns", "http://www.example.org/schema");
		namespaceMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		
		
		resourceLookup = new TemplateProcessingXPathResourceLookup();
		String resourceXpath = "concat('mock-responses/',name(//soapenv:Body/*[1]),'/',//ns:text,'-response.xml')";
				
		String defaultXPath = "concat('mock-responses/',name(//soapenv:Body/*[1]),'/default-response.xml')";
		resourceLookup.setResourceExpressions(new String[]{resourceXpath, defaultXPath});
		resourceLookup.setNamespaceMap(namespaceMap);
	}
	
	@Test
	public void testTemplate() throws IOException
	{
		WsTestContextHolder.getTestContext().setAttribute("number", 2);
		
		WebServiceMessage request = createMessage("xml/valid-message2.xml");
		Resource resource = resourceLookup.lookupResource(null, request);
		
		
		Document controlDocument = loadDocument(new ClassPathResource("xml/resolved-different-response.xml"));
		Document responseDocument = loadDocument(resource);
		logger.trace("Comparing "+serializeDocument(controlDocument)+"\n to \n"+serializeDocument(responseDocument));
		Diff diff = new Diff(controlDocument, responseDocument);
		assertTrue(diff.toString(), diff.similar());
		
		WsTestContextHolder.getTestContext().clear();
	}
	private String serializeDocument(Document document) {
		return DefaultXmlUtil.getInstance().serializeDocument(document); 
	}

	private Document loadDocument(Resource resource) throws IOException {
		return DefaultXmlUtil.getInstance().loadDocument(resource); 
	}

	@Test
	public void testIsTemplate() throws IOException
	{
		Resource resource = new ClassPathResource("mock-responses/test/different-response.xml");
		assertTrue(resourceLookup.isTemplate(resource));		
	}
	@Test
	public void testIsNotTemplate() throws IOException
	{
		Resource resource = new ClassPathResource("mock-responses/test/default-response.xml");
		assertFalse(resourceLookup.isTemplate(resource));		
	}
	@Test
	public void testNormalXml() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		Resource resource = resourceLookup.lookupResource(null, request);
		
		
		Document controlDocument = loadDocument(new ClassPathResource("mock-responses/test/default-response.xml"));
		Document responseDocument = loadDocument(resource);
		logger.trace("Comparing "+serializeDocument(controlDocument)+"\n to \n"+serializeDocument(responseDocument));
		Diff diff = new Diff(controlDocument, responseDocument);
		assertTrue(diff.toString(), diff.similar());
		
	}
	
	
}
