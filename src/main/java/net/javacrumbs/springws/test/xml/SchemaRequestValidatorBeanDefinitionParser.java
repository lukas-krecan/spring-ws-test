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
