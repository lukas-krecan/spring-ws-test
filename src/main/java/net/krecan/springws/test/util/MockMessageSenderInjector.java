package net.krecan.springws.test.util;

import java.util.Collection;

import net.krecan.springws.test.MockWebServiceMessageSender;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Injects mock message senders into {@link WebServiceTemplate}.
 * @author Lukas Krecan
 *
 */
public class MockMessageSenderInjector implements BeanFactoryPostProcessor {

	@SuppressWarnings("unchecked")
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Collection<WebServiceTemplate> templates = beanFactory.getBeansOfType(WebServiceTemplate.class).values();
		WebServiceTemplate template = templates.iterator().next();
		
		Collection<MockWebServiceMessageSender> mockSenders = beanFactory.getBeansOfType(MockWebServiceMessageSender.class).values();
				
		template.setMessageSenders(mockSenders.toArray(new WebServiceMessageSender[mockSenders.size()]));		
	}
}
