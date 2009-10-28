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

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.w3c.dom.Document;


/**
 * Loads resources based on expressions and message content. Iterates over
 * expressions, evaluates them and looks for resources with given name. If resource is found, it is returned, if
 * it is not found another expression is applied. If no result is found, <code>null</code> is
 * returned.
 * XPathResourceLookup
 * <br/> 
 * 
 * Following variables can be used:
 * <ul>
 * <li><code>$uri</code> for service URI.</li>
 * <li><Context attributes like <code>$context.departureTime</code></li>
 * <li>Expressions like <code>$uri.host</code>
 * </ul>
 * 
 * @author Lukas Krecan
 * 
 */
public class ExpressionBasedResourceLookup extends AbstractResourceLookup {
	private String[] resourceExpressions;

	
	/**
	 * Iterates over expressions, evaluates them and looks for resource with corresponding name. 
	 */
	public Resource lookupResource(URI uri, WebServiceMessage message) throws IOException {
		if (resourceExpressions != null) {
			Document document = loadDocument(message);
			for (String resourceExpression: resourceExpressions) {
				Resource resultResource = findResourceForExpression(resourceExpression, uri, document);
				if (resultResource!=null)
				{
					logger.debug("Found resource "+resultResource);
					return getTemplateProcessor().processTemplate(resultResource, uri, message);
				}
			}
		}
		return null;
	}
	
	/**
	 * Looks for a resource using given expression.
	 * @param xpath
	 * @param uri
	 * @param document
	 * @return
	 */
	protected Resource findResourceForExpression(String xpath, URI uri, Document document) {
		String resourcePath = evaluateExpression(xpath, uri, document);
		logger.debug("Looking for resource \"" + resourcePath + "\"");
		Resource resultResource = getResourceLoader().getResource(resourcePath);
		resultResource = resultResource.exists() ? resultResource : null;
		return resultResource;
	}

	
	public String[] getResourceExpressions() {
		return resourceExpressions;
	}

	public void setResourceExpressions(String... resourceExpressions) {
		this.resourceExpressions = resourceExpressions;
	}
}
