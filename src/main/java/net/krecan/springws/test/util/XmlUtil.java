package net.krecan.springws.test.util;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;

public final class XmlUtil {
	
	private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
	
	
	private  XmlUtil()
	{
	 //nothing	
	}
	
	public static final Document loadDocument(Source source)
	{
		try {
			DOMResult messageContent = new DOMResult();
			TRANSFORMER_FACTORY.newTransformer().transform(source, messageContent);
			return (Document)messageContent.getNode();
		} catch (TransformerException e) {
			throw new RuntimeException("Unexpected exception",e);
		}
	}

	public static Document loadDocument(WebServiceMessage message) {
		return loadDocument(getEnvelopeSource(message));
	}

	public static Source getEnvelopeSource(WebServiceMessage message) {
		if (message instanceof SoapMessage) {
			SoapMessage soapMessage = (SoapMessage) message;
			return soapMessage.getEnvelope().getSource();
		}
		else
		{
			throw new UnsupportedOperationException("Can not load message that is not SoapMessage");
		}
	}
}
