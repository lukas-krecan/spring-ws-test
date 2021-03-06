/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.springws.test.simple.annotation;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;

import net.javacrumbs.springws.test.AbstractMessageTest;
import net.javacrumbs.springws.test.NoResponseGeneratorSpecifiedException;
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

	@Test(expected=NoResponseGeneratorSpecifiedException.class)
	public void testNoConfiguration() throws WebServiceClientException, IOException, SAXException
	{
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult(TEST_URI.toString(),createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
	}
	
	
	
	@After
	public void tearDown()
	{
		assertTrue(mockControl.getRequestProcessors().size()>=1);
	}

	private void testResponse(String response) throws IOException, SAXException {
		mockControl.returnResponse(response);
		
		StringResult responseResult = new StringResult();
		template.sendSourceAndReceiveToResult(TEST_URI.toString(),createMessage("xml/valid-message.xml").getPayloadSource(), responseResult );
		
		Diff diff = XMLUnit.compareXML(new InputStreamReader(new ClassPathResource(response).getInputStream()), responseResult.toString());
		assertTrue(diff.toString(), diff.similar());
	}
	
	
}
