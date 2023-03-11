package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.List;

public class BookGenreDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer genreId;

	private String name;
	private String description;

	private List<Integer> booksIds;

	public Integer getGenreId() {
		return genreId;
	}

	public void setGenreId(Integer genreId) {
		this.genreId = genreId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Integer> getBooksIds() {
		return booksIds;
	}

	public void setBooksIds(List<Integer> booksIds) {
		this.booksIds = booksIds;
	}

}
