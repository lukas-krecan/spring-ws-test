package net.javacrumbs.springws.test.common;

import java.io.IOException;

import javax.xml.transform.Source;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

/**
 * Compares messages, throws {@link WsTestException} if they differ.
 * @author Lukas Krecan
 *
 */
public class DefaultMessageComparator implements MessageComparator {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	private boolean ignoreWhitespace = true; 

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.common.MessageComparator#compareMessage(org.springframework.ws.WebServiceMessage, org.springframework.core.io.Resource)
	 */
	public void compareMessage(WebServiceMessage message, Resource controlResource) throws IOException {
		Document controlDocument = loadDocument(controlResource);
		if (isSoap(controlDocument))
		{
			Document messageDocument = loadDocument(message);
			compareDocuments(controlDocument, messageDocument);
		}
		else
		{
			Document payloadDocument = loadDocument(message.getPayloadSource());
			compareDocuments(controlDocument, payloadDocument);
		}
	}
	
	/**
	 * Compares documents. If they are different throws {@link WsTestException}.
	 * @param controlDocument
	 * @param messageDocument
	 */
	protected void compareDocuments(Document controlDocument, Document messageDocument) {
		if (logger.isDebugEnabled())
		{
			logger.debug("Comparing \""+serializeDocument(controlDocument)+" \"\n with \n\""+serializeDocument(messageDocument)+"\"");
		}
		Diff diff = createDiff(controlDocument, messageDocument);
		if (!diff.similar()) {
			logger.debug("Messages different, throwing exception");
			throw new WsTestException("Message is different " + diff.toString());
		}
	}
	
	
	protected Diff createDiff(Document controlDocument, Document messageDocument) {
		if (ignoreWhitespace != XMLUnit.getIgnoreWhitespace())
		{
			XMLUnit.setIgnoreWhitespace(ignoreWhitespace);
		}
		return new EnhancedDiff(controlDocument, messageDocument);
	}

	protected String serializeDocument(Document document) {
		return getXmlUtil().serializeDocument(document);
	}
	
	protected boolean isSoap(Document document)
	{
		return xmlUtil.isSoap(document);
	}
	
	protected Document loadDocument(WebServiceMessage message) throws IOException {
		return getXmlUtil().loadDocument(message);
	}

	protected Document loadDocument(Resource resource) throws IOException {
		return getXmlUtil().loadDocument(resource);
	}
	
	protected Document loadDocument(Source source) {
		return getXmlUtil().loadDocument(source);
	}

	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.common.MessageComparator#setXmlUtil(net.javacrumbs.springws.test.util.XmlUtil)
	 */
	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

	public boolean isIgnoreWhitespace() {
		return ignoreWhitespace;
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.common.MessageComparator#setIgnoreWhitespace(boolean)
	 */
	public void setIgnoreWhitespace(boolean ignoreWhitespace) {
		this.ignoreWhitespace = ignoreWhitespace;
	}
	
}
