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

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import net.javacrumbs.springws.test.ResponseGenerator;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Iterates through expressions mapped by exceptionMapping, if the expression is evaluated as true on given request, 
 * method {@link #expressionValid(String, String)} is called.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractExpressionEvaluator implements ResponseGenerator {

	private static final String TRUE = Boolean.TRUE.toString();
	
	private int order;
	private ExpressionResolver expressionResolver;
	private Map<String, String> exceptionMapping;
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();

	public AbstractExpressionEvaluator() {
		super();
	}

	/**
	 * Iterates through expressions mapped by exceptionMapping, if the expression is evaluated as true on given request, 
     * method {@link #expressionValid(String, String)} is called.
	 */
	public WebServiceMessage generateResponse(URI uri, WebServiceMessageFactory messageFactory, WebServiceMessage request) throws IOException {
		for (Map.Entry<String, String> entry: exceptionMapping.entrySet())
		{
			String result = expressionResolver.resolveExpression(entry.getKey(), uri, xmlUtil.loadDocument(request));
			if (TRUE.equals(result))
			{
				expressionValid(entry.getKey(), entry.getValue());
			}
		}
		return null;
	}
	
	/**
	 * Method called if any XPath expression is evaluated as "true".
	 * @param expression
	 * @param errorMessage
	 */
	protected abstract void expressionValid(String expression, String errorMessage);

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public ExpressionResolver getExpressionResolver() {
		return expressionResolver;
	}

	public void setExpressionResolver(ExpressionResolver resolver) {
		this.expressionResolver = resolver;
	}

	public Map<String, String> getExceptionMapping() {
		return exceptionMapping;
	}

	public void setExceptionMapping(Map<String, String> exceptionMapping) {
		this.exceptionMapping = exceptionMapping;
	}

	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

}