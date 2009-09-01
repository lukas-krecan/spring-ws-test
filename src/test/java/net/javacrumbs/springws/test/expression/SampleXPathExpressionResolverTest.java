/**
 * Copyright 2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.springws.test.expression;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.context.WsTestContextHolder;

import org.junit.Test;
import org.w3c.dom.Document;

public class SampleXPathExpressionResolverTest extends AbstractMessageTest{

	@Test
	public void testEvaluateBody() throws Exception
	{
		String xpath = "concat(local-name(//soapenv:Body/*[1]),'-response.xml')";
		String result = resolveXPath(xpath);
		assertEquals("GetFlightsRequest-response.xml",result);
	}
	@Test
	public void testEvaluateDirectory() throws Exception
	{
		String xpath = "concat(local-name(//soapenv:Body/*[1]),'/default-response.xml')";
		String result = resolveXPath(xpath);
		assertEquals("GetFlightsRequest/default-response.xml",result);
	}
	@Test
	public void testEvaluateTargetDestination() throws Exception
	{
		String xpath = "concat(local-name(//soapenv:Body/*[1]),'/',//ns:from,'-',//ns:to,'-response.xml')";
		String result = resolveXPath(xpath);
		assertEquals("GetFlightsRequest/PRG-DUB-response.xml",result);
	}
	@Test
	public void testEvaluateUri() throws Exception
	{
		String xpath = "concat($uri.host, '/', local-name(//soapenv:Body/*[1]),'/',//ns:from,'-',//ns:to,'-response.xml')";
		String result = resolveXPath(xpath);
		assertEquals("www.csa.cz/GetFlightsRequest/PRG-DUB-response.xml",result);
	}
	@Test
	public void testContextVariable() throws Exception
	{
		WsTestContextHolder.getTestContext().setAttribute("testName", "test136");
		String xpath = "concat($context.testName,'-response.xml')";
		String result = resolveXPath(xpath);
		assertEquals("test136-response.xml",result);
	}


	private String resolveXPath(String xpath) throws URISyntaxException, IOException {
		XPathExpressionResolver evaluator = new XPathExpressionResolver();
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("ns", "http://www.springframework.org/spring-ws/samples/airline/schemas/messages");
		namespaceMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		evaluator.setNamespaceMap(namespaceMap);
		
		URI uri = new URI("http://www.csa.cz");
		
		String expression = xpath;
		Document document = getXmlUtil().loadDocument(createMessage("xml/PRG-DUB-request.xml"));
		String result = evaluator.resolveExpression(expression, uri, document);
		return result;
	}
}
