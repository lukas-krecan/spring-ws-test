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

package net.javacrumbs.springws.test.helper;

import java.util.Collections;

import javax.xml.transform.Source;

import net.javacrumbs.springws.test.common.XPathExpressionEvaluator;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;

import org.springframework.ws.server.endpoint.PayloadEndpoint;
import org.springframework.xml.namespace.SimpleNamespaceContext;
import org.springframework.xml.transform.StringSource;
import org.w3c.dom.Document;

public class DummyEndpoint implements PayloadEndpoint {

	public Source invoke(Source request) throws Exception {
		if (!shoulReturnError(request))
		{
			return new StringSource("<test xmlns=\"http://www.example.org/schema\"><number>0</number><text>text</text></test>");	
		}
		else
		{
			throw new RuntimeException("Test exception, do not panic.");
		}
		
	}

	private boolean shoulReturnError(Source request) {
		XPathExpressionEvaluator evaluator = new XPathExpressionEvaluator();
		Document document = DefaultXmlUtil.getInstance().loadDocument(request);
		SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();
		namespaceContext.setBindings(Collections.singletonMap("ns", "http://www.example.org/schema"));
		return "true".equals(evaluator.evaluateExpression(document, "//ns:number='not-number'", null, namespaceContext));
	}

}
