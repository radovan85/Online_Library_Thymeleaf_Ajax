package com.radovan.spring.service;


import com.radovan.spring.dto.CartDto;



public interface CartService {

	CartDto getCartByCartId(Integer cartId);

	void refreshCartState(Integer cartId);
	
	Double calculateGrandTotal(Integer cartId);
	
	CartDto validateCart(Integer cartId);

	Double calculateFullPrice(Integer cartId);
	
	void deleteCart(Integer cartId);
}
