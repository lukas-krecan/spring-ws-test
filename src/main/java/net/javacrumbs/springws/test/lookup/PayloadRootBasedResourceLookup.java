/**
 * Copyright 2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.springws.test.lookup;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

	
	static final String DEFAULT_PAYLOAD_DELIMITER = "/";

	static final String DEFAULT_DISCRIMINATOR_DELIMITER = "-";

	static final String DEFAULT_PATH_SUFFIX = "response.xml";

	static final String DEFAULT_PATH_PREFIX = "mock-xml/";

	private Map<String,String[]> discriminators;
	
	private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
	
	private String pathPrefix = DEFAULT_PATH_PREFIX;

	private String pathSuffix = DEFAULT_PATH_SUFFIX;

	private String discriminatorDelimiter = DEFAULT_DISCRIMINATOR_DELIMITER;

	private String payloadDelimiter = DEFAULT_PAYLOAD_DELIMITER;
	
	private boolean prependUri = false;
	
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
		String[] expressions = getDiscriminators(payloadName);
		Document document = getXmlUtil().loadDocument(message);
		Resource resource; 
		int discriminatorsCount = expressions.length;
		do
		{
			String resourceName = getResourceName(uri, payloadName, expressions, discriminatorsCount, document);
			logger.debug("Looking for resource "+resourceName);
			resource = getResourceLoader().getResource(resourceName);
			discriminatorsCount--;
		}
		while((resource == null || !resource.exists()) && discriminatorsCount>=0);
		if (resource!=null && resource.exists())
		{
			logger.debug("Found resource "+resource);
			return getTemplateProcessor().processTemplate(resource, uri, message);
		}
		else
		{
			return null;
		}
	}
	
	private String[] getDiscriminators(String payloadName) {
		String[] result = discriminators.get(payloadName);
		return result!=null?result:new String[0];
	}
	
	protected String getResourceName(URI uri, String payloadName, String[] expressions, int discriminatorsCount, Document document) {
		String uriHost = prependUri?(uri.getHost()+payloadDelimiter):"";
		return pathPrefix+uriHost+payloadName+payloadDelimiter+getDiscriminatorExpression(uri, expressions, discriminatorsCount, document)+pathSuffix;
	}
	/**
	 * Returns expression generated from discriminators
	 * @param uri
	 * @param expressions
	 * @param discriminatorsCount 
	 * @param document
	 * @return
	 */
	protected String getDiscriminatorExpression(URI uri, String[] expressions, int discriminatorsCount, Document document) {
		StringBuilder result = new StringBuilder();
		for (int i=0; i<discriminatorsCount;i++) {
			String expression = expressions[i];
			String evaluated = evaluateExpression(expression, uri, document);
			if (StringUtils.hasText(evaluated))
			{
				result.append(evaluated).append(discriminatorDelimiter);
			}
		}	 
		return result.toString(); 
	}

	public Map<String, String[]> getDiscriminatorsMap() {
		return discriminators;
	}

	public void setDiscriminatorsMap(Map<String, String[]> discriminators) {
		this.discriminators = discriminators;
	}
	
	public void setDiscriminators(Map<String, String> discriminators) {
		Map<String, String[]> disc = new HashMap<String, String[]>();
		for(Entry<String, String> entry:discriminators.entrySet())
		{
			disc.put(entry.getKey(), entry.getValue().split("\\s*;\\s*"));
		}
		this.discriminators = disc;
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
	public String getDiscriminatorDelimiter() {
		return discriminatorDelimiter;
	}
	public void setDiscriminatorDelimiter(String discriminatorDelimiter) {
		this.discriminatorDelimiter = discriminatorDelimiter;
	}
	public String getPayloadDelimiter() {
		return payloadDelimiter;
	}
	public void setPayloadDelimiter(String payloadDelimiter) {
		this.payloadDelimiter = payloadDelimiter;
	}

	public boolean isPrependUri() {
		return prependUri;
	}

	public void setPrependUri(boolean prependUri) {
		this.prependUri = prependUri;
	}
}
