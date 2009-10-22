package net.javacrumbs.springws.test.simple;

import java.io.IOException;
import java.net.URI;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.WsTestException;

/**
 * Request processor wrapper that verifies number of {@link #processRequest(URI, WebServiceMessageFactory, WebServiceMessage)} calls. 
 * @author Lukas Krecan
 *
 */
public class UsageValidator implements RequestProcessor {

	private int numberOfProcessedRequests = 0;
	
	private int minNumberOfProcessedRequests = 1;
	
	private int maxNumberOfProcessedRequests = 1;


	public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory, WebServiceMessage request)
			throws IOException {
		numberOfProcessedRequests++;
		if (numberOfProcessedRequests>maxNumberOfProcessedRequests)
		{
			throw new WsTestException(getExceptionMessage());
		}
		return null;
	}

	public int getNumberOfProcessedRequests() {
		return numberOfProcessedRequests;
	}

	/**
	 * Verifies number of {@link #processRequest(URI, WebServiceMessageFactory, WebServiceMessage)} calls. If it's not between {@link #minNumberOfProcessedRequests} 
	 * and {@link #maxNumberOfProcessedRequests} (inclusive) throws {@link WsTestException}.
	 * @throws WsTestException
	 */
	public void verify() throws WsTestException{
		if (numberOfProcessedRequests>maxNumberOfProcessedRequests || numberOfProcessedRequests<minNumberOfProcessedRequests)
		{
			throw new WsTestException(getExceptionMessage());
		}
	}

	private String getExceptionMessage() {
		return "Unexpected number of WebServiceTemplate calls, expected from "+minNumberOfProcessedRequests+" to "+maxNumberOfProcessedRequests+" calls, was "+numberOfProcessedRequests+".";
	}

	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.simple.VerifiableRequestProcessor#getMinNumberOfProcessedRequests()
	 */
	public int getMinNumberOfProcessedRequests() {
		return minNumberOfProcessedRequests;
	}



	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.simple.VerifiableRequestProcessor#setMinNumberOfProcessedRequests(int)
	 */
	public void setMinNumberOfProcessedRequests(int minNumberOfProcessedRequests) {
		this.minNumberOfProcessedRequests = minNumberOfProcessedRequests;
	}



	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.simple.VerifiableRequestProcessor#getMaxNumberOfProcessedRequests()
	 */
	public int getMaxNumberOfProcessedRequests() {
		return maxNumberOfProcessedRequests;
	}



	/* (non-Javadoc)
	 * @see net.javacrumbs.springws.test.simple.VerifiableRequestProcessor#setMaxNumberOfProcessedRequests(int)
	 */
	public void setMaxNumberOfProcessedRequests(int maxNumberOfProcessedRequests) {
		this.maxNumberOfProcessedRequests = maxNumberOfProcessedRequests;
	}



	public void setNumberOfProcessedRequests(int numberOfProcessedRequests) {
		this.numberOfProcessedRequests = numberOfProcessedRequests;
	}
}
