package net.javacrumbs.springws.test.simple;

import java.net.URI;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

public interface LimitingRequestProcessor extends RequestProcessor{

	/**
	 * Request processor wrapper that checks number of {@link #processRequest(URI, WebServiceMessageFactory, WebServiceMessage)} calls.
     * If number of calls is higher then {@link #maxNumberOfProcessedRequests} null is returned.
     * If number of calls is lower then {@link #minNumberOfProcessedRequests} and verify is called, {@link WsTestException} is thrown.  
	 */
	public abstract void verify() throws WsTestException;

	public abstract int getMinNumberOfProcessedRequests();

	public abstract void setMinNumberOfProcessedRequests(int minNumberOfProcessedRequests);

	public abstract int getMaxNumberOfProcessedRequests();

	public abstract void setMaxNumberOfProcessedRequests(int maxNumberOfProcessedRequests);

}