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
package net.javacrumbs.springws.test.xml;

import java.util.Collections;
import java.util.Map;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.generator.PayloadRootBasedResponseGeneratorFactoryBean;
import net.javacrumbs.springws.test.util.MockMessageSenderInjector;
import net.javacrumbs.springws.test.validator.PayloadRootBasedXmlCompareRequestValidatorFactoryBean;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;

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
		String pathPrefix = DomUtils.getChildElementByTagName(element, "resource-config").getAttribute("pathPrefix");
		String prependUri = DomUtils.getChildElementByTagName(element, "resource-config").getAttribute("prependUri");
		
		bean.addPropertyValue("autowireRequestProcessors", element.getAttribute("autowireRequestProcessors"));
		
		ManagedList requestProcessors = new ManagedList();	
				
		BeanDefinitionBuilder xmlCompareRequestValidator = BeanDefinitionBuilder.rootBeanDefinition(PayloadRootBasedXmlCompareRequestValidatorFactoryBean.class);
		xmlCompareRequestValidator.addPropertyValue("namespaceMap", namespaces);
		xmlCompareRequestValidator.addPropertyValue("discriminators", discriminators);
		xmlCompareRequestValidator.addPropertyValue("pathPrefix", pathPrefix);
		xmlCompareRequestValidator.addPropertyValue("prependUri", prependUri);
		addRequestProcessor(requestProcessors, xmlCompareRequestValidator);
		
		String[] schemas = parseRequestValidationSchemas(element, bean);
		if (schemas!=null)
		{
			BeanDefinitionBuilder schemaRequestValidator = BeanDefinitionBuilder.rootBeanDefinition(SchemaRequestValidator.class);
			schemaRequestValidator.addPropertyValue("schemas", schemas);
			addRequestProcessor(requestProcessors, schemaRequestValidator);
		}
		
		BeanDefinitionBuilder defaultResponseGenerator = BeanDefinitionBuilder.rootBeanDefinition(PayloadRootBasedResponseGeneratorFactoryBean.class);
		defaultResponseGenerator.addPropertyValue("namespaceMap", namespaces);
		defaultResponseGenerator.addPropertyValue("discriminators", discriminators);
		defaultResponseGenerator.addPropertyValue("pathPrefix", pathPrefix);
		defaultResponseGenerator.addPropertyValue("prependUri", prependUri);
		addRequestProcessor(requestProcessors, defaultResponseGenerator);
		
		bean.addPropertyValue("requestProcessors", requestProcessors);
		
		if (TRUE.equals(element.getAttribute("autoinjectMock")))
		{
			AbstractBeanDefinition injector = BeanDefinitionBuilder.rootBeanDefinition(MockMessageSenderInjector.class).getBeanDefinition();
			BeanDefinitionHolder holder = new BeanDefinitionHolder(injector, parserContext.getReaderContext().generateBeanName(injector));
			registerBeanDefinition(holder, parserContext.getRegistry());
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
