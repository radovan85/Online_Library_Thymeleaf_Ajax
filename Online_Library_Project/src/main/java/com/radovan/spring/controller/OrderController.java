package com.radovan.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.DeliveryAddressService;
import com.radovan.spring.service.LoyaltyCardService;
import com.radovan.spring.service.OrderService;

@Controller
@RequestMapping(value = "/orders")
public class OrderController {
	
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private DeliveryAddressService addressService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private LoyaltyCardService loyaltyCardService;
	
	@Autowired
	private OrderService orderService;

	@RequestMapping(value="/confirmOrder/{cartId}")
	public String confirmOrder(@PathVariable ("cartId") Integer cartId,ModelMap map) {
		OrderDto order = new OrderDto();
		cartService.validateCart(cartId);
		CustomerDto customer = customerService.getCustomerByCartId(cartId);
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(cartId);
		DeliveryAddressDto address = addressService.getAddressById(customer.getDeliveryAddressId());
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		List<BookDto> allBooks = bookService.listAll();
		Integer bookQuantity = cartItemService.getBookQuantity(cartId);
		Optional<Integer> loyaltyCardId = Optional.ofNullable(customer.getLoyaltyCardId());
		if(loyaltyCardId.isPresent()) {
			LoyaltyCardDto loyaltyCard = loyaltyCardService.getCardByCardId(loyaltyCardId.get());
			map.put("discount", loyaltyCard.getDiscount());
		}else {
			map.put("discount", 0);
		}
		map.put("order", order);
		map.put("customer", customer);
		map.put("allCartItems", allCartItems);
		map.put("address", address);
		map.put("cart", cart);
		map.put("allBooks", allBooks);
		map.put("bookQuantity", bookQuantity);
		return "fragments/orderConfirmation :: ajaxLoadedContent";	
	}
	
	
	@RequestMapping(value="/processOrder")
	public String processOrder(ModelMap map) {
		Integer pointsCollected = null;
		Integer pointsSpent = 0;
		Integer cardPoints = null;
		OrderDto order = orderService.addOrder();
		Double orderPrice = order.getOrderPrice();
		CustomerDto customer = customerService.getCustomer(order.getCustomerId());
		Optional<Integer> loyaltyCardId = Optional.ofNullable(customer.getLoyaltyCardId());
		if(loyaltyCardId.isPresent()) {
			LoyaltyCardDto loyaltyCard = loyaltyCardService.getCardByCardId(loyaltyCardId.get());
			cardPoints = loyaltyCard.getPoints();
			
			order.setDiscount(loyaltyCard.getDiscount());
			orderService.refreshOrder(order.getOrderId(),order);
			
			pointsCollected = (int) (orderPrice / 10);
			
			if(pointsCollected >= 100) {
				pointsCollected = 100;
			}
			
			if(cardPoints >= 100) {
				pointsSpent = 100;
				cardPoints = cardPoints - pointsSpent;
			}
			
			cardPoints = cardPoints + pointsCollected;
			loyaltyCard.setPoints(cardPoints);
			if(loyaltyCard.getPoints() >= 100) {
				loyaltyCard.setDiscount(35);
			}else {
				loyaltyCard.setDiscount(0);
			}
			
			loyaltyCardService.updateLoyaltyCard(loyaltyCardId.get(), loyaltyCard);
		}
		
		map.put("discount", order.getDiscount());
		map.put("pointsCollected", pointsCollected);
		map.put("pointsSpent", pointsSpent);
		map.put("cardPoints", cardPoints);
		
		return "fragments/orderCompleted :: ajaxLoadedContent";
	}
	
	
	
	
	
}
