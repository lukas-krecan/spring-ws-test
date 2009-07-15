package net.krecan.springws.test.lookup;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

import net.krecan.springws.test.util.DefaultXmlUtil;
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
 * Loads resources based on expressions and message content. Iterates over
 * expressions, evaluates them and looks for resources with given name. If resource is found, it is returned, if
 * it is not found another expression is applied. If no result is found, <code>null</code> is
 * returned.
 * XPathResourceLookup
 * <br/> 
 * 
 * Following variables can be used:
 * <ul>
 * <li><code>$uri</code> for service URI.</li>
 * <li><Context attributes like <code>$context.departureTime</code></li>
 * <li>Expressions like <code>$uri.host</code>
 * </ul>
 * 
 * @author Lukas Krecan
 * 
 */
public abstract class AbstractResourceLookup implements ResourceLookup, ResourceLoaderAware {
	private String[] resourceExpressions;
	
	private NamespaceContext namespaceContext;

	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();

	/**
	 * Iterates over expressions, evaluates them and looks for resource with corresponding name. 
	 */
	public Resource lookupResource(URI uri, WebServiceMessage message) throws IOException {
		if (resourceExpressions != null) {
			Document document = loadDocument(message);
			for (String resourceExpression: resourceExpressions) {
				Resource resultResource = findResourceForExpression(resourceExpression, uri, document);
				if (resultResource!=null)
				{
					logger.debug("Found resource "+resultResource);
					return resultResource;
				}
			}
		}
		return null;
	}
	
	/**
	 * Looks for a resource using given expression.
	 * @param xpath
	 * @param uri
	 * @param document
	 * @return
	 */
	protected Resource findResourceForExpression(String xpath, URI uri, Document document) {
		Resource resultResource; 
		String resourcePath = evaluateExpression(xpath, uri, document);
		logger.debug("Looking for resource \"" + resourcePath + "\"");
		resultResource = resourceLoader.getResource(resourcePath);
		resultResource = resultResource.exists() ? resultResource : null;
		return resultResource;
	}

	/**
	 * Evaluates the expression.
	 * @param expression
	 * @param uri
	 * @param document
	 * @return
	 */
	protected abstract String evaluateExpression(String expression, URI uri, Document document);

	protected Document loadDocument(WebServiceMessage message) {
		return getXmlUtil().loadDocument(message);
	}



	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public String[] getResourceExpressions() {
		return resourceExpressions;
	}

	public void setResourceExpressions(String[] resourceExpressions) {
		this.resourceExpressions = resourceExpressions;
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

	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

}
