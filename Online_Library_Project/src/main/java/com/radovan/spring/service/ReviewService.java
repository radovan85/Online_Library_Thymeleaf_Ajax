package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.ReviewDto;

public interface ReviewService {

	ReviewDto addReview(ReviewDto review);
	
	ReviewDto getReviewById(Integer reviewId);
	
	List<ReviewDto> listAll();
	
	List<ReviewDto> listAllByBookId(Integer bookId);
	
	List<ReviewDto> listAllAuthorized();
	
	List<ReviewDto> listAllOnHold();
	
	void deleteReview(Integer reviewId);
	
	void authorizeReview(Integer reviewId);

	void deleteAllByCustomerId(Integer customerId);
}
