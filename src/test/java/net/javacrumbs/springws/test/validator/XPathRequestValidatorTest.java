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
package net.javacrumbs.springws.test.validator;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.expression.AbstractExpressionEvaluator;
import net.javacrumbs.springws.test.expression.ExpressionResolver;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;


public class XPathRequestValidatorTest extends AbstractValidatorTest{

	private static final String ERROR_MESSAGE = "Error message";

	@Test
	public void testValidationFailsOnFirst() throws IOException
	{
		WebServiceMessage message = getValidMessage();
		String expression1 = "//ns:number = 0";
		Map<String, String> expressionMap = Collections.singletonMap(expression1, ERROR_MESSAGE+"1");
		
		AbstractExpressionEvaluator validator = new XPathRequestValidator();
		ExpressionResolver resolver = createMock(ExpressionResolver.class);
		validator.setExpressionResolver(resolver);
		
		validator.setExceptionMapping(expressionMap);
		expect(resolver.resolveExpression(eq(expression1), (URI)anyObject(), (Document)anyObject())).andReturn("true");
		
		replay(resolver);
		
		try
		{
			validator.processRequest(null, messageFactory, message);
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			assertEquals(ERROR_MESSAGE+"1",e.getMessage());
		}
		verify(resolver);
	}
	@Test
	public void testValidationFailsOnSecond() throws IOException
	{
		WebServiceMessage message = getValidMessage();
		String expression1 = "//ns:number = 0";
		String expression2 = "//ns:text = 'Hi'";
		Map<String, String> expressionMap = new HashMap<String, String>(); 
		expressionMap.put(expression1, ERROR_MESSAGE+"1");
		expressionMap.put(expression2, ERROR_MESSAGE+"2");
		
		AbstractExpressionEvaluator validator = new XPathRequestValidator();
		ExpressionResolver resolver = createMock(ExpressionResolver.class);
		validator.setExpressionResolver(resolver);
		
		validator.setExceptionMapping(expressionMap);
		expect(resolver.resolveExpression(eq(expression1), (URI)anyObject(), (Document)anyObject())).andReturn("false");
		expect(resolver.resolveExpression(eq(expression2), (URI)anyObject(), (Document)anyObject())).andReturn("true");
		
		replay(resolver);
		
		try
		{
			validator.processRequest(null, messageFactory, message);
			fail("Exception expected");
		}
		catch(WsTestException e)
		{
			assertEquals(ERROR_MESSAGE+"2",e.getMessage());
		}
		verify(resolver);
	}
	@Test
	public void testValidationOk() throws IOException
	{
		WebServiceMessage message = getValidMessage();
		String expression1 = "//ns:number = 0";
		String expression2 = "//ns:text = 'Hi'";
		Map<String, String> expressionMap = new HashMap<String, String>(); 
		expressionMap.put(expression1, ERROR_MESSAGE+"1");
		expressionMap.put(expression2, ERROR_MESSAGE+"2");
		
		AbstractExpressionEvaluator validator = new XPathRequestValidator();
		ExpressionResolver resolver = createMock(ExpressionResolver.class);
		validator.setExpressionResolver(resolver);
		
		validator.setExceptionMapping(expressionMap);
		expect(resolver.resolveExpression(eq(expression1), (URI)anyObject(), (Document)anyObject())).andReturn("false");
		expect(resolver.resolveExpression(eq(expression2), (URI)anyObject(), (Document)anyObject())).andReturn("false");
		
		replay(resolver);
	
		validator.processRequest(null, messageFactory, message);
		
		verify(resolver);
	}
}
