package net.javacrumbs.springws.test.validator;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.net.URI;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.expression.ExpressionResolver;

import org.junit.Test;
import org.w3c.dom.Document;


public class ExpressionAssertRequestValidatorTest extends AbstractValidatorTest{

	@Test(expected=WsTestException.class)
	public void testFail() throws IOException
	{
		ExpressionAssertRequestValidator validator = new ExpressionAssertRequestValidator();
		String assertion = "//ns:number=1";
		validator.setAssertExpression(assertion);
		ExpressionResolver expressionResolver = createMock(ExpressionResolver.class);
		expect(expressionResolver.resolveExpression(eq(assertion), (URI)isNull(), (Document)anyObject())).andReturn("false");
		validator.setExpressionResolver(expressionResolver);
		
		replay(expressionResolver);
		
		validator.processRequest(null, null, getValidMessage());
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
