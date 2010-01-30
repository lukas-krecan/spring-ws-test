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
package net.javacrumbs.springws.test.validator;

import java.io.IOException;
import java.net.URI;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.expression.ExpressionResolver;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Fails if expression is evaluated to false.
 * @author Lukas Krecan
 *
 */
public class ExpressionAssertRequestValidator implements RequestProcessor {

	private static final String FALSE = Boolean.FALSE.toString();

	private String assertExpression;
	
	private ExpressionResolver expressionResolver;
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory, WebServiceMessage request)
			throws IOException {
		if (FALSE.equals(expressionResolver.resolveExpression(assertExpression, uri, xmlUtil.loadDocument(request))))
		{
			throw new WsTestException("Expression \""+assertExpression+"\" is not valid");
		}
		return null;
	}

	public String getAssertExpression() {
		return assertExpression;
	}

	public void setAssertExpression(String expression) {
		this.assertExpression = expression;
	}

	public ExpressionResolver getExpressionResolver() {
		return expressionResolver;
	}

	public void setExpressionResolver(ExpressionResolver expressionResolver) {
		this.expressionResolver = expressionResolver;
	}

	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

}
