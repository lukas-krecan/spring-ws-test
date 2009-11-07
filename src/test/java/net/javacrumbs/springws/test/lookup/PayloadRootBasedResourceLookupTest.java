package net.javacrumbs.springws.test.lookup;

import static org.junit.Assert.assertArrayEquals;

import java.util.Collections;

import org.junit.Test;


public class PayloadRootBasedResourceLookupTest {

	@Test
	public void testParseDiscriminators()
	{
		PayloadRootBasedResourceLookup lookup = new PayloadRootBasedResourceLookup();
		
		lookup.setDiscriminators(Collections.singletonMap("test", "//ns:text	; //ns:number"));
		assertArrayEquals(new String[]{"//ns:text","//ns:number"}, lookup.getDiscriminatorsMap().get("test"));
		
		lookup.setDiscriminators(Collections.singletonMap("test", "//ns:text;//ns:number"));
		assertArrayEquals(new String[]{"//ns:text","//ns:number"}, lookup.getDiscriminatorsMap().get("test"));
	}
}
