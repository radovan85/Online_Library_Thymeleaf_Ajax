package com.radovan.spring.exceptions;

import javax.management.RuntimeErrorException;

public class ReviewAlreadyExistsException extends RuntimeErrorException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReviewAlreadyExistsException(Error e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

}
