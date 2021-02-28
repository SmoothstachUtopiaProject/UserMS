package com.ss.utopia.exceptions;

public class UserRoleAlreadyExistsException extends Exception {
  private static final long serialVersionUID = 1L;

	public UserRoleAlreadyExistsException() {}
	public UserRoleAlreadyExistsException(String message) {
		super(message);
	}
}
