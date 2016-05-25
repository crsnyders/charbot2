package za.co.chris.wug.exception;

public class UnknownProcessorException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnknownProcessorException() {

	}

	public UnknownProcessorException(String message) {
		super(message);
	}

	public UnknownProcessorException(Throwable e) {
		super(e);
	}

	public UnknownProcessorException(String message, Throwable e) {
		super(message, e);
	}
}
