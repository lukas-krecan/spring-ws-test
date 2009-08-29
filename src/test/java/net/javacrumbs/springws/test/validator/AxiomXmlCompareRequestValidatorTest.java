package net.javacrumbs.springws.test.validator;


/**
 * Does the same tests as {@link XmlCompareRequestValidatorTest} using Axiom.
 * @author Lukas Krecan
 *
 */
public class AxiomXmlCompareRequestValidatorTest extends XmlCompareRequestValidatorTest{
	
	@Override
	protected void initializeMessageFactory() {
		initializeAxiomMessageFactory();
	}



}
