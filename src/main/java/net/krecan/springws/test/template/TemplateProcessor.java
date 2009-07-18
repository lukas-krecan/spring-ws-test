package net.krecan.springws.test.template;

import java.io.IOException;
import java.net.URI;

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;

public interface TemplateProcessor {
	/**
	 * Processes the resource as template. Returns resource with processed data. If the processor is not applicable 
	 * returns original resource or its copy.
	 * @param resource
	 * @param uri
	 * @param message
	 * @return
	 * @throws IOException 
	 */
	public Resource processTemplate(Resource resource, URI uri, WebServiceMessage message) throws IOException;
}
