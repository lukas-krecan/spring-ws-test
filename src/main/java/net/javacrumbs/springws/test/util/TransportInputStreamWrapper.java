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
/**
 * 
 */
package net.javacrumbs.springws.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;

import org.springframework.core.io.Resource;
import org.springframework.ws.transport.TransportInputStream;

/**
 * Wraps an input stream and exposes it as {@link TransportInputStream}.
 * @author Lukas Krecan
 *
 */
public class TransportInputStreamWrapper extends TransportInputStream {
	private final Resource resultResource;

	public TransportInputStreamWrapper(Resource resultResource) {
		this.resultResource = resultResource;
	}

	@Override
	protected InputStream createInputStream() throws IOException {
		return resultResource.getInputStream();
	}

	@Override
	public Iterator<String> getHeaderNames() throws IOException {
		return Collections.<String>emptyList().iterator();
	}

	@Override
	public Iterator<String> getHeaders(String name) throws IOException {
		return Collections.<String>emptyList().iterator();
	}
}