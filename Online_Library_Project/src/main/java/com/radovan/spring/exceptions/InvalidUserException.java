package com.radovan.spring.exceptions;

import javax.management.RuntimeErrorException;

public class InvalidUserException extends RuntimeErrorException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidUserException(Error e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

}
