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


import net.javacrumbs.springws.test.generator.DefaultResponseGeneratorFactoryBean;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

/**
 * Support for swst schema. Example:
 * <code><pre>
 * &lt;swst:response-generator&gt;
 *	&lt;swst:namespaces&gt;
 *		soapenv=http://schemas.xmlsoap.org/soap/envelope/
 *		ns=http://www.springframework.org/spring-ws/samples/airline/schemas/messages
 *	&lt;/swst:namespaces&gt;
 *	&lt;swst:xpaths&gt;
 *		concat('mock-responses/',$uri.host, '/', local-name(//soapenv:Body/*[1]),'/',//ns:from,'-',//ns:to,'-response.xml')
 *		concat('mock-responses/',$uri.host, '/', local-name(//soapenv:Body/*[1]),'/default-response.xml')
 *	&lt;/swst:xpaths&gt;
 *&lt;/swst:response-generator&gt;
 * </pre></code>
 * @author Lukas Krecan
 *
 */
public class DefaultResponseGeneratorBeanDefinitionParser extends AbstractNamespaceParsingBeanDefinitionParser {
	protected Class<?> getBeanClass(Element element) {
		return DefaultResponseGeneratorFactoryBean.class;
	}

	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		parseXPaths(element, bean);
		parseNamespaces(element, bean);
	}



}
