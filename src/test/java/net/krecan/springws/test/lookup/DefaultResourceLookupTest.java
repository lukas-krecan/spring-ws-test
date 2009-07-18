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
import net.krecan.springws.test.expression.ExpressionResolver;

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
		
		ExpressionResolver resolver = createMock(ExpressionResolver.class);
		expect(resolver.resolveExpression(eq("expr1"), (URI)isNull(), (Document)anyObject())).andReturn(responsePath);
		resourceLookup.setExpressionResolver(resolver);

		replay(resolver);
				
		Resource resource = resourceLookup.lookupResource(null, request);
		assertEquals(new ClassPathResource(responsePath), resource);
		verify(resolver);
	}
	@Test
	public void testSecondExpression() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		String responsePath = "mock-responses/test/default-response.xml";
		
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setResourceExpressions(new String[]{"expr1","expr2"});
		
		ExpressionResolver resolver = createMock(ExpressionResolver.class);
		expect(resolver.resolveExpression(eq("expr1"), (URI)isNull(), (Document)anyObject())).andReturn("wrongPath");
		expect(resolver.resolveExpression(eq("expr2"), (URI)isNull(), (Document)anyObject())).andReturn(responsePath);
		resourceLookup.setExpressionResolver(resolver);
		
		replay(resolver);
		
		Resource resource = resourceLookup.lookupResource(null, request);
		assertEquals(new ClassPathResource(responsePath), resource);
		verify(resolver);
	}
	@Test
	public void testNoExpression() throws IOException
	{
		WebServiceMessage request = createMessage("xml/valid-message.xml");
		
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setResourceExpressions(new String[]{"expr1","expr2"});
		
		ExpressionResolver resolve = createMock(ExpressionResolver.class);
		expect(resolve.resolveExpression(eq("expr1"), (URI)isNull(), (Document)anyObject())).andReturn("wrongPath");
		expect(resolve.resolveExpression(eq("expr2"), (URI)isNull(), (Document)anyObject())).andReturn("wrongPath2");
		resourceLookup.setExpressionResolver(resolve);
		
		replay(resolve);
		
		assertNull(resourceLookup.lookupResource(null, request));
		verify(resolve);
	}
}
