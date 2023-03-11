package com.radovan.spring.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.ReviewDto;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.ReviewService;

@Controller
@RequestMapping(value = "/reviews")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/createReview/{bookId}")
	public String renderReviewForm(@PathVariable ("bookId") Integer bookId,ModelMap map) {
		ReviewDto review = new ReviewDto();
		BookDto book = bookService.getBookById(bookId);
		List<Integer> ratings = new ArrayList<Integer>();
		for(int x=1;x<=5;x++) {
			ratings.add(x);
		}
		map.put("review", review);
		map.put("book", book);
		map.put("ratings", ratings);
		return "fragments/reviewForm :: ajaxLoadedContent";
	}
	
	
	@RequestMapping(value = "/createReview",method=RequestMethod.POST)
	public String createReview(@ModelAttribute ("review") ReviewDto review) {
		reviewService.addReview(review);
		return "fragments/homePage :: ajaxLoadedContent";
	}
	
	@RequestMapping(value="/reviewSentCompleted")
	public String reviewCompleted() {
		return "fragments/reviewSent :: ajaxLoadedContent";
	}
}
