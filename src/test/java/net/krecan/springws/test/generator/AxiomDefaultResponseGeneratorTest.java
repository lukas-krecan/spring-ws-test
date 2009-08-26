package net.krecan.springws.test.generator;

public class AxiomDefaultResponseGeneratorTest extends DefaultResponseGeneratorTest {
	@Override
	protected void initializeMessageFactory() {
		initializeAxiomMessageFactory();
	}
}
