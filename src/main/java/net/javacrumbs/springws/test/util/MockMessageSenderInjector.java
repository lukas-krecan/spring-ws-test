package net.javacrumbs.springws.test.util;

import java.util.Collection;

import net.javacrumbs.springws.test.MockWebServiceMessageSender;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
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
		if (templates.size()==0)
		{
			throw new BeanCreationException("No WebServiceTemplate found in the servlet context.");
		}
		WebServiceTemplate template = templates.iterator().next();
		
		Collection<MockWebServiceMessageSender> mockSenders = beanFactory.getBeansOfType(MockWebServiceMessageSender.class).values();
				
		template.setMessageSenders(mockSenders.toArray(new WebServiceMessageSender[mockSenders.size()]));		
	}
}
