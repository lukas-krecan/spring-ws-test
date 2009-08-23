package net.javacrumbs.springws.test.xml;

import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public abstract class AbstractNamespaceParsingBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	private static final String DELIMITERS = "\n";

	protected String[] tokenize(Element controlRequestXPaths) {
		return StringUtils.tokenizeToStringArray(controlRequestXPaths.getTextContent(), DELIMITERS);
	}

	protected void parseNamespaces(Element element, BeanDefinitionBuilder bean) {
		Element namespaces = DomUtils.getChildElementByTagName(element, "namespaces");
		if (namespaces != null) {
			Map<Object, Object> controlRequestXpathExpressions = StringUtils.splitArrayElementsIntoProperties(
					tokenize(namespaces), "=");
			bean.addPropertyValue("namespaceMap", controlRequestXpathExpressions);
		}
	}
	
	protected void parseXPaths(Element element, BeanDefinitionBuilder bean) {
		Element responseXPaths = DomUtils.getChildElementByTagName(element, "xpaths");
		if (responseXPaths != null) {
			String[] responseXPathExpressions = tokenize(responseXPaths);
			bean.addPropertyValue("XPathExpressions", responseXPathExpressions);
		}
	}
	
	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

}