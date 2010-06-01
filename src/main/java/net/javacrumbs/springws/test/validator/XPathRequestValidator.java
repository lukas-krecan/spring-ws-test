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


import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.expression.AbstractExpressionProcessor;

import org.springframework.core.Ordered;

/**
 * Validates request using given XPath expressions. If the expression is evaluated as true {@link WsTestException} is thrown.
 * @author Lukas Krecan
 *
 */
public class XPathRequestValidator extends AbstractExpressionProcessor implements RequestProcessor, Ordered {
	


	static final int DEFAULT_ORDER = 30;
	
	public XPathRequestValidator()
	{
		setOrder(DEFAULT_ORDER);
	}
	
	protected void expressionValid(String expression, String errorMessage) {
		throw new WsTestException(errorMessage);
	}



}
