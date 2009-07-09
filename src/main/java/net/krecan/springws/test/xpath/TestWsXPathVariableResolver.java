package net.krecan.springws.test.xpath;

import java.net.URI;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathVariableResolver;

import net.krecan.springws.test.context.WsTestContextHolder;

import org.springframework.beans.BeanWrapperImpl;

public class TestWsXPathVariableResolver implements XPathVariableResolver {
	private final URI uri;
	
	public TestWsXPathVariableResolver(URI uri) {
		super();
		this.uri = uri;
	}

	public Object resolveVariable(QName variableName) {
		return new BeanWrapperImpl(this).getPropertyValue(variableName.getLocalPart());
	}

	public URI getUri() {
		return uri;
	}
	
	public Map getContext()
	{
		return WsTestContextHolder.getTestContext().getAttributeMap();
	}

}
