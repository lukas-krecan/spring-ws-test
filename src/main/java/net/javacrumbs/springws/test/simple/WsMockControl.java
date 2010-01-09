/**
 * Copyright 2006 the original author or authors.
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
package net.javacrumbs.springws.test.simple;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.expression.ExpressionResolver;
import net.javacrumbs.springws.test.expression.XPathExpressionResolver;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.ExpressionBasedResourceLookup;
import net.javacrumbs.springws.test.template.FreeMarkerTemplateProcessor;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.validator.ExpressionAssertRequestValidator;
import net.javacrumbs.springws.test.validator.XPathRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Utility class for easy unit-test mock preparation. Usually used in this way
 * <code><pre>
 * //create control
 * WsMockControl mockControl = new WsMockControl();
 * //create mock
 * WebServiceMessageSender mockMessageSender = mockControl.expectRequest("PRG-DUB-request.xml").returnResponse("PRG-DUB-response.xml").createMock();
 * //use the mock
 * webServiceTemplate.setMessageSender(mockMessageSender);
 * 
 * //do your test here ...
 * 
 * //verify that the mock was used
 * mockControl.verify();
 * </pre></code>
 * 
 * @author Lukas Krecan
 * 
 */
public class WsMockControl {
	private final List<LimitingRequestProcessor> requestProcessors = new ArrayList<LimitingRequestProcessor>();

	private TemplateProcessor templateProcessor = new XsltTemplateProcessor();

	/**
	 * Create mock {@link WebServiceMessageSender}. If behavior not defined,
	 * throws {@link IllegalArgumentException}.
	 * 
	 * @return
	 */
	public WebServiceMessageSender createMock() {
		if (requestProcessors.isEmpty()) {
			throw new IllegalStateException(
					"No request processor defined. Please call at least returnResponse() method.");
		}
		MockWebServiceMessageSender messageSender = new MockWebServiceMessageSender();
		messageSender.setRequestProcessors(requestProcessors);
		return messageSender;
	}

	/**
	 * Adds request processor. Uses toString() method to get description of the
	 * processor and calls
	 * {@link #addRequestProcessor(RequestProcessor, String)}.
	 * 
	 * @param requestProcessor
	 * @return
	 */
	public WsMockControl addRequestProcessor(RequestProcessor requestProcessor) {
		return addRequestProcessor(requestProcessor, requestProcessor.toString());
	}

	/**
	 * Adds a request processor. If the processor does not implement
	 * {@link LimitingRequestProcessor} it's wrapped in
	 * {@link LimitingRequestProcessorWrapper}.
	 * 
	 * @param requestProcessor
	 * @param requestProcessorDescription
	 * @return
	 */
	public WsMockControl addRequestProcessor(RequestProcessor requestProcessor, String requestProcessorDescription) {
		if (requestProcessor instanceof LimitingRequestProcessor) {
			requestProcessors.add((LimitingRequestProcessor) requestProcessor);
		} else {
			requestProcessors.add(new LimitingRequestProcessorWrapper(requestProcessor, requestProcessorDescription));
		}
		return this;
	}

	/**
	 * Expects that request will be the same as content of the resource.
	 * 
	 * @param resourceName
	 * @return
	 */
	public WsMockControl expectRequest(String resourceName) {
		XmlCompareRequestValidator validator = new XmlCompareRequestValidator();
		ExpressionBasedResourceLookup resourceLookup = createResourceLookup(resourceName);
		validator.setControlResourceLookup(resourceLookup);
		validator.setFailIfControlResourceNotFound(true);
		addRequestProcessor(validator, "expectRequest(\"" + resourceName + "\")");
		return this;
	}

	/**
	 * Creates resource lookup to be used in request validators and response generators.
	 * @param resourceName
	 * @return
	 */
	protected ExpressionBasedResourceLookup createResourceLookup(String resourceName) {
		ExpressionBasedResourceLookup resourceLookup = new ExpressionBasedResourceLookup();
		resourceLookup.setExpressionResolver(ExpressionResolver.DUMMY_EXPRESSION_RESOLVER);
		resourceLookup.setResourceExpressions(resourceName);
		resourceLookup.setTemplateProcessor(templateProcessor);
		return resourceLookup;
	}

