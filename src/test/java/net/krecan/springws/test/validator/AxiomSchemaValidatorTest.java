package net.krecan.springws.test.validator;

public class AxiomSchemaValidatorTest extends SchemaValidatorTest {

	public AxiomSchemaValidatorTest() throws Exception {
		super();
	}

	@Override
	protected void initializeMessageFactory() {
		initializeAxiomMessageFactory();
	}
}
