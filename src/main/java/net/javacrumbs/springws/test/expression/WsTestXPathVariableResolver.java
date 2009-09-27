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
package net.javacrumbs.springws.test.expression;

import java.net.URI;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathVariableResolver;

import net.javacrumbs.springws.test.context.WsTestContextHolder;

import org.springframework.beans.BeanWrapperImpl;

/**
 * Resolves XPath variables. Supports uri and context XPath variables.
 * @author Lukas Krecan
 *
 */
public class WsTestXPathVariableResolver implements XPathVariableResolver {
	private final URI uri;
	
	public WsTestXPathVariableResolver(URI uri) {
		super();
		this.uri = uri;
	}

	public Object resolveVariable(QName variableName) {
		String property = variableName.getLocalPart();
		property = normalizeContextVariable(property);
		return new BeanWrapperImpl(this).getPropertyValue(property);
	
	}
	/**
	 * Converts context.property to context[property]
	 * @param property
	 * @return
	 */
	String normalizeContextVariable(String property) {
		if (property.startsWith("context.")) {
			int endIndex = property.indexOf('.', 8);
			endIndex = endIndex!=-1?endIndex:property.length();
			property = "context["+property.substring(8,endIndex)+"]"+property.substring(endIndex);
		}
		return property;
	}

	public URI getUri() {
		return uri;
	}
	
	public Map<String, Object> getContext()
	{
		return WsTestContextHolder.getTestContext().getAttributeMap();
	}

}
