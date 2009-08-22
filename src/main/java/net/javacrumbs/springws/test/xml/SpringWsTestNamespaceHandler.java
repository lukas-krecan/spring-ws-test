package net.javacrumbs.springws.test.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class SpringWsTestNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("mock-message-sender", new MockMessageSenderBeanDefinitionParser());        
	}

}
