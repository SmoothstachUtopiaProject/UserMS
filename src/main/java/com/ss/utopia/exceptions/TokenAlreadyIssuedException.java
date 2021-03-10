package com.ss.utopia.exceptions;

public class TokenAlreadyIssuedException  extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TokenAlreadyIssuedException() {};
	
	public TokenAlreadyIssuedException(String message) {
		super(message);
	}

}
