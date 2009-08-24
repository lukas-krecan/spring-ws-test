package net.javacrumbs.springws.test.xml;

import net.krecan.springws.test.validator.XmlCompareRequestValidatorFactoryBean;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

public class XmlCompareRequestValidatorBeanDefinitionParser extends AbstractNamespaceParsingBeanDefinitionParser {
	protected Class<?> getBeanClass(Element element) {
		return XmlCompareRequestValidatorFactoryBean.class;
	}

	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		parseXPaths(element, bean);
		parseNamespaces(element, bean);
	}
}
