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
import net.javacrumbs.springws.test.validator.ExpressionAssertRequestValidator;
import net.javacrumbs.springws.test.validator.XPathRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceMessageSender;

public class WsMockControl {
	private final List<LimitingRequestProcessor> requestProcessors = new ArrayList<LimitingRequestProcessor>();
	
	public WebServiceMessageSender createMock() {
		MockWebServiceMessageSender messageSender = new MockWebServiceMessageSender();
		messageSender.setRequestProcessors(requestProcessors);
		return messageSender;
	}

	public WsMockControl addRequestProcessor(RequestProcessor requestProcessor)
	{
		return addRequestProcessor(requestProcessor, requestProcessor.toString());
	}
	
	public WsMockControl addRequestProcessor(RequestProcessor requestProcessor, String requestProcessorDescription)
	{
		if (requestProcessor instanceof LimitingRequestProcessor)
		{
			requestProcessors.add((LimitingRequestProcessor)requestProcessor);
		}
		else
		{
			requestProcessors.add(new LimitingRequestProcessorWrapper(requestProcessor, requestProcessorDescription));
		}
		return this;
	}
	

	public WsMockControl expectRequest(String resourceName) {
		XmlCompareRequestValidator validator = new XmlCompareRequestValidator();
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setResourceExpressions("'"+resourceName+"'");
		validator.setControlResourceLookup(resourceLookup);
		validator.setFailIfControlResourceNotFound(true);
		addRequestProcessor(validator, "expectRequest(\""+resourceName+"\")");
		return this;
	}
	public WsMockControl failIf(String expression, Map<String, String> namespaceMap) {
		XPathRequestValidator validator = new XPathRequestValidator();
		validator.setExceptionMapping(Collections.singletonMap(expression, "XPath assertion \""+expression+"\" failed."));
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		validator.setExpressionResolver(expressionResolver);
		addRequestProcessor(validator, "failIf(\""+expression+"\")");
		return this;
	}
	public WsMockControl assertThat(String expression, Map<String, String> namespaceMap) {
		ExpressionAssertRequestValidator validator = new ExpressionAssertRequestValidator();
		validator.setAssertExpression(expression);
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		validator.setExpressionResolver(expressionResolver);
		addRequestProcessor(validator, "assertThat(\""+expression+"\")");
		return this;
	}

	public WsMockControl returnResponse(String resourceName) {
		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setResourceExpressions("'"+resourceName+"'");
		responseGenerator.setResourceLookup(resourceLookup);
		addRequestProcessor(responseGenerator, "returnResponse(\""+resourceName+"\")");
		return this;
	}

	public WsMockControl throwException(final RuntimeException exception) {
		RequestProcessor thrower = new RequestProcessor()
		{
			public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory,	WebServiceMessage request) throws IOException {
				throw exception;
			}
		};
		addRequestProcessor(thrower, "throwException(\""+exception.getMessage()+"\")");
		return this;
	}

	List<LimitingRequestProcessor> getRequestProcessors() {
		return requestProcessors;
	}

	public WsMockControl times(int min, int max) {
		if (requestProcessors.isEmpty())
		{
			throw new IllegalStateException("Can not set behaviour. No request processor defined.");
		}
		LimitingRequestProcessor lastProcessor = getLastProcessor();
		lastProcessor.setMinNumberOfProcessedRequests(min);
		lastProcessor.setMaxNumberOfProcessedRequests(max);
		return this; 
	}

	public WsMockControl times(int count) {
		return times(count, count); 		
	}

	public WsMockControl once() {
		return times(1);
	}

	public WsMockControl anyTimes() {
		return times(0,Integer.MAX_VALUE);
	}

	public WsMockControl atLeastOnce() {
		return times(1,Integer.MAX_VALUE);
	}

	private LimitingRequestProcessor getLastProcessor() {
		return requestProcessors.get(requestProcessors.size()-1);
	}






	
	

}
