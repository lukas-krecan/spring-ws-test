package net.javacrumbs.springws.test.validator;

public class AxiomSchemaValidatorTest extends SchemaRequestValidatorTest {

	public AxiomSchemaValidatorTest() throws Exception {
		super();
	}

	@Override
	protected void initializeMessageFactory() {
		initializeAxiomMessageFactory();
	}
}
