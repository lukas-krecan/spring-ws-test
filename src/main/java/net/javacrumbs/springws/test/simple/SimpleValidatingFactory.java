package net.javacrumbs.springws.test.simple;

import java.util.Collections;
import java.util.List;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.ResponseGenerator;
import net.javacrumbs.springws.test.expression.SimpleExpressionResolver;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.springframework.ws.transport.WebServiceMessageSender;

public class SimpleValidatingFactory {
	private final SimpleValidatingFactory previousFactory;
	
	private final String resourceName;
	
	public SimpleValidatingFactory(String resourceName, SimpleValidatingFactory previousFactory)
	{
		this.resourceName = resourceName;
		this.previousFactory = previousFactory;
	}
	
	public WebServiceMessageSender create() {
		MockWebServiceMessageSender messageSender = new MockWebServiceMessageSender();
		messageSender.setResponseGenerators(getResponseGenerators());
		return messageSender;
	}

	private List<ResponseGenerator> getResponseGenerators() {
		ResponseGenerator generator = createResponseGenerator();
		return Collections.singletonList(generator);
	}

	private ResponseGenerator createResponseGenerator() {
		XmlCompareRequestValidator validator = new XmlCompareRequestValidator();
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setExpressionResolver(new SimpleExpressionResolver(resourceName));
		validator.setControlResourceLookup(resourceLookup);
		return validator;
	}
	
	

}
