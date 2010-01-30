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
package net.javacrumbs.springws.test.lookup;

import java.io.IOException;
import java.net.URI;

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;

/**
 * Returns resource set in the constructor. Calls processResource method before returning.  
 * @author Lukas Krecan
 *
 */
public class SimpleResourceLookup extends AbstractTemplateProcessingResourceLookup {

	private final Resource resultResource;
	
	public SimpleResourceLookup(Resource resultResource) {
		this.resultResource = resultResource;
	}

	public Resource lookupResource(URI uri, WebServiceMessage message) throws IOException {
		return processResource(uri, message, resultResource);
	}

	public Resource getResultResource() {
		return resultResource;
	}

}
