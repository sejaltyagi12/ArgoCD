package com.ems.exception;

public class ServiceRuntimeException extends RuntimeException{

	  /**
	 * 
	 */
	private static final long serialVersionUID = 5627010380340080925L;
	private String message;

	public ServiceRuntimeException(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
