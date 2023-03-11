package com.radovan.spring.service;

import java.util.List;


import com.radovan.spring.dto.CartItemDto;

public interface CartItemService {

	CartItemDto addCartItem(CartItemDto cartItem);
	
	void removeCartItem(Integer cartId,Integer itemId);
	
	void eraseAllCartItems(Integer cartId);
	
	List<CartItemDto> listAllByCartId(Integer cartId);
	
	CartItemDto getCartItem(Integer id);
	
	CartItemDto getCartItemByCartIdAndBookId(Integer cartId,Integer bookId);
	
	Integer getBookQuantity(Integer cartId);
	
	void eraseAllByBookId(Integer bookId);
	
	Boolean hasDiscount(Integer itemId);
}

