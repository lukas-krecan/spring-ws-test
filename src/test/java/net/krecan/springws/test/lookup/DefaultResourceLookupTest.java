package net.krecan.springws.test.lookup;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;

import net.krecan.springws.test.AbstractMessageTest;
import net.krecan.springws.test.expression.ExpressionEvaluator;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

public class DefaultResourceLookupTest extends AbstractMessageTest{

	
	@Test
	public void testNoExpressions() throws IOException
	{
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		
		assertNull(resourceLookup.lookupResource(null, createMessage("xml/valid-message.xml")));
	}
	@Test
	public void testFirstExpression() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		String responsePath = "mock-responses/test/default-response.xml";

		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setResourceExpressions(new String[]{"expr1","expr2"});
		
		ExpressionEvaluator evaluator = createMock(ExpressionEvaluator.class);
		expect(evaluator.evaluateExpression(eq("expr1"), (URI)isNull(), (Document)anyObject())).andReturn(responsePath);
		resourceLookup.setExpressionEvaluator(evaluator);

		replay(evaluator);
				
		Resource resource = resourceLookup.lookupResource(null, request);
		assertEquals(new ClassPathResource(responsePath), resource);
		verify(evaluator);
	}
	@Test
	public void testSecondExpression() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		String responsePath = "mock-responses/test/default-response.xml";
		
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setResourceExpressions(new String[]{"expr1","expr2"});
		
		ExpressionEvaluator evaluator = createMock(ExpressionEvaluator.class);
		expect(evaluator.evaluateExpression(eq("expr1"), (URI)isNull(), (Document)anyObject())).andReturn("wrongPath");
		expect(evaluator.evaluateExpression(eq("expr2"), (URI)isNull(), (Document)anyObject())).andReturn(responsePath);
		resourceLookup.setExpressionEvaluator(evaluator);
		
		replay(evaluator);
		
		Resource resource = resourceLookup.lookupResource(null, request);
		assertEquals(new ClassPathResource(responsePath), resource);
		verify(evaluator);
	}
	@Test
	public void testNoExpression() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setResourceExpressions(new String[]{"expr1","expr2"});
		
		ExpressionEvaluator evaluator = createMock(ExpressionEvaluator.class);
		expect(evaluator.evaluateExpression(eq("expr1"), (URI)isNull(), (Document)anyObject())).andReturn("wrongPath");
		expect(evaluator.evaluateExpression(eq("expr2"), (URI)isNull(), (Document)anyObject())).andReturn("wrongPath2");
		resourceLookup.setExpressionEvaluator(evaluator);
		
		replay(evaluator);
		
		assertNull(resourceLookup.lookupResource(null, request));
		verify(evaluator);
	}
}
