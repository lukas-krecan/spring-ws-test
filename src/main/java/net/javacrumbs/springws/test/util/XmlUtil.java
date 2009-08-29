package net.javacrumbs.springws.test.util;

import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;

/**
 * Common XML manipulating code. 
 * @author Lukas Krecan
 *
 */
public interface XmlUtil {

	public abstract Document loadDocument(Source source);

	public abstract Document loadDocument(Resource resource) throws IOException;

	public abstract void transform(Source source, Result result);

	public abstract void transform(Source template, Source source, Result result);

	public abstract Document loadDocument(WebServiceMessage message);

	public abstract Source getEnvelopeSource(WebServiceMessage message);

	public abstract String serializeDocument(Source source);

	public abstract String serializeDocument(WebServiceMessage message);

	public abstract String serializeDocument(Document document);

}