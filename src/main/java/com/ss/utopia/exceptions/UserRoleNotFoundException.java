package com.ss.utopia.exceptions;

public class UserRoleNotFoundException extends Exception {
  private static final long serialVersionUID = 1L;

	public UserRoleNotFoundException() {}
	public UserRoleNotFoundException(String message) {
		super(message);
	}
}
