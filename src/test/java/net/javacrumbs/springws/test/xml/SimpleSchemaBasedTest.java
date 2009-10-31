package net.javacrumbs.springws.test.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;
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
		
		assertEquals(2, requestProcessors.size());
		
		XmlCompareRequestValidator xmlCompareValidator = (XmlCompareRequestValidator) requestProcessors.get(0);
		PayloadRootBasedResourceLookup resourceLookup = (PayloadRootBasedResourceLookup) xmlCompareValidator.getControlResourceLookup();
		assertEquals("request.xml",resourceLookup.getPathSuffix());

		SchemaRequestValidator schemaValidator = (SchemaRequestValidator) requestProcessors.get(1);
		assertEquals(1, schemaValidator.getSchemas().length);

	}
}
