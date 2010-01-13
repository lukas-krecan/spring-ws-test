package net.javacrumbs.springws.test.simple;

import net.javacrumbs.springws.test.util.MockMessageSenderInjector;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class WsMockControlTestExecutionListener extends AbstractTestExecutionListener {

	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		ConfigurableListableBeanFactory beanFactory = ((AbstractApplicationContext)testContext.getApplicationContext()).getBeanFactory();
		
		
		WsMockControl mockControl = new WsMockControl();
		//TODO generate name, check existence
		beanFactory.registerSingleton("WsMockControl", mockControl);
		
		WsMockControlMockWebServiceMessageSender messageSender = new WsMockControlMockWebServiceMessageSender(mockControl);
		//TODO generate name, check existence
		beanFactory.registerSingleton("WsMockControlMockWebServiceMessageSender", messageSender);
		
		
		new MockMessageSenderInjector().postProcessBeanFactory(beanFactory);

	}
}
