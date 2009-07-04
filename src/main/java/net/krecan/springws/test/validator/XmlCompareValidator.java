package net.krecan.springws.test.validator;

import java.io.IOException;
import java.net.URI;

import javax.xml.transform.TransformerException;

import net.krecan.springws.test.RequestValidator;
import net.krecan.springws.test.WsTestException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.xml.sax.SAXException;

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

			try {
				StringResult messageContent = new StringResult();
				transform(soapMessage.getEnvelope().getSource(), messageContent);

				StringResult controlContent = new StringResult();
				transform(new ResourceSource(controlResource), controlContent);

				Diff diff = new Diff(controlContent.toString(), messageContent.toString());
				if (!diff.similar())
				{
					throw new WsTestException("Message is different "+diff.toString());
				}
			} catch (TransformerException e) {
				throw new IOException("Unexpected exception",e);
			} catch (SAXException e) {
				throw new IOException("Unexpected exception",e);
			}
		}
		else
		{
			logger.warn("Comparison of non SoapMessages not supported");
		}
	}

	public Resource getControlResource() {
		return controlResource;
	}

	public void setControlResource(Resource controlResource) {
		this.controlResource = controlResource;
	}

}
