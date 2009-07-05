package net.krecan.springws.test.generator;

import java.io.IOException;
import java.net.URI;

import net.krecan.springws.test.ResponseGenerator;
import net.krecan.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.xml.xpath.XPathExpression;
import org.w3c.dom.Document;



//TODO refactor, logging
public class PathBasedResponseGenerator implements ResponseGenerator, ResourceLoaderAware {
	
	private XPathExpression resourceXPathExpression;
	
	private XPathExpression defaultXPathExpression;
	
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public WebServiceMessage generateResponse(URI uri,	WebServiceMessageFactory messageFactory, WebServiceMessage request) throws IOException {
		Resource resultResource = getResultResource(request);
		if (resultResource==null)
		{
			logger.debug("Resource not found, returning null.");
			return null;
		}
		else
		{
			return messageFactory.createWebServiceMessage(resultResource.getInputStream());
		}
	}

	protected Resource getResultResource(WebServiceMessage request) {
		Resource resultResource = null;
		if (resourceXPathExpression!=null)
		{
			resultResource = findResourceForXPath(resourceXPathExpression, request);
		}
		if (resultResource==null && defaultXPathExpression!=null)
		{
			resultResource = findResourceForXPath(defaultXPathExpression, request);
		}
		return resultResource;
	}

	protected Resource findResourceForXPath(XPathExpression xpath, WebServiceMessage request) {
		Resource resultResource;
		String resourcePath = xpath.evaluateAsString(loadDocument(request));
		logger.debug("Looking for classpath resource \""+resourcePath+"\"");
		resultResource = resourceLoader.getResource(resourcePath);
		resultResource = resultResource.exists()?resultResource:null;
		return resultResource;
	}

	public XPathExpression getResourceXPathExpression() {
		return resourceXPathExpression;
	}

	public void setResourceXPathExpression(XPathExpression resourceXpathExpression) {
		this.resourceXPathExpression = resourceXpathExpression;
	}
	
	protected Document loadDocument(WebServiceMessage message)
	{
		return XmlUtil.loadDocument(message);
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public XPathExpression getDefaultXPathExpression() {
		return defaultXPathExpression;
	}

	public void setDefaultXPathExpression(XPathExpression defaultXPathExpression) {
		this.defaultXPathExpression = defaultXPathExpression;
	}

}
