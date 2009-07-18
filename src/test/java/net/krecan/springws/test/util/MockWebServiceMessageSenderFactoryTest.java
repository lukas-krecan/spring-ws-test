package net.krecan.springws.test.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import net.krecan.springws.test.AbstractMessageTest;
import net.krecan.springws.test.MockWebServiceMessageSender;
import net.krecan.springws.test.WsTestException;
import net.krecan.springws.test.expression.ExpressionResolver;
import net.krecan.springws.test.expression.XPathExpressionResolver;
import net.krecan.springws.test.generator.DefaultResponseGenerator;
import net.krecan.springws.test.generator.ResponseGenerator;
import net.krecan.springws.test.lookup.DefaultResourceLookup;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.transport.WebServiceConnection;


public class MockWebServiceMessageSenderFactoryTest extends AbstractMessageTest{

	@Test
	public void testInitGeneratorsOnly() throws Exception
	{
		MockWebServiceMessageSenderFactory factory = new MockWebServiceMessageSenderFactory();
		
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("ns", "http://www.example.org/schema");
		namespaceMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		factory.setNamespaceMap(namespaceMap );
		String[] responseXPathExpressions = new String[]{
				"concat('mock-responses/','/',name(//soapenv:Body/*[1]),'/',//ns:text,'-response.xml')",
				"concat('mock-responses/','/',name(//soapenv:Body/*[1]),'/','default-response.xml')"
		};
		factory.setResponseXPathExpressions(responseXPathExpressions);
		factory.afterPropertiesSet();
		
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) factory.getObject();
		assertNotNull(sender);
		ResponseGenerator[] generators = sender.getResponseGenerators();
		assertEquals(1, generators.length);
		DefaultResourceLookup resourceLookup = (DefaultResourceLookup)((DefaultResponseGenerator)generators[0]).getResourceLookup();
		assertNotNull(resourceLookup);
		assertArrayEquals(responseXPathExpressions, resourceLookup.getResourceExpressions());
		ExpressionResolver expressionResolver = resourceLookup.getExpressionResolver();
		assertNotNull(expressionResolver);
		assertNotNull(((XPathExpressionResolver)expressionResolver).getNamespaceContext());
		
		WebServiceConnection connection = sender.createConnection(null);
		connection.send(createMessage("xml/valid-message.xml"));
		WebServiceMessage response = connection.receive(messageFactory);
		assertNotNull(response);
	}
	@Test(expected=WsTestException.class)
	public void testInitSchemaValidator() throws Exception
	{
		MockWebServiceMessageSenderFactory factory = new MockWebServiceMessageSenderFactory();
		
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("ns", "http://www.example.org/schema");
		namespaceMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		factory.setNamespaceMap(namespaceMap );
		String[] responseXPathExpressions = new String[]{
				"concat('mock-responses/','/',name(//soapenv:Body/*[1]),'/',//ns:text,'-response.xml')",
				"concat('mock-responses/','/',name(//soapenv:Body/*[1]),'/','default-response.xml')"
		};
		factory.setResponseXPathExpressions(responseXPathExpressions);
		factory.setRequestValidationSchemas(new Resource[]{new ClassPathResource("xml/schema.xsd")});
		
		factory.afterPropertiesSet();
		
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) factory.getObject();
		assertNotNull(sender);
		ResponseGenerator[] generators = sender.getResponseGenerators();
		assertEquals(1, generators.length);
		DefaultResourceLookup resourceLookup = (DefaultResourceLookup)((DefaultResponseGenerator)generators[0]).getResourceLookup();
		assertNotNull(resourceLookup);
		assertArrayEquals(responseXPathExpressions, resourceLookup.getResourceExpressions());
		ExpressionResolver expressionResolver = resourceLookup.getExpressionResolver();
		assertNotNull(expressionResolver);
		assertNotNull(((XPathExpressionResolver)expressionResolver).getNamespaceContext());
		
		assertEquals(1,sender.getRequestValidators().length);
		
		WebServiceConnection connection = sender.createConnection(null);
		connection.send(createMessage("xml/invalid-message.xml"));
	}
	
	@Test
	public void testInitSchemaAndXmlValidator() throws Exception
	{
		MockWebServiceMessageSenderFactory factory = new MockWebServiceMessageSenderFactory();
		
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("ns", "http://www.example.org/schema");
		namespaceMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		factory.setNamespaceMap(namespaceMap );
		String[] responseXPathExpressions = new String[]{
				"concat('mock-responses/','/',name(//soapenv:Body/*[1]),'/',//ns:text,'-response.xml')",
				"concat('mock-responses/','/',name(//soapenv:Body/*[1]),'/','default-response.xml')"
		};
		factory.setResponseXPathExpressions(responseXPathExpressions);
		
		String[] controlRequestXPathExpressions = new String[]{
				"concat('xml/','/control-message-',name(//soapenv:Body/*[1]),'.xml')",
		};
		
		factory.setControlRequestXPathExpressions(controlRequestXPathExpressions);
		factory.setRequestValidationSchemas(new Resource[]{new ClassPathResource("xml/schema.xsd")});
		
		factory.afterPropertiesSet();
		
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender) factory.getObject();
		assertNotNull(sender);
		ResponseGenerator[] generators = sender.getResponseGenerators();
		assertEquals(1, generators.length);
		DefaultResourceLookup resourceLookup = (DefaultResourceLookup)((DefaultResponseGenerator)generators[0]).getResourceLookup();
		assertNotNull(resourceLookup);
		assertArrayEquals(responseXPathExpressions, resourceLookup.getResourceExpressions());
		ExpressionResolver expressionResolver = resourceLookup.getExpressionResolver();
		assertNotNull(expressionResolver);
		assertNotNull(((XPathExpressionResolver)expressionResolver).getNamespaceContext());
		
		assertEquals(2,sender.getRequestValidators().length);
		
		WebServiceConnection connection = sender.createConnection(null);
		connection.send(createMessage("xml/valid-message.xml"));
	}
}
