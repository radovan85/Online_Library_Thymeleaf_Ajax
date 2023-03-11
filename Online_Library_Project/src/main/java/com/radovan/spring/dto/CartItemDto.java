package com.radovan.spring.dto;

import java.io.Serializable;


public class CartItemDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer cartItemId;

	private Integer quantity;

	private Double price;

	private Integer bookId;

	private Integer cartId;

	public Integer getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Integer cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	@Override
	public String toString() {
		return "CartItemDto [cartItemId=" + cartItemId + ", quantity=" + quantity + ", price=" + price + ", bookId="
				+ bookId + ", cartId=" + cartId + "]";
	}
	
	
	
}
