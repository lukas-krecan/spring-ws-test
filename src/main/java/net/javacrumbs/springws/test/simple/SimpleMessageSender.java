package net.javacrumbs.springws.test.simple;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.expression.XPathExpressionResolver;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;
import net.javacrumbs.springws.test.validator.XPathRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceMessageSender;

public class SimpleMessageSender {
	private final List<RequestProcessor> requestProcessors = new ArrayList<RequestProcessor>();
	
	private WebServiceMessageSender create() {
		MockWebServiceMessageSender messageSender = new MockWebServiceMessageSender();
		messageSender.setRequestProcessors(requestProcessors);
		return messageSender;
	}

	public SimpleMessageSender addRequestProcessor(RequestProcessor requestProcessor)
	{
		requestProcessors.add(requestProcessor);
		return this;
	}
	

	public SimpleMessageSender expectRequest(String resourceName) {
		XmlCompareRequestValidator validator = new XmlCompareRequestValidator();
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setResourceExpressions("'"+resourceName+"'");
		validator.setControlResourceLookup(resourceLookup);
		addRequestProcessor(validator);
		return this;
	}
	public SimpleMessageSender failIf(String expression, Map<String, String> namespaceMap) {
		XPathRequestValidator validator = new XPathRequestValidator();
		validator.setExceptionMapping(Collections.singletonMap(expression, "XPath assertion \""+expression+"\" failed."));
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		validator.setExpressionResolver(expressionResolver);
		addRequestProcessor(validator);
		return this;
	}

	public WebServiceMessageSender andReturnResponse(String resourceName) {
		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setResourceExpressions("'"+resourceName+"'");
		responseGenerator.setResourceLookup(resourceLookup);
		addRequestProcessor(responseGenerator);
		return create();
	}

	public WebServiceMessageSender andThrow(final RuntimeException exception) {
		RequestProcessor thrower = new RequestProcessor()
		{
			public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory,	WebServiceMessage request) throws IOException {
				throw exception;
			}
		};
		addRequestProcessor(thrower);
		return create();
	}

	
	

}
