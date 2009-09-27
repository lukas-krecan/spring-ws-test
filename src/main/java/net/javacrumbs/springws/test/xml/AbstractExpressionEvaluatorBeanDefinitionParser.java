package net.javacrumbs.springws.test.xml;

import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public abstract class AbstractExpressionEvaluatorBeanDefinitionParser extends
		AbstractNamespaceParsingBeanDefinitionParser {

	public AbstractExpressionEvaluatorBeanDefinitionParser() {
		super();
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
		parseNamespaces(element, bean);
		parseExceptionMapping(element, parserContext, bean);
	}

	private void parseExceptionMapping(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
		Element exceptionMapping = DomUtils.getChildElementByTagName(element, "exceptionMapping");
		if (exceptionMapping != null) {
			Map<?, ?> parsedMap = parserContext.getDelegate().parseMapElement(exceptionMapping, bean.getRawBeanDefinition());
			bean.addPropertyValue("exceptionMapping", parsedMap);
		}
	}

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

}