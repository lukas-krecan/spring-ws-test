package net.javacrumbs.springws.test.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.expression.ExpressionResolver;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.junit.Test;


public class TestSimpleMessageSender extends AbstractMessageTest{
	
	@Test
	public void testExpectAndReturn()
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)new SimpleMessageFactory().expectRequest("xml/control-message-test.xml").andReturnResponse("mock-responses/test/default-response.xml");
		assertNotNull(sender);
		assertEquals(2, sender.getResponseGenerators().size());
		
		ExpressionResolver resolver1 = ((DefaultResourceLookup)((XmlCompareRequestValidator)sender.getResponseGenerators().get(0)).getControlResourceLookup()).getExpressionResolver();
		assertEquals("xml/control-message-test.xml", resolver1.resolveExpression(null, null, null));
		
		ExpressionResolver resolver2 = ((DefaultResourceLookup)((DefaultResponseGenerator)sender.getResponseGenerators().get(1)).getResourceLookup()).getExpressionResolver();
		assertEquals("mock-responses/test/default-response.xml", resolver2.resolveExpression(null, null, null));
		
	}
	
	
}
