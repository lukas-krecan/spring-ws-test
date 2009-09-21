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

import java.util.Map;

import net.javacrumbs.springws.test.validator.XPathRequestValidatorFactoryBean;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class XPathRequestValidatorBeanDefinitionParser extends AbstractNamespaceParsingBeanDefinitionParser {
	

	protected Class<?> getBeanClass(Element element) {
		return XPathRequestValidatorFactoryBean.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
		parseNamespaces(element, bean);
		parseExceptionMapping(element, parserContext, bean);
	}
	
	private void parseExceptionMapping(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
		Element exceptionMapping = DomUtils.getChildElementByTagName(element, "exceptionMapping");
		if (exceptionMapping != null) {
			Map<?, ?> parsedMap = parserContext.getDelegate().parseMapElement(exceptionMapping, bean.getRawBeanDefinition());
			bean.addPropertyValue("exceptionMapping", parsedMap);
		}
	}
	
	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

}
