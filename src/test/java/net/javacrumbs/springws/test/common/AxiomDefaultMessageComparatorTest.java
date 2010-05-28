package net.javacrumbs.springws.test.common;


public class AxiomDefaultMessageComparatorTest extends DefaultMessageComparatorTest {
	@Override
	protected void initializeMessageFactory() {
		initializeAxiomMessageFactory();
	}
}
