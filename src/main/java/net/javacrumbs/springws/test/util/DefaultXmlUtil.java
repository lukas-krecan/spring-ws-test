package net.javacrumbs.springws.test.util;

import java.io.IOException;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import net.javacrumbs.springws.test.context.WsTestContextHolder;

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.transform.StringResult;
import org.w3c.dom.Document;

public class DefaultXmlUtil implements XmlUtil {

	private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();

	private static final XmlUtil INSTANCE = new DefaultXmlUtil();

	public static final XmlUtil getInstance()
	{
		return INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.util.XmlUtil#loadDocument(javax.xml.transform.Source)
	 */
	public Document loadDocument(Source source) {
		DOMResult messageContent = new DOMResult();
		transform(source, messageContent);
		return (Document) messageContent.getNode();
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.util.XmlUtil#loadDocument(org.springframework.core.io.Resource)
	 */
	public Document loadDocument(Resource resource) throws IOException {
		return loadDocument(new ResourceSource(resource));
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.util.XmlUtil#transform(javax.xml.transform.Source, javax.xml.transform.Result)
	 */
	public void transform(Source source, Result result) {
		try {
			TRANSFORMER_FACTORY.newTransformer().transform(source, result);
		} catch (TransformerException e) {
			// FIXME throw better exception here.
			throw new RuntimeException("Unexpected exception", e);
		}
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.util.XmlUtil#transform(javax.xml.transform.Source, javax.xml.transform.Source, javax.xml.transform.Result)
	 */
	public void transform(Source template, Source source, Result result) {
		try {
			Transformer transformer = TRANSFORMER_FACTORY.newTransformer(template);
			setParameters(transformer);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// FIXME throw better exception here.
			throw new RuntimeException("Unexpected exception", e);
		}
	}

	private void setParameters(Transformer transformer) {
		for (Map.Entry<String, Object> entry : WsTestContextHolder.getTestContext().getAttributeMap().entrySet()) {
			transformer.setParameter(entry.getKey(), entry.getValue());
		}
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.util.XmlUtil#loadDocument(org.springframework.ws.WebServiceMessage)
	 */
	public Document loadDocument(WebServiceMessage message) {
		return loadDocument(getEnvelopeSource(message));
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.util.XmlUtil#getEnvelopeSource(org.springframework.ws.WebServiceMessage)
	 */
	public Source getEnvelopeSource(WebServiceMessage message) {
		if (message instanceof SoapMessage) {
			SoapMessage soapMessage = (SoapMessage) message;
			return soapMessage.getEnvelope().getSource();
		} else {
			throw new UnsupportedOperationException("Can not load message that is not SoapMessage");
		}
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.util.XmlUtil#serializeDocument(javax.xml.transform.Source)
	 */
	public String serializeDocument(Source source) {
		StringResult result = new StringResult();
		transform(source, result);
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.util.XmlUtil#serializeDocument(org.springframework.ws.WebServiceMessage)
	 */
	public String serializeDocument(WebServiceMessage message) {
		return serializeDocument(getEnvelopeSource(message));
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.util.XmlUtil#serializeDocument(org.w3c.dom.Document)
	 */
	public String serializeDocument(Document document) {
		return serializeDocument(new DOMSource(document));
	}
}
