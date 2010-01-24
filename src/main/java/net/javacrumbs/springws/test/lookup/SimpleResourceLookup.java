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
