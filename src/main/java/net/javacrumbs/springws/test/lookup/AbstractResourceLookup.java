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

import java.net.URI;

import net.javacrumbs.springws.test.expression.ExpressionResolver;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
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
public abstract class AbstractResourceLookup implements ResourceLookup, ResourceLoaderAware {
	
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();
	
	private ExpressionResolver expressionResolver;
	
	private TemplateProcessor templateProcessor = new XsltTemplateProcessor();

	
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

	protected Document loadDocument(WebServiceMessage message) {
		return getXmlUtil().loadDocument(message);
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

	public ExpressionResolver getExpressionResolver() {
		return expressionResolver;
	}

	public void setExpressionResolver(ExpressionResolver expressionResolver) {
		this.expressionResolver = expressionResolver;
	}

	public TemplateProcessor getTemplateProcessor() {
		return templateProcessor;
	}

	public void setTemplateProcessor(TemplateProcessor templateProcessor) {
		this.templateProcessor = templateProcessor;
	}

}
