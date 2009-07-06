package net.krecan.springws.test;

/**
 * Exception thrown test validation fails.
 * @author Lukas Krecan
 *
 */
public class WsTestException extends RuntimeException {
	private static final long serialVersionUID = 1746522483177610502L;

	public WsTestException() {
		super();
	}

	public WsTestException(String message, Throwable cause) {
		super(message, cause);
	}

	public WsTestException(String message) {
		super(message);
	}

	public WsTestException(Throwable cause) {
		super(cause);
	}

}
