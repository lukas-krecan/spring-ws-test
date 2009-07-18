package net.krecan.springws.test.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import net.krecan.springws.test.AbstractMessageTest;
import net.krecan.springws.test.MockWebServiceMessageSender;
import net.krecan.springws.test.expression.ExpressionEvaluator;
import net.krecan.springws.test.expression.XPathExpressionEvaluator;
import net.krecan.springws.test.generator.DefaultResponseGenerator;
import net.krecan.springws.test.generator.ResponseGenerator;
import net.krecan.springws.test.lookup.DefaultResourceLookup;

import org.junit.Test;
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
		ExpressionEvaluator expressionEvaluator = resourceLookup.getExpressionEvaluator();
		assertNotNull(expressionEvaluator);
		assertNotNull(((XPathExpressionEvaluator)expressionEvaluator).getNamespaceContext());
		
		WebServiceConnection connection = sender.createConnection(null);
		connection.send(createMessage("xml/valid-message.xml"));
		WebServiceMessage response = connection.receive(messageFactory);
		assertNotNull(response);
	}
}
