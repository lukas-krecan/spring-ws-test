package net.krecan.springws.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.WebServiceOperations;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:simple-context.xml")
public class SampleTest {
	@Autowired
	private WebServiceOperations template;
	
	@Test
	public void testSendMessage()
	{
		Dummy dummy = new Dummy();
		dummy.setField1("text");
		dummy.setField2(1);
		
		template.marshalSendAndReceive(dummy);
	}
}
