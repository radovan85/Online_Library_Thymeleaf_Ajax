package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.List;



public class WishListDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer wishListId;
	
	private List<Integer> booksIds;
	
	private Integer customerId;

	public Integer getWishListId() {
		return wishListId;
	}

	public void setWishListId(Integer wishListId) {
		this.wishListId = wishListId;
	}

	

	

	public List<Integer> getBooksIds() {
		return booksIds;
	}

	public void setBooksIds(List<Integer> booksIds) {
		this.booksIds = booksIds;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	

}
