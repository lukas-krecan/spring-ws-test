package net.javacrumbs.springws.test.expression;

import java.net.URI;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathVariableResolver;

import net.javacrumbs.springws.test.context.WsTestContextHolder;

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
		String property = variableName.getLocalPart();
		property = normalizeContextVariable(property);
		return new BeanWrapperImpl(this).getPropertyValue(property);
	
	}
	/**
	 * Converts context.property to context[property]
	 * @param property
	 * @return
	 */
	private String normalizeContextVariable(String property) {
		if (property.startsWith("context.")) {
			property = "context["+property.substring(8)+"]";
		}
		return property;
	}

	public URI getUri() {
		return uri;
	}
	
	public Map<String, Object> getContext()
	{
		return WsTestContextHolder.getTestContext().getAttributeMap();
	}

}
