package net.krecan.springws.test.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import javax.xml.transform.stream.StreamResult;

import net.krecan.springws.test.util.DefaultXmlUtil;
import net.krecan.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.ResourceSource;
import org.w3c.dom.Document;

public class XsltTemplateProcessor implements TemplateProcessor {
	
	private static final String XSL_NAMESPACE = "http://www.w3.org/1999/XSL/Transform";
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	public Resource processTemplate(Resource resource, URI uri, WebServiceMessage message) throws IOException {
		if (resource!=null)
		{
			if (isTemplate(resource))
			{
				return transform(resource, message);
			}
			else
			{
				return resource;
			}
		}
		else
		{
			return null;
		}
	}
	
	protected boolean isTemplate(Resource resource) throws IOException {
		Document document = loadDocument(resource);
		return XSL_NAMESPACE.equals(document.getFirstChild().getNamespaceURI());
	}
	
	protected Document loadDocument(Resource resource) throws IOException {
		return getXmlUtil().loadDocument(resource);
	}

	protected Resource transform(Resource resource, WebServiceMessage message) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		getXmlUtil().transform(new ResourceSource(resource), getXmlUtil().getEnvelopeSource(message), new StreamResult(baos));
		if (logger.isTraceEnabled())
		{
			logger.trace("Transformation result:\n"+new String(baos.toByteArray(),"UTF-8"));
		}
		return new ByteArrayResource(baos.toByteArray());
	}

	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

}
