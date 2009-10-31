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

import java.util.Map;

import net.javacrumbs.springws.test.validator.XPathRequestValidator;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Abstract FactoryBean for creating {@link AbstractExpressionEvaluator}s.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractExpressionEvaluatorFactoryBean extends AbstractFactoryBean {

	private Map<String, String> namespaceMap;
	private Map<String, String> exceptionMapping;
	
	private int order;

	public AbstractExpressionEvaluatorFactoryBean() {
		super();
	}

	@Override
	protected Object createInstance() throws Exception {
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		
		AbstractExpressionEvaluator evaluator = createEvaluator();
		evaluator.setExpressionResolver(expressionResolver);
		evaluator.setExceptionMapping(exceptionMapping);
		return evaluator;
	}

	protected abstract AbstractExpressionEvaluator createEvaluator();

	@Override
	public Class<?> getObjectType() {
		return XPathRequestValidator.class;
	}

	public Map<String, String> getNamespaceMap() {
		return namespaceMap;
	}

	public void setNamespaceMap(Map<String, String> namespaceMap) {
		this.namespaceMap = namespaceMap;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Map<String, String> getExceptionMapping() {
		return exceptionMapping;
	}

	public void setExceptionMapping(Map<String, String> exceptionMapping) {
		this.exceptionMapping = exceptionMapping;
	}

}