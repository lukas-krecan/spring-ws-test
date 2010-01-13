package net.javacrumbs.springws.test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.WebServiceMessageSender;

public abstract class AbstractMockWebServiceMessageSender implements WebServiceMessageSender{

	private List<EndpointInterceptor> interceptors = Collections.emptyList();

	public AbstractMockWebServiceMessageSender() {
		super();
	}

	public WebServiceConnection createConnection(URI uri) throws IOException {
		MockWebServiceConnection connection = new MockWebServiceConnection(uri);
		connection.setRequestProcessors(getRequestProcessors());
		connection.setInterceptors(interceptors);
		return connection;
	}

	protected abstract List<RequestProcessor> getRequestProcessors();

	public boolean supports(URI uri) {
		return true;
	}

	public List<EndpointInterceptor> getInterceptors() {
		return Collections.unmodifiableList(interceptors);
	}

	public void setInterceptors(List<? extends EndpointInterceptor> interceptors) {
		this.interceptors = new ArrayList<EndpointInterceptor>(interceptors);
	}

}