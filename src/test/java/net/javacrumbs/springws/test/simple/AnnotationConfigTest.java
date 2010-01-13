package net.javacrumbs.springws.test.simple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:simple-context.xml"})
@TestExecutionListeners({WsMockControlTestExecutionListener.class})
public class AnnotationConfigTest {

	@Autowired
	private WsMockControl mockControl;
	
	@Test
	public void testConfiguration()
	{
		
	}
}
