package net.javacrumbs.springws.test.simple.annotation;

import net.javacrumbs.springws.test.util.MockMessageSenderInjector;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Listener alters current application context. It registers {@link ThreadLocalWsMockControlFactoryBean} and replaces all
 * {@link WebServiceMessageSender}s in all {@link WebServiceTemplate}s in the application context.
 * @author Lukas Krecan
 *
 */
public class WsMockControlTestExecutionListener extends AbstractTestExecutionListener {

	private static final String MESSAGE_SENDER_BEAN_NAME = "net.javacrumbs.springws.test.simple.annotation.WsMockControlMockWebServiceMessageSender";
	private static final String MOCK_CONTROL_BEAN_NAME = "net.javacrumbs.springws.test.simple.annotation.WsMockControl";

	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		ConfigurableListableBeanFactory beanFactory = ((AbstractApplicationContext)testContext.getApplicationContext()).getBeanFactory();
		
		if (!beanFactory.containsBean(MOCK_CONTROL_BEAN_NAME))
		{
			beanFactory.registerSingleton(MOCK_CONTROL_BEAN_NAME, new ThreadLocalWsMockControlFactoryBean());
			WsMockControlMockWebServiceMessageSender messageSender = new WsMockControlMockWebServiceMessageSender();
			beanFactory.registerSingleton(MESSAGE_SENDER_BEAN_NAME, messageSender);
			new MockMessageSenderInjector().postProcessBeanFactory(beanFactory);
		}

	}
	
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		ThreadLocalWsMockControlFactoryBean.clean();
	}
}
