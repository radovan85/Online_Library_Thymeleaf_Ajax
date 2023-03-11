package com.radovan.spring.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "reviews")
public class ReviewEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "review_id")
	private Integer reviewId;

	private String text;

	private Integer rating;

	private Timestamp createdAt;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private CustomerEntity author;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private BookEntity book;

	private byte authorized;

	public Integer getReviewId() {
		return reviewId;
	}

	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public CustomerEntity getAuthor() {
		return author;
	}

	public void setAuthor(CustomerEntity author) {
		this.author = author;
	}

	public BookEntity getBook() {
		return book;
	}

	public void setBook(BookEntity book) {
		this.book = book;
	}

	public byte getAuthorized() {
		return authorized;
	}

	public void setAuthorized(byte authorized) {
		this.authorized = authorized;
	}

}
