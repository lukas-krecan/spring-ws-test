package net.javacrumbs.springws.test.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class SpringWsTestNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("mock-message-sender", new MockWebServiceMessageSenderBeanDefinitionParser());        
		registerBeanDefinitionParser("response-generator", new DefaultResponseGeneratorBeanDefinitionParser());        
		registerBeanDefinitionParser("compare-request-validator", new XmlCompareRequestValidatorBeanDefinitionParser());        
		registerBeanDefinitionParser("schema-validator", new SchemaRequestValidatorBeanDefinitionParser());        
	}

}
