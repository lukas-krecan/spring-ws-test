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
package net.javacrumbs.springws.test.xml;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.expression.XPathExpressionResolver;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.PayloadRootBasedResourceLookup;
import net.javacrumbs.springws.test.template.FreeMarkerTemplateProcessor;
import net.javacrumbs.springws.test.template.XsltTemplateProcessor;
import net.javacrumbs.springws.test.util.MockMessageSenderInjector;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class MockWsMessageSenderBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	protected final Log logger = LogFactory.getLog(getClass());

	private static final String TRUE = Boolean.TRUE.toString();

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
		Map<?, ?> namespaces = parseNamespaces(element, parserContext, bean);
		Map<?, ?> discriminators = parseDiscriminators(element, parserContext, bean);
		Element resourceConfig = DomUtils.getChildElementByTagName(element, "resource-config");
		String pathPrefix = resourceConfig.getAttribute("pathPrefix");
		String prependUri = resourceConfig.getAttribute("prependUri");
		String ignoreWhitespace = resourceConfig.getAttribute("ignoreWhitespace");
		BeanDefinitionBuilder templateProcessor = getTemplateProcessor(resourceConfig);
		
		bean.addPropertyValue("autowireRequestProcessors", element.getAttribute("autowireRequestProcessors"));
		
		BeanDefinitionBuilder expressionResolver = BeanDefinitionBuilder.rootBeanDefinition(XPathExpressionResolver.class);
		expressionResolver.addPropertyValue("namespaceMap", namespaces);
		
		ManagedList requestProcessors = new ManagedList();
						
		BeanDefinitionBuilder controlResourceLookup = BeanDefinitionBuilder.rootBeanDefinition(PayloadRootBasedResourceLookup.class);
		controlResourceLookup.addPropertyValue("expressionResolver", expressionResolver.getBeanDefinition());
		controlResourceLookup.addPropertyValue("discriminators", discriminators);
		controlResourceLookup.addPropertyValue("pathPrefix", pathPrefix);
		controlResourceLookup.addPropertyValue("prependUri", prependUri);
		controlResourceLookup.addPropertyValue("pathSuffix", "request.xml");
		controlResourceLookup.addPropertyValue("templateProcessor", templateProcessor.getBeanDefinition());

		BeanDefinitionBuilder xmlCompareRequestValidator = BeanDefinitionBuilder.rootBeanDefinition(XmlCompareRequestValidator.class);
		xmlCompareRequestValidator.addPropertyValue("controlResourceLookup", controlResourceLookup.getBeanDefinition());
		xmlCompareRequestValidator.addPropertyValue("ignoreWhitespace", ignoreWhitespace);

		addRequestProcessor(requestProcessors, xmlCompareRequestValidator);
		
		String[] schemas = parseRequestValidationSchemas(element, bean);
		if (schemas!=null)
		{
			BeanDefinitionBuilder schemaRequestValidator = BeanDefinitionBuilder.rootBeanDefinition(SchemaRequestValidator.class);
			schemaRequestValidator.addPropertyValue("schemas", schemas);
			addRequestProcessor(requestProcessors, schemaRequestValidator);
		}
		
		
		BeanDefinitionBuilder responseResourceLookup = BeanDefinitionBuilder.rootBeanDefinition(PayloadRootBasedResourceLookup.class);
		responseResourceLookup.addPropertyValue("expressionResolver", expressionResolver.getBeanDefinition());
		responseResourceLookup.addPropertyValue("discriminators", discriminators);
		responseResourceLookup.addPropertyValue("pathPrefix", pathPrefix);
		responseResourceLookup.addPropertyValue("prependUri", prependUri);
		responseResourceLookup.addPropertyValue("pathSuffix", "response.xml");
		responseResourceLookup.addPropertyValue("templateProcessor", templateProcessor.getBeanDefinition());
		
		BeanDefinitionBuilder defaultResponseGenerator = BeanDefinitionBuilder.rootBeanDefinition(DefaultResponseGenerator.class);
		defaultResponseGenerator.addPropertyValue("resourceLookup", responseResourceLookup.getBeanDefinition());
		addRequestProcessor(requestProcessors, defaultResponseGenerator);
		
		bean.addPropertyValue("requestProcessors", requestProcessors);
		
		if (TRUE.equals(element.getAttribute("autoinjectMock")))
		{
			AbstractBeanDefinition injector = BeanDefinitionBuilder.rootBeanDefinition(MockMessageSenderInjector.class).getBeanDefinition();
			BeanDefinitionHolder holder = new BeanDefinitionHolder(injector, parserContext.getReaderContext().generateBeanName(injector));
			registerBeanDefinition(holder, parserContext.getRegistry());
		}
		
		bean.addPropertyValue("interceptors",parseInterceptors(element, parserContext, bean));
	}

	private BeanDefinitionBuilder getTemplateProcessor(Element resourceConfig) {
		String templateProcessorName = resourceConfig.getAttribute("templateProcessor");
		if ("FreeMarker".equals(templateProcessorName))
		{
			return BeanDefinitionBuilder.rootBeanDefinition(FreeMarkerTemplateProcessor.class);
		}
		else
		{
			return BeanDefinitionBuilder.rootBeanDefinition(XsltTemplateProcessor.class);
		}
	}

	@SuppressWarnings("unchecked")
	protected boolean addRequestProcessor(ManagedList requestProcessors, BeanDefinitionBuilder schemaRequestValidator) {
		return requestProcessors.add(schemaRequestValidator.getBeanDefinition());
	}
	
	protected String[] parseRequestValidationSchemas(Element element, BeanDefinitionBuilder bean) {
		Element schemas = DomUtils.getChildElementByTagName(element, "schemas");
		if (schemas != null) {
			return tokenize(schemas);
		}
		else
		{
			return null;
		}
	}
	
	protected Map<?,?> parseDiscriminators(Element element,  ParserContext parserContext, BeanDefinitionBuilder bean) {
		Element discriminators = DomUtils.getChildElementByTagName(DomUtils.getChildElementByTagName(element, "resource-config"),"discriminators");
		if (discriminators != null) {
			return parserContext.getDelegate().parseMapElement(discriminators, bean.getRawBeanDefinition());
		}
		else
		{
			logger.warn("No discriminators found");
			return Collections.emptyMap();
		}
	}
	protected List<?> parseInterceptors(Element element,  ParserContext parserContext, BeanDefinitionBuilder bean) {
		Element interceptors = DomUtils.getChildElementByTagName(element, "interceptors");
		if (interceptors != null) {
			return parserContext.getDelegate().parseListElement(interceptors, bean.getRawBeanDefinition());
		}
		else
		{
			return Collections.emptyList();
		}
	}
		
	protected Map<?,?> parseNamespaces(Element element,  ParserContext parserContext, BeanDefinitionBuilder bean) {
		Element namespaces = DomUtils.getChildElementByTagName(element, "namespaces");
		if (namespaces != null) {
			return parserContext.getDelegate().parseMapElement(namespaces, bean.getRawBeanDefinition());
		}
		else
		{
			logger.warn("No namespaces found");
			return Collections.emptyMap();
		}
	}

	protected String[] tokenize(Element array) {
		return array.getTextContent().trim().split("\\s+");
	}
	
	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}
	
	protected Class<?> getBeanClass(Element element) {
		return MockWebServiceMessageSender.class;
	}

}
