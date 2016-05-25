package za.co.chris.wug.exception;

public class UnknownServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnknownServiceException() {

	}

	public UnknownServiceException(String message) {
		super(message);
	}

	public UnknownServiceException(Throwable e) {
		super(e);
	}

	public UnknownServiceException(String message, Throwable e) {
		super(message, e);
	}
}
