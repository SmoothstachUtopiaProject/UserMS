package com.ss.utopia.exception;

public class ExpiredTokenExpception extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ExpiredTokenExpception() {}

	public ExpiredTokenExpception(String message) {
		super(message);
	}
}
