package com.ansu.iam.client.exception;

public class IAMServerErrorException extends Exception {
	private static final long serialVersionUID = 1889829172097484257L;
	
	public IAMServerErrorException(String message) {
		super(message);
	}
}
