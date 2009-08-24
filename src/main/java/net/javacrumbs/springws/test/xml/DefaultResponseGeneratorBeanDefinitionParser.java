package net.javacrumbs.springws.test.xml;


import net.krecan.springws.test.generator.DefaultResponseGeneratorFactoryBean;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

/**
 * Support for swst schema. Example:
 * <code><pre>
 * &lt;swst:response-generator&gt;
 *	&lt;swst:namespaces&gt;
 *		soapenv=http://schemas.xmlsoap.org/soap/envelope/
 *		ns=http://www.springframework.org/spring-ws/samples/airline/schemas/messages
 *	&lt;/swst:namespaces&gt;
 *	&lt;swst:xpaths&gt;
 *		concat('mock-responses/',$uri.host, '/', local-name(//soapenv:Body/*[1]),'/',//ns:from,'-',//ns:to,'-response.xml')
 *		concat('mock-responses/',$uri.host, '/', local-name(//soapenv:Body/*[1]),'/default-response.xml')
 *	&lt;/swst:xpaths&gt;
 *&lt;/swst:response-generator&gt;
 * </pre></code>
 * @author Lukas Krecan
 *
 */
public class DefaultResponseGeneratorBeanDefinitionParser extends AbstractNamespaceParsingBeanDefinitionParser {
	protected Class<?> getBeanClass(Element element) {
		return DefaultResponseGeneratorFactoryBean.class;
	}

	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		parseXPaths(element, bean);
		parseNamespaces(element, bean);
	}



}
