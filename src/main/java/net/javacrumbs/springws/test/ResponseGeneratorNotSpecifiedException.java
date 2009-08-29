package net.javacrumbs.springws.test;

/**
 * Exception throw if no response generator is specified or if all of them return <code>null</code>.
 * @author Lukas Krecan
 *
 */
public class ResponseGeneratorNotSpecifiedException extends IllegalStateException {

	private static final long serialVersionUID = -1760861006446428034L;

	public ResponseGeneratorNotSpecifiedException(String message) {
		super(message);
	}

}
