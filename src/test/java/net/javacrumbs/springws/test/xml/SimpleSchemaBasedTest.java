package net.javacrumbs.springws.test.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.PayloadRootBasedResourceLookup;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SimpleSchemaBasedTest {

	@Test
	public void testSimpleSchema()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("context/simple-schema-based-context.xml");
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) context.getBean("mock-sender");
		assertNotNull(sender);
		List<RequestProcessor> requestProcessors = sender.getRequestProcessors();
		assertNotNull(requestProcessors);
		
		assertEquals(3, requestProcessors.size());
		
		XmlCompareRequestValidator xmlCompareValidator = (XmlCompareRequestValidator) requestProcessors.get(0);
		PayloadRootBasedResourceLookup controlResourceLookup = (PayloadRootBasedResourceLookup) xmlCompareValidator.getControlResourceLookup();
		assertEquals("request.xml",controlResourceLookup.getPathSuffix());

		SchemaRequestValidator schemaValidator = (SchemaRequestValidator) requestProcessors.get(1);
		assertEquals(1, schemaValidator.getSchemas().length);

		DefaultResponseGenerator generator = (DefaultResponseGenerator) requestProcessors.get(2);
		PayloadRootBasedResourceLookup resourceLookup = (PayloadRootBasedResourceLookup)generator.getResourceLookup();
		assertEquals("response.xml",resourceLookup.getPathSuffix());

	}
}
