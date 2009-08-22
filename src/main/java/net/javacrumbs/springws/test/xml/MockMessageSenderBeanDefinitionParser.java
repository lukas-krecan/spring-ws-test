package net.javacrumbs.springws.test.xml;

import net.krecan.springws.test.MockWebServiceMessageSender;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class MockMessageSenderBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	    protected Class<?> getBeanClass(Element element) {
	      return MockWebServiceMessageSender.class; 
	   }

	   protected void doParse(Element element, BeanDefinitionBuilder bean) {
	      
	      
	   }

}
