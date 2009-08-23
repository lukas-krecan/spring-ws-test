package net.javacrumbs.springws.test.xml;

import net.krecan.springws.test.MockWebServiceMessageSender;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class MockWebServiceMessageSenderBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
//	private static final String XPATH_DELIMITERS = "\n";


	protected Class<?> getBeanClass(Element element) {
		return MockWebServiceMessageSender.class;
	}

	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		
	}
	
	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}
	

//	private void parseResponseXPaths(Element element, BeanDefinitionBuilder bean) {
//		Element responseXPaths = DomUtils.getChildElementByTagName(element, "response-xpaths");
//		if (responseXPaths != null) {
//			String[] responseXPathExpressions = tokenize(responseXPaths);
//			bean.addPropertyValue("responseXPathExpressions", responseXPathExpressions);
//		}
//	}
//
//	private void parseControlRequestXPaths(Element element, BeanDefinitionBuilder bean) {
//		Element controlRequestXPaths = DomUtils.getChildElementByTagName(element, "control-request-xpaths");
//		if (controlRequestXPaths != null) {
//			String[] controlRequestXpathExpressions = tokenize(controlRequestXPaths);
//			bean.addPropertyValue("controlRequestXPathExpressions", controlRequestXpathExpressions);
//		}
//	}
//
//	private String[] tokenize(Element controlRequestXPaths) {
//		return StringUtils.tokenizeToStringArray(controlRequestXPaths.getTextContent(), XPATH_DELIMITERS);
//	}
//	
//	private void parseNamespaces(Element element, BeanDefinitionBuilder bean) {
//		Element namespaces = DomUtils.getChildElementByTagName(element, "namespaces");
//		if (namespaces != null) {
//			Map<Object,Object> controlRequestXpathExpressions = StringUtils.splitArrayElementsIntoProperties(tokenize(namespaces), "=");
//			bean.addPropertyValue("namespaceMap", controlRequestXpathExpressions);
//		}
//	}
//	private void parseRequestValidationSchemas(Element element, BeanDefinitionBuilder bean) {
//		Element schemas = DomUtils.getChildElementByTagName(element, "request-validation-schemas");
//		if (schemas != null) {
//			String[] schemaArray = tokenize(schemas);
//			bean.addPropertyValue("requestValidationSchemas", schemaArray);
//		}
//	}


}
