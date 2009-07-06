package net.krecan.springws.test;

import java.net.URI;

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;

/**
 * Searches for resources and loads it.
 * 
 * @author Lukas Krecan
 * 
 */
public interface ResourceLookup {
	/**
	 * Looks for appropriate resource.
	 * 
	 * @param uri
	 * @param message
	 * @return null if not found.
	 */
	public Resource lookupResource(URI uri, WebServiceMessage message);
}
