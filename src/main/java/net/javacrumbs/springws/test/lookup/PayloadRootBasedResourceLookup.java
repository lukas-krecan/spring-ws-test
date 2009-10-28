package net.javacrumbs.springws.test.lookup;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.server.endpoint.support.PayloadRootUtils;
import org.w3c.dom.Document;

/**
 * Resource lookup based on root payload map. 
 * TODO add documentation
 * @author Lukas Krecan
 *
 */
public class PayloadRootBasedResourceLookup extends AbstractResourceLookup {

	private Map<String, List<String>> discriminators;
	
	private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
	
	private String pathPrefix = "mock-xml/";

	private String pathSuffix = "response.xml";

	private String discriminatorDelimiter = "-";

	private String payloadDelimiter = "/";
	
	public Resource lookupResource(URI uri, WebServiceMessage message) throws IOException {
		QName payloadQName;
		try {
			payloadQName = PayloadRootUtils.getPayloadRootQName(message.getPayloadSource(), TRANSFORMER_FACTORY);
		} catch (TransformerException e) {
			logger.warn("Can not resolve payload.",e);
			return null;
		} catch (XMLStreamException e) {
			logger.warn("Can not resolve payload.",e);
			return null;
		}
		String payloadName = payloadQName.getLocalPart();
		List<String> expressions = discriminators.get(payloadName);
		Document document = getXmlUtil().loadDocument(message);
		Resource resource; 
		int discriminatorsCount = expressions.size();
		do
		{
			resource = getResourceLoader().getResource(getResourceName(uri, payloadName, expressions, discriminatorsCount, document));
			discriminatorsCount--;
		}
		while(resource == null && discriminatorsCount>=0);
		return resource;
	}
	protected String getResourceName(URI uri, String payloadName, List<String> expressions, int discriminatorsCount, Document document) {
		return pathPrefix+payloadName+payloadDelimiter+getDiscriminatorExpression(uri, expressions, discriminatorsCount, document)+pathSuffix;
	}
	/**
	 * Returns expression generated from discriminators
	 * @param uri
	 * @param expressions
	 * @param discriminatorsCount 
	 * @param document
	 * @return
	 */
	protected String getDiscriminatorExpression(URI uri, List<String> expressions, int discriminatorsCount, Document document) {
		StringBuilder result = new StringBuilder();
		for (int i=0; i<discriminatorsCount;i++) {
			String expression = expressions.get(i);
			String evaluated = evaluateExpression(expression, uri, document);
			if (StringUtils.hasText(evaluated))
			{
				result.append(evaluated).append(discriminatorDelimiter);
			}
		}	 
		return result.toString(); 
	}

	public Map<String, List<String>> getDiscriminators() {
		return discriminators;
	}

	public void setDiscriminators(Map<String, List<String>> discriminators) {
		this.discriminators = discriminators;
	}
	public String getPathPrefix() {
		return pathPrefix;
	}
	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}
	public String getPathSuffix() {
		return pathSuffix;
	}
	public void setPathSuffix(String pathSuffix) {
		this.pathSuffix = pathSuffix;
	}
	String getDiscriminatorDelimiter() {
		return discriminatorDelimiter;
	}
	void setDiscriminatorDelimiter(String discriminatorDelimiter) {
		this.discriminatorDelimiter = discriminatorDelimiter;
	}
	String getPayloadDelimiter() {
		return payloadDelimiter;
	}
	void setPayloadDelimiter(String payloadDelimiter) {
		this.payloadDelimiter = payloadDelimiter;
	}
}
