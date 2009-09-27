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