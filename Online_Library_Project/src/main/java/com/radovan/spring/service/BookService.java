package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.BookDto;

public interface BookService {

	BookDto addBook(BookDto book);
	
	BookDto getBookById(Integer bookId);
	
	BookDto getBookByISBN(String isbn);
	
	void deleteBook(Integer bookId);
	
	List<BookDto> listAll();
	
	List<BookDto> listAllByGenreId(Integer genreId);
	
	List<BookDto> search(String keyword);
	
	void addToWishList(Integer bookId);
	
	List<BookDto> listAllFromWishList();
	
	void removeFromWishList(Integer bookId);
	
	List<BookDto> listAllByBookId();
	
	List<BookDto> listAllByRating();
	
	List<BookDto> listAllByPrice();
	
	void removeBookFromAllWishlist(Integer bookId);
	
	void refreshAvgRating();
}
