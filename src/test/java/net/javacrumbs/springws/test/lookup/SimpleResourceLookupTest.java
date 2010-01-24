package net.javacrumbs.springws.test.lookup;

import static org.junit.Assert.*;

import java.io.IOException;

import net.javacrumbs.springws.test.AbstractMessageTest;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public class SimpleResourceLookupTest extends AbstractMessageTest
{
	@Test
	public void testSimple() throws IOException
	{
		SimpleResourceLookup lookup = new SimpleResourceLookup(new ClassPathResource("xml/valid-message2.xml"));
		Resource res = lookup.lookupResource(null, null);
		assertNotNull(res);
		assertEquals("valid-message2.xml", res.getFilename());
	}
	@Test
	public void testTemplate() throws IOException
	{
		SimpleResourceLookup lookup = new SimpleResourceLookup(new ClassPathResource("xml/valid-message2.xml"));
		Resource res = lookup.lookupResource(null, null);
		assertNotNull(res);
		assertEquals("valid-message2.xml", res.getFilename());
	}
	
	
	
}
