package net.javacrumbs.springws.test.simple.annotation;

import java.util.ArrayList;
import java.util.List;

import net.javacrumbs.springws.test.AbstractMockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;

public class WsMockControlMockWebServiceMessageSender extends AbstractMockWebServiceMessageSender {

	@Override
	protected List<RequestProcessor> getRequestProcessors() {
		return new ArrayList<RequestProcessor>(ThreadLocalWsMockControlFactoryBean.getWsMockControl().getRequestProcessors());
	}
}
