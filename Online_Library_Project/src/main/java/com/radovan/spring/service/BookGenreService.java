package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.BookGenreDto;

public interface BookGenreService {

	BookGenreDto addGenre(BookGenreDto genre);
	
	BookGenreDto getGenreById(Integer genreId);
	
	BookGenreDto getGenreByName(String name);
	
	void deleteGenre(Integer genreId);
	
	List<BookGenreDto> listAll();
}
