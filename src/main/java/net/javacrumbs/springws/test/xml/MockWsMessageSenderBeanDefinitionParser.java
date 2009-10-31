package net.javacrumbs.springws.test.xml;

import java.util.Collections;
import java.util.Map;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.generator.PayloadRootBasedResponseGeneratorFactoryBean;
import net.javacrumbs.springws.test.validator.PayloadRootBasedXmlCompareRequestValidatorFactoryBean;
import net.javacrumbs.springws.test.validator.SchemaRequestValidator;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class MockWsMessageSenderBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
		Map<?, ?> namespaces = parseNamespaces(element, parserContext, bean);
		Map<?, ?> discriminators = parseDiscriminators(element, parserContext, bean);
		String pathPrefix = DomUtils.getChildElementByTagName(element, "resource-config").getAttribute("pathPrefix");
		String prependUri = DomUtils.getChildElementByTagName(element, "resource-config").getAttribute("prependUri");
		
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
		Element namespaces = DomUtils.getChildElementByTagName(element, "discriminators");
		if (namespaces != null) {
			return parserContext.getDelegate().parseMapElement(namespaces, bean.getRawBeanDefinition());
		}
		else
		{
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
