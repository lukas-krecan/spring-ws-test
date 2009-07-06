package net.krecan.springws.test.message;

import java.net.URI;

import net.krecan.springws.test.MessageLookup;
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
 * Loads resources based on XPath expressions and message content. Iterates over
 * expressions and looks for resources. If resource is found, it is returned, if
 * it is not found another expression is applied. If no result is found, null is
 * returned.
 * 
 * @author Lukas Krecan
 * 
 */
public class XPathMessageLookup implements MessageLookup, ResourceLoaderAware {
	private XPathExpression[] resourceXPathExpressions;

	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	protected final Log logger = LogFactory.getLog(getClass());

	public Resource lookupResource(URI uri, WebServiceMessage message) {
		if (resourceXPathExpressions != null) {
			for (int i = 0; i < resourceXPathExpressions.length; i++) {
				
				Resource resultResource = findResourceForXPath(resourceXPathExpressions[i], message);
				if (resultResource!=null)
				{
					return resultResource;
				}
			}
		}
		return null;
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

	public XPathExpression[] getResourceXPathExpressions() {
		return resourceXPathExpressions;
	}

	public void setResourceXPathExpressions(XPathExpression[] resourceXpathExpressions) {
		this.resourceXPathExpressions = resourceXpathExpressions;
	}
	
	public void setResourceXPathExpression(XPathExpression resourceXpathExpression) {
		this.resourceXPathExpressions = new XPathExpression[]{resourceXpathExpression};
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
