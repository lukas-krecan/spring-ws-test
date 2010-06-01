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

import java.net.URI;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

import net.javacrumbs.springws.test.common.ExpressionEvaluator;
import net.javacrumbs.springws.test.common.XPathExpressionEvaluator;

import org.springframework.xml.namespace.SimpleNamespaceContext;
import org.w3c.dom.Document;

/**
 * Evaluates XPath expression.
 * @author Lukas Krecan
 *
 */
public class XPathExpressionResolver implements ExpressionResolver{
	

	private ExpressionEvaluator expressionEvaluator = new XPathExpressionEvaluator();

	private NamespaceContext namespaceContext;

	/**
	 * Evaluates an expression.
	 * 
	 * @param expression
	 * @param uri
	 * @param document
	 * @return
	 */
	public String resolveExpression(String expression, URI uri, Document document) {
		return expressionEvaluator.evaluateExpression(document, expression, uri, getNamespaceContext());
	}

	public void setNamespaceMap(Map<String, String> namespaceMap) {
		SimpleNamespaceContext context = new SimpleNamespaceContext();
		context.setBindings(namespaceMap);
		setNamespaceContext(context);
	}

	public NamespaceContext getNamespaceContext() {
		return namespaceContext;
	}

	public void setNamespaceContext(NamespaceContext namespaceContext) {
		this.namespaceContext = namespaceContext;
	}

}
