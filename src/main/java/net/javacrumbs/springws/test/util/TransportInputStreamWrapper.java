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
	public Iterator<?> getHeaderNames() throws IOException {
		return Collections.emptyList().iterator();
	}

	@Override
	public Iterator<?> getHeaders(String name) throws IOException {
		return Collections.emptyList().iterator();
	}
}