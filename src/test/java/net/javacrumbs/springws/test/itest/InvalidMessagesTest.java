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
package net.javacrumbs.springws.test.itest;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.transform.Source;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;
import net.javacrumbs.springws.test.generator.DefaultResponseGenerator;
import net.javacrumbs.springws.test.lookup.SimpleResourceLookup;
import net.javacrumbs.springws.test.template.TemplateProcessor;
import net.javacrumbs.springws.test.validator.AbstractValidatorTest;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringResult;


public class InvalidMessagesTest extends AbstractValidatorTest{

	@Test
	public void testInvalidResponse() throws IOException
	{
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setCheckConnectionForFault(false);
		MockWebServiceMessageSender messageSender = new MockWebServiceMessageSender();
		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		responseGenerator.setNeverCreateEnvelope(true);
		SimpleResourceLookup resourceLookup = new SimpleResourceLookup(new ClassPathResource("mock-responses/test/default-response-invalid.xml"));
		resourceLookup.setTemplateProcessor(TemplateProcessor.DUMMY_TEMPLATE_PROCESSOR);
		responseGenerator.setResourceLookup(resourceLookup);
		messageSender.setRequestProcessors(Arrays.asList(responseGenerator));
		webServiceTemplate.setMessageSender(messageSender);
		
		Source source = getValidMessage().getPayloadSource(); 
		StringResult result = new StringResult();
		//TODO uncomment
//		webServiceTemplate.sendSourceAndReceiveToResult(TEST_URI.toString(), source, result);		
	}
}
