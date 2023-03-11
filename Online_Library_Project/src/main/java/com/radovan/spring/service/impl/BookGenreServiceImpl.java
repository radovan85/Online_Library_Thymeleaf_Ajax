package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.BookGenreDto;
import com.radovan.spring.entity.BookEntity;
import com.radovan.spring.entity.BookGenreEntity;
import com.radovan.spring.exceptions.DeleteGenreException;
import com.radovan.spring.repository.BookGenreRepository;
import com.radovan.spring.repository.BookRepository;
import com.radovan.spring.service.BookGenreService;

@Service
@Transactional
public class BookGenreServiceImpl implements BookGenreService{
	
	@Autowired
	private BookGenreRepository genreRepository;
	
	@Autowired
	private TempConverter tempConverter;
	
	@Autowired
	private BookRepository bookRepository;

	@Override
	public BookGenreDto addGenre(BookGenreDto genre) {
		// TODO Auto-generated method stub
		BookGenreEntity genreEntity = tempConverter.bookGenreDtoToEntity(genre);
		BookGenreEntity storedGenre = genreRepository.save(genreEntity);
		BookGenreDto returnValue = tempConverter.bookGenreEntityToDto(storedGenre);
		return returnValue;
	}

	@Override
	public BookGenreDto getGenreById(Integer genreId) {
		// TODO Auto-generated method stub
		BookGenreDto returnValue = null;
		Optional<BookGenreEntity> genreEntity = Optional.ofNullable(genreRepository.getById(genreId));
		if(genreEntity.isPresent()) {
			returnValue = tempConverter.bookGenreEntityToDto(genreEntity.get());
		}
		return returnValue;
	}

	@Override
	public BookGenreDto getGenreByName(String name) {
		// TODO Auto-generated method stub
		BookGenreDto returnValue = null;
		Optional<BookGenreEntity> genreEntity = Optional.ofNullable(genreRepository.findByName(name));
		if(genreEntity.isPresent()) {
			returnValue = tempConverter.bookGenreEntityToDto(genreEntity.get());
		}
		return returnValue;
	}

	@Override
	public void deleteGenre(Integer genreId) {
		// TODO Auto-generated method stub
		
		List<BookEntity> books = bookRepository.findAllByGenreId(genreId);
		if(!books.isEmpty()) {
			Error error = new Error("Genre contains books");
			throw new DeleteGenreException(error);
		}
		
		genreRepository.deleteById(genreId);
		genreRepository.flush();
	}

	@Override
	public List<BookGenreDto> listAll() {
		// TODO Auto-generated method stub
		List<BookGenreDto> returnValue = new ArrayList<BookGenreDto>();
		Optional<List<BookGenreEntity>> allGenres = Optional.ofNullable(genreRepository.findAll());
		if(!allGenres.isEmpty()) {
			for(BookGenreEntity genre : allGenres.get()) {
				BookGenreDto genreDto = tempConverter.bookGenreEntityToDto(genre);
				returnValue.add(genreDto);
			}
		}
		return returnValue;
	}

}
