package net.javacrumbs.springws.test.xml;

import net.javacrumbs.springws.test.validator.SchemaRequestValidator;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class SchemaRequestValidatorBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	
	private static final String DELIMITERS = "\n";

	protected Class<?> getBeanClass(Element element) {
		return SchemaRequestValidator.class;
	}

	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		parseRequestValidationSchemas(element, bean);
	}
	
	private void parseRequestValidationSchemas(Element element, BeanDefinitionBuilder bean) {
		Element schemas = DomUtils.getChildElementByTagName(element, "schemas");
		if (schemas != null) {
			String[] schemaArray = tokenize(schemas);
			bean.addPropertyValue("schemas", schemaArray);
		}
	}
	

	protected String[] tokenize(Element controlRequestXPaths) {
		return StringUtils.tokenizeToStringArray(controlRequestXPaths.getTextContent(), DELIMITERS);
	}
	
	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

}
