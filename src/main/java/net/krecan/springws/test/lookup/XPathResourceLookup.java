package net.krecan.springws.test.lookup;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.krecan.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.namespace.SimpleNamespaceContext;
import org.w3c.dom.Document;


/**
 * Loads resources based on XPaths and message content. Iterates over
 * expressions and looks for resources. If resource is found, it is returned, if
 * it is not found another expression is applied. If no result is found, null is
 * returned. 
 * 
 * @author Lukas Krecan
 * 
 */
public class XPathResourceLookup implements ResourceLookup, ResourceLoaderAware {
	//can not use XPAthExpression, we have dynamic beahviour.
	private String[] resourceXPaths;
	
	private NamespaceContext namespaceContext;

	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	protected final Log logger = LogFactory.getLog(getClass());

	public Resource lookupResource(URI uri, WebServiceMessage message) throws IOException {
		if (resourceXPaths != null) {
			Document document = loadDocument(message);
			for (int i = 0; i < resourceXPaths.length; i++) {
				Resource resultResource = findResourceForXPath(resourceXPaths[i], uri, document);
				if (resultResource!=null)
				{
					return resultResource;
				}
			}
		}
		return null;
	}

	protected Resource findResourceForXPath(String xpath, URI uri, Document document) {
		Resource resultResource;
		String resourcePath = evaluateXpath(xpath, uri, document);
		logger.debug("Looking for resource \"" + resourcePath + "\"");
		resultResource = resourceLoader.getResource(resourcePath);
		resultResource = resultResource.exists() ? resultResource : null;
		return resultResource;
	}

	protected String evaluateXpath(String xpathExpression, URI uri, Document document) {
		XPathFactory factory = XPathFactory.newInstance();
		factory.setXPathVariableResolver(new WsTestXPathVariableResolver(uri));
		try {
			XPath xpath = factory.newXPath();
			xpath.setNamespaceContext(namespaceContext);
			return xpath.evaluate(xpathExpression, document);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Unexpected exception",e);
		}
	}

	protected Document loadDocument(WebServiceMessage message) {
		return XmlUtil.loadDocument(message);
	}



	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public String[] getResourceXPaths() {
		return resourceXPaths;
	}

	public void setResourceXPaths(String[] resourceXPaths) {
		this.resourceXPaths = resourceXPaths;
	}
	public void setResourceXPath(String resourceXpath) {
		setResourceXPaths(new String[]{resourceXpath});		
	}

	public void setNamespaceMap(Map<String, String> namespaceMap) {
		SimpleNamespaceContext context = new SimpleNamespaceContext();
		context.setBindings(namespaceMap);
		setNamespaceContext(context);
	}

	public NamespaceContext getNamespaceContext() {
		return namespaceContext;
	}

	public void setNamespaceContext(NamespaceContext namespaceContext) {
		this.namespaceContext = namespaceContext;
	}

}
