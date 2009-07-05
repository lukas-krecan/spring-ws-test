package net.krecan.springws.test.resource;

import java.net.URI;

import net.krecan.springws.test.ResourceLookup;
import net.krecan.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.xpath.XPathExpression;
import org.w3c.dom.Document;

/**
 * Loads resources based on XPath expression and message content.
 * 
 * @author Lukas Krecan
 * 
 */
//TODO array of xpaths
public class XPathResourceLookup implements ResourceLookup, ResourceLoaderAware {
	private XPathExpression resourceXPathExpression;

	private XPathExpression defaultXPathExpression;

	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	protected final Log logger = LogFactory.getLog(getClass());

	public Resource lookupResource(URI uri, WebServiceMessage message) {
		Resource resultResource = null;
		if (resourceXPathExpression != null) {
			resultResource = findResourceForXPath(resourceXPathExpression, message);
		}
		if (resultResource == null && defaultXPathExpression != null) {
			resultResource = findResourceForXPath(defaultXPathExpression, message);
		}
		return resultResource;
	}

	protected Resource findResourceForXPath(XPathExpression xpath, WebServiceMessage message) {
		Resource resultResource;
		String resourcePath = xpath.evaluateAsString(loadDocument(message));
		logger.debug("Looking for classpath resource \"" + resourcePath + "\"");
		resultResource = resourceLoader.getResource(resourcePath);
		resultResource = resultResource.exists() ? resultResource : null;
		return resultResource;
	}

	protected Document loadDocument(WebServiceMessage message) {
		return XmlUtil.loadDocument(message);
	}

	public XPathExpression getResourceXPathExpression() {
		return resourceXPathExpression;
	}

	public void setResourceXPathExpression(XPathExpression resourceXpathExpression) {
		this.resourceXPathExpression = resourceXpathExpression;
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
