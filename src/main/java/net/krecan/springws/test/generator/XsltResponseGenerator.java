package net.krecan.springws.test.generator;

import static net.krecan.springws.test.util.XmlUtil.getEnvelopeSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import net.krecan.springws.test.ResourceLookup;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.ResourceSource;

/**
 * Looks-up resource using {@link ResourceLookup}, this resource is used as XSLT template on request {@link WebServiceMessage}.
 * Result of the transformation is used as source for the message.  
 * @author Lukas Krecan
 *
 */
public class XsltResponseGenerator extends DefaultResponseGenerator {
	private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
	
	@Override
	protected Resource getResultResource(URI uri, WebServiceMessage request) throws IOException{
		
		Resource resource = super.getResultResource(uri, request);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			TRANSFORMER_FACTORY.newTransformer(new ResourceSource(resource)).transform(getEnvelopeSource(request), new StreamResult(baos));
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException("Unexpected exception.",e);
		} catch (TransformerException e) {
			throw new RuntimeException("Unexpected exception.",e);
		} 
		return new ByteArrayResource(baos.toByteArray());
	}
}
