package net.krecan.springws.test.validator;

import java.io.IOException;
import java.net.URI;

import javax.xml.transform.Source;

import net.krecan.springws.test.RequestValidator;
import net.krecan.springws.test.WsTestException;
import net.krecan.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.ResourceSource;
import org.w3c.dom.Document;

/**
 * Compares message with control message.
 * 
 * @author Lukas Krecan
 * 
 */
public class XmlCompareValidator  implements RequestValidator{

	private Resource controlResource;
	
    protected final Log logger = LogFactory.getLog(getClass());
	


	public void validate(URI uri, WebServiceMessage message) throws IOException {
		Document messageDocument = loadDocument(message);

		Document controlDocument = loadDocument(new ResourceSource(controlResource));

		Diff diff = createDiff(controlDocument, messageDocument);
		if (!diff.similar())
		{
			throw new WsTestException("Message is different "+diff.toString());
		}
	}

	

	protected Diff createDiff(Document controlDocument, Document messageDocument) {
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

	protected Document loadDocument(WebServiceMessage message) {
		return XmlUtil.loadDocument(message);
	}
	
	protected Document loadDocument(Source source)  {
		return XmlUtil.loadDocument(source);
	}

	public Resource getControlResource() {
		return controlResource;
	}

	public void setControlResource(Resource controlResource) {
		this.controlResource = controlResource;
	}

}
