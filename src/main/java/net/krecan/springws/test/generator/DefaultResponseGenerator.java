package net.krecan.springws.test.generator;

import java.io.IOException;
import java.net.URI;

import net.krecan.springws.test.ResourceLookup;
import net.krecan.springws.test.ResponseGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

//TODO refactor, logging
public class DefaultResponseGenerator implements ResponseGenerator {

	protected final Log logger = LogFactory.getLog(getClass());

	private ResourceLookup resourceLookup;

	public WebServiceMessage generateResponse(URI uri, WebServiceMessageFactory messageFactory,
			WebServiceMessage request) throws IOException {
		Resource resultResource = getResultResource(uri, request);
		if (resultResource == null) {
			logger.debug("Resource not found, returning null.");
			return null;
		} else {
			return createResponse(messageFactory, resultResource);
		}
	}

	protected WebServiceMessage createResponse(WebServiceMessageFactory messageFactory, Resource resultResource)
			throws IOException {
		return messageFactory.createWebServiceMessage(resultResource.getInputStream());
	}

	protected Resource getResultResource(URI uri, WebServiceMessage request) {
		return resourceLookup.lookupResource(uri, request);
	}

	public ResourceLookup getResourceLookup() {
		return resourceLookup;
	}

	public void setResourceLookup(ResourceLookup resourceLookup) {
		this.resourceLookup = resourceLookup;
	}
}
