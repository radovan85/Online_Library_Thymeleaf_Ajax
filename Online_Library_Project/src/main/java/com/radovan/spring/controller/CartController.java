package com.radovan.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.exceptions.BookQuantityException;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.LoyaltyCardService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private BookService bookService;

	@Autowired
	private LoyaltyCardService loyaltyCardService;

	@RequestMapping(value = "/addToCart", method = RequestMethod.POST)
	public String addCartItem(@ModelAttribute("cartItem") CartItemDto cartItem) {
		Integer bookId = cartItem.getBookId();
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		Optional<Integer> loyaltyCardId = Optional.ofNullable(customer.getLoyaltyCardId());
		BookDto book = bookService.getBookById(bookId);
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(cart.getCartId());
		Integer tempQuantity = 0;
		for(CartItemDto tempCartItem : allCartItems) {
			tempQuantity = tempQuantity + tempCartItem.getQuantity();
		}
		
		if(tempQuantity + cartItem.getQuantity() > 50) {
			Error error = new Error("Maximum 50 books allowed");
			throw new BookQuantityException(error);
		}
		
		
		if (loyaltyCardId.isPresent()) {
			LoyaltyCardDto loyaltyCard = loyaltyCardService.getCardByCardId(loyaltyCardId.get());
			Integer discount = loyaltyCard.getDiscount();

			Optional<CartItemDto> existingCartItem = Optional
					.ofNullable(cartItemService.getCartItemByCartIdAndBookId(cart.getCartId(), bookId));
			if (existingCartItem.isPresent()) {
				cartItem.setCartItemId(existingCartItem.get().getCartItemId());
				cartItem.setCartId(cart.getCartId());
				cartItem.setQuantity(existingCartItem.get().getQuantity() + cartItem.getQuantity());
				if (cartItem.getQuantity() > 50) {
					cartItem.setQuantity(50);
				}
				if (discount == 0) {
					cartItem.setPrice(book.getPrice() * cartItem.getQuantity());
				} else {
					Double cartPrice = (book.getPrice() * cartItem.getQuantity());
					cartPrice = cartPrice - ((cartPrice * discount) / 100);
					cartItem.setPrice(cartPrice);
				}
				cartItemService.addCartItem(cartItem);
				cartService.refreshCartState(cart.getCartId());
			} else {
				cartItem.setQuantity(cartItem.getQuantity());
				if(cartItem.getQuantity() > 50) {
					cartItem.setQuantity(50);
				}
				if (discount == 0) {
					cartItem.setPrice(book.getPrice() * cartItem.getQuantity());
				} else {
					Double cartPrice = (book.getPrice() * cartItem.getQuantity());
					cartPrice = cartPrice - ((cartPrice * discount) / 100);
					cartItem.setPrice(cartPrice);
				}
				cartItem.setCartId(cart.getCartId());
				cartItemService.addCartItem(cartItem);
				cartService.refreshCartState(cart.getCartId());
			}

		} else {

			Optional<CartItemDto> existingCartItem = Optional
					.ofNullable(cartItemService.getCartItemByCartIdAndBookId(cart.getCartId(), bookId));
			if (existingCartItem.isPresent()) {
				cartItem.setCartItemId(existingCartItem.get().getCartItemId());
				cartItem.setCartId(cart.getCartId());
				cartItem.setQuantity(existingCartItem.get().getQuantity() + cartItem.getQuantity());
				if(cartItem.getQuantity() > 50) {
					cartItem.setQuantity(50);
				}
				cartItem.setPrice(book.getPrice() * cartItem.getQuantity());
				cartItemService.addCartItem(cartItem);
				cartService.refreshCartState(cart.getCartId());
			} else {
				cartItem.setQuantity(cartItem.getQuantity());
				if(cartItem.getQuantity() > 50) {
					cartItem.setQuantity(50);
				}
				cartItem.setCartId(cart.getCartId());
				cartItem.setPrice(book.getPrice() * cartItem.getQuantity());
				cartItemService.addCartItem(cartItem);
				cartService.refreshCartState(cart.getCartId());
			}
		}

		return "fragments/homePage :: ajaxLoadedContent";

	}

	@RequestMapping(value = "/addItemCompleted")
	public String addItemCompleted() {
		return "fragments/itemAdded :: ajaxLoadedContent";
	}
	
	@RequestMapping(value = "/getCart")
	public String cartDetails(ModelMap map) {
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(customer.getCartId()); 
		List<BookDto> allBooks = bookService.listAll();
		Optional<Integer> loyaltyCardId = Optional.ofNullable(customer.getLoyaltyCardId());
		if(loyaltyCardId.isPresent()) {
			LoyaltyCardDto loyaltyCard = loyaltyCardService.getCardByCardId(loyaltyCardId.get());
			map.put("discount", loyaltyCard.getDiscount());
		}else {
			map.put("discount", 0);
		}
		map.put("allCartItems", allCartItems);
		map.put("allBooks", allBooks);
		map.put("cart", cart);
		return "fragments/cart :: ajaxLoadedContent";
		
	}
	
	
	@RequestMapping(value="/deleteItem/{cartId}/{itemId}")
	public String deleteCartItem(@PathVariable ("cartId") Integer cartId,@PathVariable ("itemId") Integer itemId) {
		cartItemService.removeCartItem(cartId, itemId);
		return "fragments/homePage :: ajaxLoadedContent";
	}
	
	@RequestMapping(value="/deleteAllItems/{cartId}")
	public String deleteAllCartItems(@PathVariable ("cartId") Integer cartId) {
		cartItemService.eraseAllCartItems(cartId);
		return "fragments/homePage :: ajaxLoadedContent";
	}
	
	@RequestMapping(value="/invalidCart")
	public String invalidCart() {
		return "fragments/invalidCart :: ajaxLoadedContent";
	}
	

}
