package com.uTorrent.resource.exception;

/**
 * thrown when the utorrent webUI server cannot be located
 * 
 * @author glenn
 * 
 */
public class ServerNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private String _message;

	public ServerNotFoundException(String message) {
		_message = message;
	}

	public String getMessage() {
		return _message;
	}
}
