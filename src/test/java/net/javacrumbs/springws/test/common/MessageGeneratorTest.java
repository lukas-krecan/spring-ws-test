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

package net.javacrumbs.springws.test.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;


public class MessageGeneratorTest {
	
	
	@Test
	public void testAlwaysCreateEnvelope() throws IOException
	{
		MessageGenerator generator = new MessageGenerator();
		generator.setAlwaysCreateEnvelope(true);
		assertTrue(generator.shouldCreateSoapEnvelope(new ClassPathResource("mock-responses/test/default-response.xml")));
	}
	@Test
	public void testNeverCreateEnvelope() throws IOException
	{
		MessageGenerator generator = new MessageGenerator();
		generator.setNeverCreateEnvelope(true);
		assertFalse(generator.shouldCreateSoapEnvelope(new ClassPathResource("mock-responses/test/default-response-payload.xml")));
	}
	@Test
	public void testDetectCreateEnvelope1() throws IOException
	{
		MessageGenerator generator = new MessageGenerator();
		assertTrue(generator.shouldCreateSoapEnvelope(new ClassPathResource("mock-responses/test/default-response-payload.xml")));
	}
	@Test
	public void testDetectCreateEnvelope2() throws IOException
	{
		MessageGenerator generator = new MessageGenerator();
		assertFalse(generator.shouldCreateSoapEnvelope(new ClassPathResource("mock-responses/test/default-response.xml")));
	}
}
