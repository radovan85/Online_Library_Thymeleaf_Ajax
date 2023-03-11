package com.radovan.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.radovan.spring.exceptions.BookQuantityException;
import com.radovan.spring.exceptions.DeleteGenreException;
import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.exceptions.ImagePathException;
import com.radovan.spring.exceptions.InvalidCartException;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.exceptions.ReviewAlreadyExistsException;
import com.radovan.spring.exceptions.SuspendedUserException;

@ControllerAdvice
public class ExceptionController {

	@ResponseStatus
	@ExceptionHandler(ExistingEmailException.class)
	public ResponseEntity<?> handleExistingEmailException(ExistingEmailException ex) {
		return ResponseEntity.internalServerError().body("Email exists already!");
	}

	@ResponseStatus
	@ExceptionHandler(InvalidUserException.class)
	public ResponseEntity<?> handleInvalidUserException(InvalidUserException ex) {
		return ResponseEntity.internalServerError().body("Invalid user!");
	}

	@ResponseStatus
	@ExceptionHandler(InvalidCartException.class)
	public ResponseEntity<?> handleInvalidCartException(InvalidCartException ex) {
		return ResponseEntity.internalServerError().body("Invalid cart");
	}
	

	@ResponseStatus
	@ExceptionHandler(SuspendedUserException.class)
	public ResponseEntity<?> handleSuspendedUserException(SuspendedUserException ex) {
		SecurityContextHolder.clearContext();
		return ResponseEntity.internalServerError().body("Account Suspended!");
	}
	
	@ResponseStatus
	@ExceptionHandler(ImagePathException.class)
	public ResponseEntity<?> handleImagePathException(ImagePathException ex){
		System.out.println("Error image fires up!");
		return ResponseEntity.internalServerError().body("Invalid image path");
	}
	
	@ResponseStatus
	@ExceptionHandler(ReviewAlreadyExistsException.class)
	public ResponseEntity<?> handleReviewAlreadyExistsException(ReviewAlreadyExistsException ex){
		return ResponseEntity.internalServerError().body("Review exists already");
	}
	
	@ResponseStatus
	@ExceptionHandler(DeleteGenreException.class)
	public ResponseEntity<?> handleDeleteGenreException(DeleteGenreException ex) {
		return ResponseEntity.internalServerError().body("Selected genre contains books!");
	}
	
	@ResponseStatus
	@ExceptionHandler(BookQuantityException.class)
	public ResponseEntity<?> handleBookQuantityException(BookQuantityException ex){
		return ResponseEntity.internalServerError().body("Maximum 50 books allowed");
	}
}
