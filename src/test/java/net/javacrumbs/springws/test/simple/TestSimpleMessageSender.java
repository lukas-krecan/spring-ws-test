package net.javacrumbs.springws.test.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.expression.ExpressionResolver;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.junit.Test;


public class TestSimpleMessageSender extends AbstractMessageTest{
	
	@Test
	public void testExpectAndReturn()
	{
		MockWebServiceMessageSender sender = (MockWebServiceMessageSender)SimpleMessageFactory.expectRequest("xml/control-message-test.xml").create();
		assertNotNull(sender);
		assertEquals(1, sender.getResponseGenerators().size());
		
		ExpressionResolver resolver = ((DefaultResourceLookup)((XmlCompareRequestValidator)sender.getResponseGenerators().get(0)).getControlResourceLookup()).getExpressionResolver();
		assertEquals("xml/control-message-test.xml", resolver.resolveExpression(null, null, null));
		
	}
	
	
}