	/**
	 * Mock will fail if the expression evaluates to true.
	 * 
	 * @param expression
	 * @param namespaceMap
	 * @return
	 */
	public WsMockControl failIf(String expression, Map<String, String> namespaceMap) {
		XPathRequestValidator validator = new XPathRequestValidator();
		validator.setExceptionMapping(Collections.singletonMap(expression, "XPath assertion \"" + expression
				+ "\" failed."));
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		validator.setExpressionResolver(expressionResolver);
		addRequestProcessor(validator, "failIf(\"" + expression + "\")");
		return this;
	}

	/**
	 * Mock will fail if the expression evaluates to false.
	 * 
	 * @param expression
	 * @param namespaceMap
	 * @return
	 */
	public WsMockControl assertThat(String expression, Map<String, String> namespaceMap) {
		ExpressionAssertRequestValidator validator = new ExpressionAssertRequestValidator();
		validator.setAssertExpression(expression);
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		validator.setExpressionResolver(expressionResolver);
		addRequestProcessor(validator, "assertThat(\"" + expression + "\")");
		return this;
	}

	/**
	 * Mock will return response taken from the resource.
	 * 
	 * @param resourceName
	 * @return
	 */
	public WsMockControl returnResponse(String resourceName) {
		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		ExpressionBasedResourceLookup resourceLookup = createResourceLookup(resourceName);
		responseGenerator.setResourceLookup(resourceLookup);
		addRequestProcessor(responseGenerator, "returnResponse(\"" + resourceName + "\")");
		return this;
	}

	/**
	 * From now use FreeMarker for templates.
	 * @return
	 */
	public WsMockControl useFreeMarkerTemplateProcessor() {
		FreeMarkerTemplateProcessor freemarkerTemplateProcessor = new FreeMarkerTemplateProcessor();
		freemarkerTemplateProcessor.setResourceLoader(new DefaultResourceLoader());
		freemarkerTemplateProcessor.afterPropertiesSet();
		return useTemplateProcessor(freemarkerTemplateProcessor);
	}
	
	/**
	 * From now use XSLT for templates.
	 * @return
	 */
	public WsMockControl useXsltTemplateProcessor() {
		return useTemplateProcessor(new XsltTemplateProcessor());
	}

	/**
	 * Sets template processor to be used.
	 * 
	 * @param templateProcessor
	 * @return
	 */
	public WsMockControl useTemplateProcessor(TemplateProcessor templateProcessor) {
		this.templateProcessor = templateProcessor;
		return this;
	}

	/**
	 * Mock will throw an exception.
	 * 
	 * @param exception
	 * @return
	 */
	public WsMockControl throwException(final RuntimeException exception) {
		RequestProcessor thrower = new RequestProcessor() {
			public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory,
					WebServiceMessage request) throws IOException {
				throw exception;
			}
		};
		addRequestProcessor(thrower, "throwException(\"" + exception.getMessage() + "\")");
		return this;
	}

	/**
	 * Expects given uri. If other URI is used, {@link WsTestException} is
	 * thrown.
	 * 
	 * @param string
	 * @return
	 */
	public WsMockControl expectUri(final URI expectedUri) {
		RequestProcessor validator = new RequestProcessor() {
			public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory,
					WebServiceMessage request) throws IOException {
				if (!uri.equals(expectedUri)) {
					throw new WsTestException("Expected uri " + expectedUri + " but got " + uri);
				}
				return null;
			}
		};
		addRequestProcessor(validator, "expectUri(\"" + expectedUri + "\")");
		return this;
	}

	List<LimitingRequestProcessor> getRequestProcessors() {
		return requestProcessors;
	}

	/**
	 * Sets number of calls for the last {@link RequestProcessor}. If given
	 * processor was called les the min times, verify will throw
	 * {@link WsTestException}, if it was called for more then max times, the
	 * {@link RequestProcessor} will do nothing and return null. See
	 * {@link LimitingRequestProcessor} for more details.
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public WsMockControl times(int min, int max) {
		if (requestProcessors.isEmpty()) {
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
		return times(0, Integer.MAX_VALUE);
	}

	public WsMockControl atLeastOnce() {
		return times(1, Integer.MAX_VALUE);
	}

	private LimitingRequestProcessor getLastProcessor() {
		return requestProcessors.get(requestProcessors.size() - 1);
	}

	/**
	 * Verifies that all RequestProcessors were called given number of times.
	 */
	public void verify() {
		for (LimitingRequestProcessor processor : requestProcessors) {
			processor.verify();
		}
	}

}
