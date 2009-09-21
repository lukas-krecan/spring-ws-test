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

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public abstract class AbstractNamespaceParsingBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	private static final String DELIMITERS = "\n";

	protected String[] tokenize(Element array) {
		return StringUtils.tokenizeToStringArray(array.getTextContent(), DELIMITERS);
	}

	protected void parseNamespaces(Element element, BeanDefinitionBuilder bean) {
		Element namespaces = DomUtils.getChildElementByTagName(element, "namespaces");
		if (namespaces != null) {
			Map<Object, Object> controlRequestXpathExpressions = StringUtils.splitArrayElementsIntoProperties(
					tokenize(namespaces), "=");
			bean.addPropertyValue("namespaceMap", controlRequestXpathExpressions);
		}
	}
	
	protected void parseXPaths(Element element, BeanDefinitionBuilder bean) {
		Element responseXPaths = DomUtils.getChildElementByTagName(element, "xpaths");
		if (responseXPaths != null) {
			String[] responseXPathExpressions = tokenize(responseXPaths);
			bean.addPropertyValue("XPathExpressions", responseXPathExpressions);
		}
	}
	
	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

}