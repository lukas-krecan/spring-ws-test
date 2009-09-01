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
package net.javacrumbs.springws.test.validator;

import java.util.Map;

import net.javacrumbs.springws.test.expression.XPathExpressionResolver;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Simplifies creation of {@link DefaultResponseGenerator}.
 * @author Lukas Krecan
 *
 */
public class XmlCompareRequestValidatorFactoryBean extends AbstractFactoryBean {

	private Map<String, String> namespaceMap;
	
	private String[] xPathExpressions;

	@Override
	protected Object createInstance() throws Exception {
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		
		XmlCompareRequestValidator responseGenerator = new XmlCompareRequestValidator();
		DefaultResourceLookup reourceLookup = new DefaultResourceLookup();
		reourceLookup.setExpressionResolver(expressionResolver);
		reourceLookup.setResourceExpressions(getXPathExpressions());
		responseGenerator.setControlResourceLookup(reourceLookup);
		return responseGenerator;
	}

	@Override
	public Class<?> getObjectType() {
		return XmlCompareRequestValidator.class;
	}

	public String[] getXPathExpressions() {
		return xPathExpressions;
	}

	public void setXPathExpressions(String[] responseXPathExpressions) {
		this.xPathExpressions = responseXPathExpressions;
	}

	public Map<String, String> getNamespaceMap() {
		return namespaceMap;
	}

	public void setNamespaceMap(Map<String, String> namespaceMap) {
		this.namespaceMap = namespaceMap;
	}


}
