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
package net.javacrumbs.springws.test.validator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;

import net.javacrumbs.springws.test.WsTestException;
import net.javacrumbs.springws.test.lookup.ResourceLookup;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;



public class XmlCompareRequestValidatorTest extends AbstractValidatorTest {
	@Test
	public void testNonExistingControlDocument() throws Exception
	{
		WebServiceMessage message = getValidMessage();
		
		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, message)).andReturn(null);
		
		XmlCompareRequestValidator validator = new XmlCompareRequestValidator(){
			@Override
			protected void compareMessage(WebServiceMessage message, Resource controlResource) throws IOException {
				fail("Should not get here");
			}
			
		};
		validator.setControlResourceLookup(resourceLookup);
		validator.afterPropertiesSet();
		
		replay(resourceLookup);
		
		validator.validateRequest(null, message);
		
		verify(resourceLookup);
	}
	@Test(expected=WsTestException.class)
	public void testFailIfNotFOund() throws Exception
	{
		WebServiceMessage message = getValidMessage();
		
		ResourceLookup resourceLookup = createMock(ResourceLookup.class);
		expect(resourceLookup.lookupResource(null, message)).andReturn(null);
		
		XmlCompareRequestValidator validator = new XmlCompareRequestValidator(){
			@Override
			protected void compareMessage(WebServiceMessage message, Resource controlResource) throws IOException {
				fail("Should not get here");
			}
		};
		validator.setControlResourceLookup(resourceLookup);
		validator.setFailIfControlResourceNotFound(true);
		validator.afterPropertiesSet();
		
		replay(resourceLookup);
		
		validator.validateRequest(null, message);

	}


}
