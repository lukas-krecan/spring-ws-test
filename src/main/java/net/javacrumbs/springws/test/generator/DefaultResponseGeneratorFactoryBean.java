package net.javacrumbs.springws.test.generator;

import java.util.Map;

import net.javacrumbs.springws.test.expression.XPathExpressionResolver;
import net.javacrumbs.springws.test.lookup.DefaultResourceLookup;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Simplifies creation of {@link DefaultResponseGenerator}. Could be used like this
 *  <code><pre>
 * &lt;bean class="net.javacrumbs.springws.test.generator.DefaultResponseGeneratorFactoryBean"&gt;
 *	&lt;property name="namespaceMap"&gt;
 *		&lt;map&gt;
 *			&lt;entry key="soapenv" value="http://schemas.xmlsoap.org/soap/envelope/"/&gt;
 *			&lt;entry key="ns" value="http://www.springframework.org/spring-ws/samples/airline/schemas/messages"/&gt;
 *		&lt;/map&gt;
 *	&lt;/property&gt;
 *	&lt;property name="XPathExpressions"&gt;
 *		&lt;list&gt;
 *			&lt;value&gt;concat('mock-responses/',$uri.host, '/', local-name(//soapenv:Body/*[1]),'/',//ns:from,'-',//ns:to,'-response.xml')&lt;/value&gt;
 *			&lt;value&gt;concat('mock-responses/',$uri.host, '/', local-name(//soapenv:Body/*[1]),'/default-response.xml')&lt;/value&gt;
 *		&lt;/list&gt;
 *	&lt;/property&gt;		
 *&lt;/bean&gt;
 *	</pre>	
 * </code>
 * 	
 * @author Lukas Krecan
 *
 */
public class DefaultResponseGeneratorFactoryBean extends AbstractFactoryBean {

	private Map<String, String> namespaceMap;
	
	private String[] xPathExpressions;

	@Override
	protected Object createInstance() throws Exception {
		XPathExpressionResolver expressionResolver = new XPathExpressionResolver();
		expressionResolver.setNamespaceMap(namespaceMap);
		
		DefaultResponseGenerator responseGenerator = new DefaultResponseGenerator();
		DefaultResourceLookup responseResourceLookup = new DefaultResourceLookup();
		responseResourceLookup.setExpressionResolver(expressionResolver);
		responseResourceLookup.setResourceExpressions(xPathExpressions);
		responseGenerator.setResourceLookup(responseResourceLookup);
		return responseGenerator;
	}

	@Override
	public Class<?> getObjectType() {
		return DefaultResponseGenerator.class;
	}

	public String[] getXPathExpressions() {
		return xPathExpressions;
	}

	public void setXPathExpressions(String[] responseXPathExpressions) {
		this.xPathExpressions = responseXPathExpressions;
	}

	public Map<String, String> getNamespaceMap() {
		return namespaceMap;
	}

	public void setNamespaceMap(Map<String, String> namespaceMap) {
		this.namespaceMap = namespaceMap;
	}


}
