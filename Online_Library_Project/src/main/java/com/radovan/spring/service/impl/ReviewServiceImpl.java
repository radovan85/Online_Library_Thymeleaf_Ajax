package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.ReviewDto;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.ReviewEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.ReviewAlreadyExistsException;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.ReviewRepository;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.ReviewService;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService{
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private TempConverter tempConverter;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private BookService bookService;

	@Override
	public ReviewDto addReview(ReviewDto review) {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(customerRepository.findByUserId(authUser.getId()));
		if(customerEntity.isPresent()) {
			review.setAuthorId(customerEntity.get().getCustomerId());
		}
		
		Optional<ReviewEntity> existingReview = 
				Optional.ofNullable(reviewRepository.findByCustomerIdAndBookId(customerEntity.get().getCustomerId(), review.getBookId()));
		if(existingReview.isPresent()) {
			Error error = new Error("Existing Review");
			throw new ReviewAlreadyExistsException(error);
		}
		review.setAuthorized((byte) 0);
		ReviewEntity reviewEntity = tempConverter.reviewDtoToEntity(review);
		ZonedDateTime currentTime = LocalDateTime.now().atZone(ZoneId.of("Europe/Belgrade"));
		Timestamp createdAt = new Timestamp(currentTime.toInstant().getEpochSecond() * 1000L);
		reviewEntity.setCreatedAt(createdAt);
		ReviewEntity storedReview = reviewRepository.save(reviewEntity);
		ReviewDto returnValue = tempConverter.reviewEntityToDto(storedReview);
		return returnValue;
	}

	@Override
	public ReviewDto getReviewById(Integer reviewId) {
		// TODO Auto-generated method stub
		ReviewDto returnValue = null;
		Optional<ReviewEntity> reviewEntity = Optional.ofNullable(reviewRepository.getById(reviewId));
		if(reviewEntity.isPresent()) {
			returnValue = tempConverter.reviewEntityToDto(reviewEntity.get());
		}
		return returnValue;
	}

	@Override
	public List<ReviewDto> listAll() {
		// TODO Auto-generated method stub
		List<ReviewDto> returnValue = new ArrayList<ReviewDto>();
		Optional<List<ReviewEntity>> allReviews = Optional.ofNullable(reviewRepository.findAll());
		if(!allReviews.isEmpty()) {
			for(ReviewEntity review : allReviews.get()) {
				ReviewDto reviewDto = tempConverter.reviewEntityToDto(review);
				returnValue.add(reviewDto);
			}
		}
		return returnValue;
	}

	@Override
	public List<ReviewDto> listAllByBookId(Integer bookId) {
		// TODO Auto-generated method stub
		List<ReviewDto> returnValue = new ArrayList<ReviewDto>();
		Optional<List<ReviewEntity>> allReviews = Optional.ofNullable(reviewRepository.findAllAuthorizedByBookId(bookId));
		if(!allReviews.isEmpty()) {
			for(ReviewEntity review : allReviews.get()) {
				ReviewDto reviewDto = tempConverter.reviewEntityToDto(review);
				returnValue.add(reviewDto);
			}
		}
		return returnValue;
	}

	

	@Override
	public void deleteReview(Integer reviewId) {
		// TODO Auto-generated method stub
		reviewRepository.deleteById(reviewId);
		reviewRepository.flush();
	}

	@Override
	public List<ReviewDto> listAllAuthorized() {
		// TODO Auto-generated method stub
		List<ReviewDto> returnValue = new ArrayList<ReviewDto>();
		Optional<List<ReviewEntity>> allReviews = Optional.ofNullable(reviewRepository.findAllAuthorized());
		if(!allReviews.isEmpty()) {
			for(ReviewEntity review : allReviews.get()) {
				ReviewDto reviewDto = tempConverter.reviewEntityToDto(review);
				returnValue.add(reviewDto);
			}
		}
		return returnValue;
	}

	@Override
	public List<ReviewDto> listAllOnHold() {
		// TODO Auto-generated method stub
		List<ReviewDto> returnValue = new ArrayList<ReviewDto>();
		Optional<List<ReviewEntity>> allReviews = Optional.ofNullable(reviewRepository.findAllOnHold());
		if(!allReviews.isEmpty()) {
			for(ReviewEntity review : allReviews.get()) {
				ReviewDto reviewDto = tempConverter.reviewEntityToDto(review);
				returnValue.add(reviewDto);
			}
		}
		return returnValue;
	}

	@Override
	public void authorizeReview(Integer reviewId) {
		// TODO Auto-generated method stub
		Optional<ReviewEntity> reviewEntity = Optional.ofNullable(reviewRepository.getById(reviewId));
		if(reviewEntity.isPresent()) {
			ReviewEntity reviewValue = reviewEntity.get();
			reviewValue.setAuthorized((byte) 1);
			reviewRepository.saveAndFlush(reviewValue);			
			bookService.refreshAvgRating();			
		}		
	}
	
	@Override
	public void deleteAllByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		reviewRepository.deleteAllByCustomerId(customerId);
		reviewRepository.flush();
		bookService.refreshAvgRating();
	}

}
