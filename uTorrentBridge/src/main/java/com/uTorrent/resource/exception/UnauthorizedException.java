package com.uTorrent.resource.exception;

/**
 * thrown if the user credentials supplied are invalid for the selected utorrent
 * webUI server
 * 
 * @author glenn
 * 
 */
public class UnauthorizedException extends Exception {

	private static final long serialVersionUID = 1L;
	private String _message;

	public UnauthorizedException(String message) {
		_message = message;
	}

	public String getMessage() {
		return _message;
	}
}
