package net.krecan.springws.test.resource;

import static net.krecan.springws.test.util.XmlUtil.getEnvelopeSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import javax.xml.transform.stream.StreamResult;

import net.krecan.springws.test.util.XmlUtil;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.ResourceSource;

public class TemplateProcessingXPathResourceLookup extends XPathResourceLookup {
	@Override
	public Resource lookupResource(URI uri, WebServiceMessage message) throws IOException {
		Resource resource = super.lookupResource(uri, message);
		if (resource!=null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XmlUtil.transform(new ResourceSource(resource), getEnvelopeSource(message), new StreamResult(baos));
			return new ByteArrayResource(baos.toByteArray());
		}
		else
		{
			return null;
		}
	}
}
