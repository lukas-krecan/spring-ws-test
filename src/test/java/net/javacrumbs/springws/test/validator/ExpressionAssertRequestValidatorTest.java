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

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.expression.ExpressionResolver;

import org.junit.Test;
import org.w3c.dom.Document;


public class ExpressionAssertRequestValidatorTest extends AbstractValidatorTest{

	@Test
	public void testFail() throws IOException
	{
		ExpressionAssertRequestValidator validator = new ExpressionAssertRequestValidator();
		String assertion = "//ns:number=1";
		validator.setAssertExpression(assertion);
		ExpressionResolver expressionResolver = createMock(ExpressionResolver.class);
		expect(expressionResolver.resolveExpression(eq(assertion), (URI)isNull(), (Document)anyObject())).andReturn("false");
		validator.setExpressionResolver(expressionResolver);
		
		replay(expressionResolver);
		
		try
		{
			validator.processRequest(null, null, getValidMessage());
			fail("exception expected");
		}
		catch(WsTestException e)
		{
			assertTrue(e.getMessage().contains("Source message:"));
		}
	}
	@Test
	public void testOk() throws IOException
	{
		ExpressionAssertRequestValidator validator = new ExpressionAssertRequestValidator();
		String assertion = "//ns:number=0";
		validator.setAssertExpression(assertion);
		ExpressionResolver expressionResolver = createMock(ExpressionResolver.class);
		expect(expressionResolver.resolveExpression(eq(assertion), (URI)isNull(), (Document)anyObject())).andReturn("true");
		validator.setExpressionResolver(expressionResolver);
		
		replay(expressionResolver);
		
		validator.processRequest(null, null, getValidMessage());
		
		verify(expressionResolver);
	}
}
