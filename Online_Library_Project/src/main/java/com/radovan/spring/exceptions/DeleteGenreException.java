package com.radovan.spring.exceptions;

import javax.management.RuntimeErrorException;

public class DeleteGenreException extends RuntimeErrorException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeleteGenreException(Error e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

}
