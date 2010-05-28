package net.javacrumbs.springws.test.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;


public class MessageGeneratorTest {
	
	
	@Test
	public void testAlwaysCreateEnvelope() throws IOException
	{
		MessageGenerator generator = new MessageGenerator();
		generator.setAlwaysCreateEnvelope(true);
		assertTrue(generator.shouldCreateSoapEnvelope(new ClassPathResource("mock-responses/test/default-response.xml")));
	}
	@Test
	public void testNeverCreateEnvelope() throws IOException
	{
		MessageGenerator generator = new MessageGenerator();
		generator.setNeverCreateEnvelope(true);
		assertFalse(generator.shouldCreateSoapEnvelope(new ClassPathResource("mock-responses/test/default-response-payload.xml")));
	}
	@Test
	public void testDetectCreateEnvelope1() throws IOException
	{
		MessageGenerator generator = new MessageGenerator();
		assertTrue(generator.shouldCreateSoapEnvelope(new ClassPathResource("mock-responses/test/default-response-payload.xml")));
	}
	@Test
	public void testDetectCreateEnvelope2() throws IOException
	{
		MessageGenerator generator = new MessageGenerator();
		assertFalse(generator.shouldCreateSoapEnvelope(new ClassPathResource("mock-responses/test/default-response.xml")));
	}
}
