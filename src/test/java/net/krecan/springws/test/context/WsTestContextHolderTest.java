package net.krecan.springws.test.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WsTestContextHolderTest {

	@Test
	public void testStoreAttribute() {
		WsTestContextHolder.getTestContext().setAttribute("name", "value");
		assertEquals("value", WsTestContextHolder.getTestContext().getAttribute("name"));
		WsTestContextHolder.getTestContext().clear();
		assertNull("value", WsTestContextHolder.getTestContext().getAttribute("name"));
	}
}
