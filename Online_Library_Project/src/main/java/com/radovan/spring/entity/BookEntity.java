package com.radovan.spring.entity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "books")
public class BookEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "book_id")
	private Integer bookId;

	private String ISBN;
	private String name;

	private String publisher;
	private String author;
	private String description;
	private String language;

	@Column(name = "published_year")
	private Integer publishedYear;

	@Column(name = "page_number")
	private Integer pageNumber;

	private Double price;

	@Column(name = "average_rating")
	private Double averageRating;

	@Column(name = "image_name")
	private String imageName;

	private String cover;

	private String letter;

	@ManyToOne
	@JoinColumn(name = "genre_id")
	private BookGenreEntity genre;

	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "book")
	private List<ReviewEntity> reviews;

	@Transient
	private LinkedHashMap<Integer, String> letters;

	@Transient
	private LinkedHashMap<Integer, String> covers;

	@Transient
	public String getMainImagePath() {
		if (bookId == null || imageName == null)
			return "/images/bookImages/unknown.jpg";
		return "/images/bookImages/" + this.imageName;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getPublishedYear() {
		return publishedYear;
	}

	public void setPublishedYear(Integer publishedYear) {
		this.publishedYear = publishedYear;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public BookGenreEntity getGenre() {
		return genre;
	}

	public void setGenre(BookGenreEntity genre) {
		this.genre = genre;
	}

	public LinkedHashMap<Integer, String> getLetters() {
		return letters;
	}

	public void setLetters(LinkedHashMap<Integer, String> letters) {
		this.letters = letters;
	}

	public LinkedHashMap<Integer, String> getCovers() {
		return covers;
	}

	public void setCovers(LinkedHashMap<Integer, String> covers) {
		this.covers = covers;
	}

	public List<ReviewEntity> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewEntity> reviews) {
		this.reviews = reviews;
	}

}
