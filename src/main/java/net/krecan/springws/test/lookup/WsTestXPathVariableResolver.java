package net.krecan.springws.test.lookup;

import java.net.URI;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathVariableResolver;

import net.krecan.springws.test.context.WsTestContextHolder;

import org.springframework.beans.BeanWrapperImpl;

/**
 * Resolves XPath variables.
 * @author Lukas Krecan
 *
 */
public class WsTestXPathVariableResolver implements XPathVariableResolver {
	private final URI uri;
	
	public WsTestXPathVariableResolver(URI uri) {
		super();
		this.uri = uri;
	}

	public Object resolveVariable(QName variableName) {
		return new BeanWrapperImpl(this).getPropertyValue(variableName.getLocalPart());
	}

	public URI getUri() {
		return uri;
	}
	
	public Map<String, Object> getContext()
	{
		return WsTestContextHolder.getTestContext().getAttributeMap();
	}

}
