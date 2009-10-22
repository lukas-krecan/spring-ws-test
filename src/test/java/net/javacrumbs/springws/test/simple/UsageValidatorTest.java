package net.javacrumbs.springws.test.simple;

import static org.junit.Assert.*;

import java.io.IOException;

import net.javacrumbs.springws.test.WsTestException;

import org.junit.Test;


public class UsageValidatorTest {

	@Test
	public void testUsage() throws IOException
	{
		UsageValidator validator = new UsageValidator();
		
		try
		{
			validator.verify();
			fail("WsTestException expected");
		} 
		catch(WsTestException e)
		{
			assertEquals("Unexpected number of WebServiceTemplate calls, expected from 1 to 1 calls, was 0.",e.getMessage());
		}
		
		validator.processRequest(null, null, null);
		validator.verify();
		
		try
		{
			validator.processRequest(null, null, null);
			fail("WsTestException expected");
		} 
		catch(WsTestException e)
		{
			assertEquals("Unexpected number of WebServiceTemplate calls, expected from 1 to 1 calls, was 2.",e.getMessage());
		}
	}
	
	
	@Test
	public void testUsage2() throws IOException
	{
		UsageValidator validator = new UsageValidator();
		validator.setMinNumberOfProcessedRequests(0);
		validator.setMaxNumberOfProcessedRequests(2);
		
		validator.verify();
		
		validator.processRequest(null, null, null);
		validator.verify();
	
		validator.processRequest(null, null, null);
		validator.verify();
		
		try
		{
			validator.processRequest(null, null, null);
			fail("WsTestException expected");
		} 
		catch(WsTestException e)
		{
			assertEquals("Unexpected number of WebServiceTemplate calls, expected from 0 to 2 calls, was 3.",e.getMessage());
		}
	}
	
}
