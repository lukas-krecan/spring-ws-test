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
package net.javacrumbs.springws.test.generator;


import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.expression.AbstractExpressionEvaluator;

import org.springframework.core.Ordered;
import org.springframework.ws.client.WebServiceTransportException;

/**
 * Applies given XPath expressions on request. If the expression is evaluated to true {@link WebServiceTransportException} is thrown.
 * @author Lukas Krecan
 *
 */
public class WebServiceTransportExceptionGenerator extends AbstractExpressionEvaluator implements RequestProcessor, Ordered {
	

	static final int DEFAULT_ORDER = 40;
	
	public WebServiceTransportExceptionGenerator()
	{
		setOrder(DEFAULT_ORDER);
	}
	
	protected void expressionValid(String expression, String errorMessage) {
		throw new WebServiceTransportException(errorMessage);
	}



}
