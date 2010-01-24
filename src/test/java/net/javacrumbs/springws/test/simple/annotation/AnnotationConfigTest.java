package net.javacrumbs.springws.test.simple.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;

import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.simple.WsMockControl;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceOperations;
import org.springframework.xml.transform.StringResult;
import org.xml.sax.SAXException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:simple-context.xml"})
@TestExecutionListeners({WsMockControlTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class AnnotationConfigTest extends AbstractMessageTest{

	@Autowired
	private WsMockControl mockControl;
	
	@Autowired
	private WebServiceOperations template;
	
	@Before
	public void setUp()
	{
		mockControl.validateSchema("xml/schema.xsd");
	}
	
	
	@Test
	public void testConfiguration() throws WebServiceClientException, IOException, SAXException
	{
		testResponse("xml/resolved-different-response.xml");
	}

	@Test
	public void testOtherConfiguration() throws WebServiceClientException, IOException, SAXException
	{
		testResponse("mock-responses/test/default-response-payload.xml");
	}
	
	@After
	public void tearDown()
	{
		assertEquals(2, mockControl.getRequestProcessors().size());
	}

	private void testResponse(String response) throws IOException, SAXException {
		mockControl.returnResponse(response);
		
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult(TEST_URI.toString(),createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
		
		Diff diff = XMLUnit.compareXML(new InputStreamReader(new ClassPathResource(response).getInputStream()), responseResult.toString());
		assertTrue(diff.toString(), diff.similar());
	}
	
	
}
