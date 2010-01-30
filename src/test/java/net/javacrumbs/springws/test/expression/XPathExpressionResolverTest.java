/**
 * Copyright 2009-2010 the original author or authors.
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

import org.junit.Test;
import org.springframework.xml.xpath.XPathException;
import org.w3c.dom.Document;

public class XPathExpressionResolverTest extends AbstractMessageTest{

	@Test(expected=XPathException.class)
	public void testException() throws Exception
	{
		String xpath = "XXXconcat(local-name(//soapenv:Body/*[1]),'-response.xml')";
		resolveXPath(xpath);
	}
	@Test
	public void testEvaluateBody() throws Exception
	{
		String xpath = "concat(local-name(//soapenv:Body/*[1]),'-response.xml')";
		String result = resolveXPath(xpath);
		assertEquals("test-response.xml",result);
	}
	@Test
	public void testEvaluateString() throws Exception
	{
		String xpath = "'test-response.xml'";
		String result = resolveXPath(xpath);
		assertEquals("test-response.xml",result);
	}
	@Test
	public void testBodyAsDirectory() throws Exception
	{
		String xpath = "concat(local-name(//soapenv:Body/*[1]),'/default-response.xml')";
		String result = resolveXPath(xpath);
		assertEquals("test/default-response.xml",result);
	}
//	@Test
//	public void testPayloadRootName() throws Exception
//	{
//		String xpath = "concat($payloadRootName,'/default-response.xml')";
//		String result = resolveXPath(xpath);
//		assertEquals("test/default-response.xml",result);
//	}
	@Test
	public void testEvaluateComplex() throws Exception
	{
		String xpath = "concat('mock-responses/',$uri.host,'/',local-name(//soapenv:Body/*[1]),'/',//ns:text,'-response.xml')";
		String result = resolveXPath(xpath);
		assertEquals("mock-responses/www.example.org/test/text-response.xml",result);
	}

	private String resolveXPath(String xpath) throws URISyntaxException, IOException {
		XPathExpressionResolver evaluator = new XPathExpressionResolver();
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("ns", "http://www.example.org/schema");
		namespaceMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		evaluator.setNamespaceMap(namespaceMap);
		
		URI uri = new URI("http://www.example.org/service/");
		
		String expression = xpath;
		Document document = getXmlUtil().loadDocument(createMessage("xml/valid-message.xml"));
		String result = evaluator.resolveExpression(expression, uri, document);
		return result;
	}
}
