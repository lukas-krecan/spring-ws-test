package net.javacrumbs.springws.test.simple;

import net.javacrumbs.springws.test.ResponseGenerator;
import net.javacrumbs.springws.test.expression.SimpleExpressionResolver;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;

public class SimpleResponseGeneratorFactory extends SimpleMessageFactory {

	private final String resourceName;

	public SimpleResponseGeneratorFactory(String resourceName, SimpleMessageFactory previousFactory) {
		super(previousFactory);
		this.resourceName = resourceName;
	}

	@Override
	protected ResponseGenerator createResponseGenerator() {
		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		DefaultResourceLookup resourceLookup = new DefaultResourceLookup();
		resourceLookup.setExpressionResolver(new SimpleExpressionResolver(resourceName));
		responseGenerator.setResourceLookup(resourceLookup);
		return responseGenerator;
	}
}
