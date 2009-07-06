package net.krecan.springws.test.util;

import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.transform.StringResult;
import org.w3c.dom.Document;

public final class XmlUtil {
	
	private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
	
	
	private  XmlUtil()
	{
	 //nothing	
	}
	
	public static final Document loadDocument(Source source)
	{
		DOMResult messageContent = new DOMResult();
		transform(source, messageContent);
		return (Document)messageContent.getNode();
	}
	public static final Document loadDocument(Resource resource) throws IOException
	{
		return loadDocument(new ResourceSource(resource));
	}

	public static void transform(Source source, Result result) {
		try {
			TRANSFORMER_FACTORY.newTransformer().transform(source, result);
		} catch (TransformerException e) {
			//FIXME throw better exception here.
			throw new RuntimeException("Unexpected exception",e);
		}
	}
	public static void transform(Source template, Source source, Result result) {
		try {
			TRANSFORMER_FACTORY.newTransformer(template).transform(source, result);
		} catch (TransformerException e) {
			//FIXME throw better exception here.
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
	
	public static String serializeDocument(Source source)
	{
		StringResult result = new StringResult();
		transform(source, result);
		return result.toString();
	}
	public static String serializeDocument(Document document)
	{
		return serializeDocument(new DOMSource(document));
	}
}
