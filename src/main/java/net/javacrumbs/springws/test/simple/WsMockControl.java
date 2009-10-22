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
	private final List<VerifiableRequestProcessor> requestProcessors = new ArrayList<VerifiableRequestProcessor>();
	
	private WebServiceMessageSender create() {
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
		if (requestProcessor instanceof VerifiableRequestProcessor)
		{
			requestProcessors.add((VerifiableRequestProcessor)requestProcessor);
		}
		else
		{
			requestProcessors.add(new VerifiableRequestProcessorWrapper(requestProcessor, requestProcessorDescription));
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
		addRequestProcessor(validator);
		return this;
	}
	public WsMockControl assertThat(String expression, Map<String, String> namespaceMap) {
		ExpressionAssertRequestValidator validator = new ExpressionAssertRequestValidator();
		validator.setAssertExpression(expression);
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		validator.setExpressionResolver(expressionResolver);
		addRequestProcessor(validator);
		return this;
	}

	public WebServiceMessageSender returnResponse(String resourceName) {
		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setResourceExpressions("'"+resourceName+"'");
		responseGenerator.setResourceLookup(resourceLookup);
		addRequestProcessor(responseGenerator);
		return create();
	}

	public WebServiceMessageSender throwException(final RuntimeException exception) {
		RequestProcessor thrower = new RequestProcessor()
		{
			public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory,	WebServiceMessage request) throws IOException {
				throw exception;
			}
		};
		addRequestProcessor(thrower);
		return create();
	}

	List<VerifiableRequestProcessor> getRequestProcessors() {
		return requestProcessors;
	}

	public WsMockControl times(int min, int max) {
		if (requestProcessors.isEmpty())
		{
			throw new IllegalStateException("Can not set behaviour. No request processor defined.");
		}
		VerifiableRequestProcessor lastProcessor = getLastProcessor();
		lastProcessor.setMinNumberOfProcessedRequests(min);
		lastProcessor.setMaxNumberOfProcessedRequests(max);
		return this; 
	}

	private VerifiableRequestProcessor getLastProcessor() {
		return requestProcessors.get(requestProcessors.size()-1);
	}


	
	

}
