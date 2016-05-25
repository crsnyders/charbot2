package za.co.chris.wug.exception;

public class ProviderException extends Exception {

	private static final long serialVersionUID = 1L;

	public ProviderException(String message) {
		super(message);
	}
	public ProviderException(Exception exception) {
		super(exception);
	}

	public ProviderException(String message,Exception exception) {
		super(message,exception);
	}

}
