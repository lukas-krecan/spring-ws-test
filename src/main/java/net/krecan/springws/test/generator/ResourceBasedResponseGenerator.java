package net.krecan.springws.test.generator;

import java.io.IOException;
import java.net.URI;

import net.krecan.springws.test.ResponseGenerator;
import net.krecan.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.xml.xpath.XPathExpression;
import org.w3c.dom.Document;

//TODO refactor, logging
public class ResourceBasedResponseGenerator implements ResponseGenerator {
	
	private XPathExpression resourceXPathExpression;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public WebServiceMessage generateResponse(URI uri,	WebServiceMessageFactory messageFactory, WebServiceMessage request) throws IOException {
		Resource resultResource = null;
		if (resourceXPathExpression!=null)
		{
			String resourcePath = resourceXPathExpression.evaluateAsString(loadDocument(request));
			logger.debug("Looking for classpath resource \""+resourcePath+"\"");
			resultResource = new ClassPathResource(resourcePath);
		}
		if (resultResource==null || !resultResource.exists())
		{
			logger.debug("Resource not found, using default.");
			resultResource = new ClassPathResource("mock-responses/test/default-response.xml");
		}
		return messageFactory.createWebServiceMessage(resultResource.getInputStream());
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

}
