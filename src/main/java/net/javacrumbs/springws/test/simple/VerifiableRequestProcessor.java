package net.javacrumbs.springws.test.simple;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;

public interface VerifiableRequestProcessor extends RequestProcessor{

	/**
	 * Checks if numberOfProcessedRequests is between {@link #minNumberOfProcessedRequests} and {@link #maxNumberOfProcessedRequests} inclusive.
	 * If not, {@link WsTestException} is thrown.
	 */
	public abstract void verify() throws WsTestException;

	public abstract int getMinNumberOfProcessedRequests();

	public abstract void setMinNumberOfProcessedRequests(int minNumberOfProcessedRequests);

	public abstract int getMaxNumberOfProcessedRequests();

	public abstract void setMaxNumberOfProcessedRequests(int maxNumberOfProcessedRequests);

}