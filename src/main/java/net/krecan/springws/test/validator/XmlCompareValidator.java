package net.krecan.springws.test.validator;

import java.io.IOException;
import java.net.URI;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;

import net.krecan.springws.test.RequestValidator;
import net.krecan.springws.test.WsTestException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.w3c.dom.Document;

/**
 * Compares message with control message.
 * 
 * @author Lukas Krecan
 * 
 */
public class XmlCompareValidator extends TransformerObjectSupport implements RequestValidator{

	private Resource controlResource;
	
    protected final Log logger = LogFactory.getLog(getClass());
	


	public void validate(URI uri, WebServiceMessage message) throws IOException {
		if (message instanceof SoapMessage) {
			SoapMessage soapMessage = (SoapMessage) message;
			
			Document messageDocument = loadDocument(soapMessage.getEnvelope().getSource());

			Document controlDocument = loadDocument(new ResourceSource(controlResource));

			Diff diff = createDiff(messageDocument, controlDocument);
			if (!diff.similar())
			{
				throw new WsTestException("Message is different "+diff.toString());
			}
		}
		else
		{
			logger.warn("Comparison of non SoapMessages not supported");
		}
	}

	protected Diff createDiff(Document messageDocument, Document controlDocument) {
		return new Diff(controlDocument, messageDocument){
			@Override
			public int differenceFound(Difference difference) {
				if ("${IGNORE}".equals(difference.getControlNodeDetail().getValue()))
				{
					return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
				}
				else
				{
					return super.differenceFound(difference);
				}
			}
		};
	}

	private Document loadDocument(Source source)  {
		try {
			DOMResult messageContent = new DOMResult();
			transform(source, messageContent);
			return (Document)messageContent.getNode();
		} catch (TransformerException e) {
			throw new RuntimeException("Unexpected exception",e);
		}
	}

	public Resource getControlResource() {
		return controlResource;
	}

	public void setControlResource(Resource controlResource) {
		this.controlResource = controlResource;
	}

}
