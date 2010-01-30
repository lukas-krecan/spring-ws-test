/**
 * Copyright 2009-2010 the original author or authors.
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

import java.net.URI;

import net.javacrumbs.springws.test.expression.ExpressionResolver;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
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
public abstract class AbstractResourceLookup extends AbstractTemplateProcessingResourceLookup implements ResourceLookup, ResourceLoaderAware {
	
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	private ExpressionResolver expressionResolver;
	
	/**
	 * Evaluates the expression.
	 * @param expression
	 * @param uri
	 * @param document
	 * @return
	 */
	protected String evaluateExpression(String expression, URI uri, Document document)
	{
		return expressionResolver.resolveExpression(expression, uri, document);
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	public ExpressionResolver getExpressionResolver() {
		return expressionResolver;
	}

	public void setExpressionResolver(ExpressionResolver expressionResolver) {
		this.expressionResolver = expressionResolver;
	}

}
