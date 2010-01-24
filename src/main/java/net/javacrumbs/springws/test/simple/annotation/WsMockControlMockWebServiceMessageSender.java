package net.javacrumbs.springws.test.simple.annotation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ws.transport.WebServiceConnection;

import net.javacrumbs.springws.test.AbstractMockWebServiceMessageSender;
import net.javacrumbs.springws.test.RequestProcessor;

/**
 * Message sender that creates {@link WebServiceConnection} from {@link ThreadLocalWsMockControlFactoryBean}s request processors.
 * @author Lukas Krecan
 *
 */
class WsMockControlMockWebServiceMessageSender extends AbstractMockWebServiceMessageSender {

	@Override
	protected List<RequestProcessor> getRequestProcessors() {
		return new ArrayList<RequestProcessor>(ThreadLocalWsMockControlFactoryBean.getWsMockControl().getRequestProcessors());
	}
}
