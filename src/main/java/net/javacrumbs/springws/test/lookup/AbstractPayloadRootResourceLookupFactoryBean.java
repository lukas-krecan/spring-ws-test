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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.javacrumbs.springws.test.expression.XPathExpressionResolver;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public abstract class AbstractPayloadRootResourceLookupFactoryBean extends AbstractFactoryBean {

	private Map<String, String> namespaceMap;
	private String pathPrefix = PayloadRootBasedResourceLookup.DEFAULT_PATH_PREFIX;
	private String pathSuffix = PayloadRootBasedResourceLookup.DEFAULT_PATH_SUFFIX;
	private String discriminatorDelimiter = PayloadRootBasedResourceLookup.DEFAULT_DISCRIMINATOR_DELIMITER;
	private String payloadDelimiter = PayloadRootBasedResourceLookup.DEFAULT_PAYLOAD_DELIMITER;
	private boolean prependUri;
	private Map<String, String[]> discriminators;

	public AbstractPayloadRootResourceLookupFactoryBean() {
		super();
	}

	protected ResourceLookup getResourceLookup() {
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);

		PayloadRootBasedResourceLookup resourceLookup = new PayloadRootBasedResourceLookup();
		resourceLookup.setExpressionResolver(expressionResolver);
		resourceLookup.setPathPrefix(pathPrefix);
		resourceLookup.setPathSuffix(pathSuffix);
		resourceLookup.setDiscriminatorDelimiter(discriminatorDelimiter);
		resourceLookup.setPayloadDelimiter(payloadDelimiter);
		resourceLookup.setDiscriminators(discriminators);
		resourceLookup.setPrependUri(prependUri);
		return resourceLookup;
	}


	public Map<String, String> getNamespaceMap() {
		return namespaceMap;
	}

	public void setNamespaceMap(Map<String, String> namespaceMap) {
		this.namespaceMap = namespaceMap;
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

	public boolean isPrependUri() {
		return prependUri;
	}

	public void setPrependUri(boolean prependUri) {
		this.prependUri = prependUri;
	}

}