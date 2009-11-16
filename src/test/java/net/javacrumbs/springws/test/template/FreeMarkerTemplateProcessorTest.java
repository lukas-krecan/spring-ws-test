package net.javacrumbs.springws.test.template;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.context.WsTestContextHolder;

import org.custommonkey.xmlunit.Diff;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;


public class FreeMarkerTemplateProcessorTest extends AbstractMessageTest {
	private FreeMarkerTemplateProcessor processor;
	
	@Before
	public void setUp() throws Exception
	{
		processor = new FreeMarkerTemplateProcessor();
		processor.setResourceLoader(new DefaultResourceLoader());
		processor.afterPropertiesSet();
	}
	
	@Test
	public void testTemplate() throws IOException
	{
		WsTestContextHolder.getTestContext().setAttribute("number", 2);
		WebServiceMessage request = createMessage("xml/valid-message2.xml");
		ClassPathResource template = new ClassPathResource("mock-responses/test/freemarker-response.xml");
		Resource resource = processor.processTemplate(template, null, request);
		
		
		Document controlDocument = getXmlUtil().loadDocument(new ClassPathResource("xml/resolved-different-response.xml"));
		Document responseDocument = getXmlUtil().loadDocument(resource);
		Diff diff = new Diff(controlDocument, responseDocument);
		assertTrue(diff.toString(), diff.similar());
		
		WsTestContextHolder.getTestContext().clear();
	}
}
