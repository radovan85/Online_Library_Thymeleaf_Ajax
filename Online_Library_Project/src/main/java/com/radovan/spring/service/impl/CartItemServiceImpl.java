package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.LoyaltyCardEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartRepository cartRepository;

	@Override
	public CartItemDto addCartItem(CartItemDto cartItem) {
		// TODO Auto-generated method stub
		CartItemEntity cartItemEntity = tempConverter.cartItemDtoToEntity(cartItem);
		CartItemEntity storedItem = cartItemRepository.save(cartItemEntity);
		CartItemDto returnValue = tempConverter.cartItemEntityToDto(storedItem);
		return returnValue;
	}

	@Override
	public void removeCartItem(Integer cartId, Integer itemId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeCartItem(cartId, itemId);
		cartItemRepository.flush();
		cartService.refreshCartState(cartId);

	}

	@Override
	public void eraseAllCartItems(Integer cartId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByCartId(cartId);
		cartItemRepository.flush();
		cartService.refreshCartState(cartId);
	}

	@Override
	public List<CartItemDto> listAllByCartId(Integer cartId) {
		// TODO Auto-generated method stub
		Optional<List<CartItemEntity>> cartItems = Optional.ofNullable(cartItemRepository.findAllByCartId(cartId));
		List<CartItemDto> returnValue = new ArrayList<>();

		if (!cartItems.isEmpty()) {
			for (CartItemEntity item : cartItems.get()) {
				CartItemDto itemDto = tempConverter.cartItemEntityToDto(item);
				returnValue.add(itemDto);
			}
		}

		return returnValue;
	}

	@Override
	public CartItemDto getCartItem(Integer id) {
		// TODO Auto-generated method stub
		Optional<CartItemEntity> cartItemEntity = Optional.ofNullable(cartItemRepository.getById(id));
		CartItemDto returnValue = null;
		if (cartItemEntity.isPresent()) {
			returnValue = tempConverter.cartItemEntityToDto(cartItemEntity.get());
		}

		return returnValue;
	}

	@Override
	public CartItemDto getCartItemByCartIdAndBookId(Integer cartId, Integer bookId) {
		// TODO Auto-generated method stub
		Optional<CartItemEntity> cartItemEntity = Optional
				.ofNullable(cartItemRepository.findByCartIdAndBookId(cartId, bookId));
		CartItemDto returnValue = null;
		if (cartItemEntity.isPresent()) {
			returnValue = tempConverter.cartItemEntityToDto(cartItemEntity.get());
		}

		return returnValue;
	}

	@Override
	public Integer getBookQuantity(Integer cartId) {
		// TODO Auto-generated method stub
		Integer returnValue = 0;
		Optional<Integer> bookQuantity = Optional.ofNullable(cartItemRepository.findBookQuantity(cartId));
		if(bookQuantity.isPresent()) {
			returnValue = bookQuantity.get();
		}
		return returnValue;
	}

	@Override
	public void eraseAllByBookId(Integer bookId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByBookId(bookId);
		cartItemRepository.flush();
		
		Optional<List<CartEntity>> allCarts = Optional.ofNullable(cartRepository.findAll());
		if(!allCarts.isEmpty()) {
			for(CartEntity cartEntity:allCarts.get()) {
				cartService.refreshCartState(cartEntity.getCartId());
			}
		}
	}

	@Override
	public Boolean hasDiscount(Integer itemId) {
		// TODO Auto-generated method stub
		Boolean returnValue = false;
		Optional<CartItemEntity> itemEntity = Optional.ofNullable(cartItemRepository.getById(itemId));
		if(itemEntity.isPresent()) {
			CartEntity cartEntity = itemEntity.get().getCart();
			CustomerEntity customerEntity = cartEntity.getCustomer();
			Optional<LoyaltyCardEntity> loyaltyCard = Optional.ofNullable(customerEntity.getLoyaltyCard());
			if(loyaltyCard.isPresent()) {
				Integer discount = loyaltyCard.get().getDiscount();
				if(discount > 0) {
					returnValue = true;
				}
			}
		}
		
		return returnValue;
	}

}
