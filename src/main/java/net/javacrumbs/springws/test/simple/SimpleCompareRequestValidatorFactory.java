package net.javacrumbs.springws.test.simple;

import net.javacrumbs.springws.test.ResponseGenerator;
import net.javacrumbs.springws.test.expression.SimpleExpressionResolver;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;
import net.javacrumbs.springws.test.validator.XmlCompareRequestValidator;

public class SimpleCompareRequestValidatorFactory extends SimpleMessageFactory {

	private final String resourceName;

	public SimpleCompareRequestValidatorFactory(String resourceName, SimpleMessageFactory previousFactory) {
		super(previousFactory);
		this.resourceName = resourceName;
	}

	protected ResponseGenerator createResponseGenerator() {
		XmlCompareRequestValidator validator = new XmlCompareRequestValidator();
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setExpressionResolver(new SimpleExpressionResolver(resourceName));
		validator.setControlResourceLookup(resourceLookup);
		return validator;
	}
}
