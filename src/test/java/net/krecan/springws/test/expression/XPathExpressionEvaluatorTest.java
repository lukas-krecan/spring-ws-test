package net.krecan.springws.test.expression;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import net.krecan.springws.test.AbstractMessageTest;

import org.junit.Test;
import org.w3c.dom.Document;

public class XPathExpressionEvaluatorTest extends AbstractMessageTest{

	@Test
	public void testEvaluate() throws Exception
	{
		XPathExpressionEvaluator evaluator = new XPathExpressionEvaluator();
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("ns", "http://www.example.org/schema");
		namespaceMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		evaluator.setNamespaceMap(namespaceMap);
		
		URI uri = new URI("http://www.example.org/service/");
		
		String expression = "concat('mock-responses/',$uri.host,'/',name(//soapenv:Body/*[1]),'/',//ns:text,'-response.xml')";
		Document document = getXmlUtil().loadDocument(createMessage("xml/valid-message.xml"));
		String result = evaluator.evaluateExpression(expression, uri, document);
		assertEquals("mock-responses/www.example.org/test/text-response.xml",result);
	}
}