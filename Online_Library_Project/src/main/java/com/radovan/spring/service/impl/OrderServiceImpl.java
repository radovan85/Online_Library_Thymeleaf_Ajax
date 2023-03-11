package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.DeliveryAddressEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderAddressRepository orderAddressRepository;

	@Override
	public OrderDto addOrder() {
		// TODO Auto-generated method stub
		OrderDto returnValue = null;
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<CustomerEntity> customerOptional = Optional
				.ofNullable(customerRepository.findByUserId(authUser.getId()));
		CustomerEntity customerEntity = null;
		OrderEntity orderEntity = new OrderEntity();
		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();
		if (customerOptional.isPresent()) {
			customerEntity = customerOptional.get();
			Optional<CartEntity> cartEntityOptional = Optional.ofNullable(customerEntity.getCart());
			if (cartEntityOptional.isPresent()) {
				CartEntity cartEntity = cartEntityOptional.get();
				Optional<List<CartItemEntity>> allCartItems = Optional.ofNullable(cartEntity.getCartItems());
				if (!allCartItems.isEmpty()) {
					for (CartItemEntity cartItem : allCartItems.get()) {
						OrderItemEntity orderItem = tempConverter.cartItemToOrderItemEntity(cartItem);
						orderedItems.add(orderItem);
					}

					cartItemRepository.removeAllByCartId(cartEntity.getCartId());
					cartService.refreshCartState(cartEntity.getCartId());
					
					DeliveryAddressEntity deliveryAddress = customerEntity.getDeliveryAddress();
					OrderAddressEntity orderAddress = tempConverter.addressToOrderAddress(deliveryAddress);
					OrderAddressEntity storedOrderAddress = orderAddressRepository.save(orderAddress);

					orderEntity.setCustomer(customerEntity);
					orderEntity.setAddress(storedOrderAddress);
					Optional<Integer> discount = Optional.ofNullable(orderEntity.getDiscount());
					if(!discount.isPresent()) {
						orderEntity.setDiscount(0);
					}
					
					ZonedDateTime currentTime = LocalDateTime.now().atZone(ZoneId.of("Europe/Belgrade"));
					Timestamp createdAt = new Timestamp(currentTime.toInstant().getEpochSecond() * 1000L);
					orderEntity.setCreatedAt(createdAt);
					OrderEntity storedOrder = orderRepository.save(orderEntity);
					

					for (OrderItemEntity orderItem : orderedItems) {
						orderItem.setOrder(storedOrder);
						orderItemRepository.save(orderItem);
					}

					orderedItems = orderItemRepository.findAllByOrderId(storedOrder.getOrderId());
					Optional<Double> orderPrice = Optional
							.ofNullable(orderItemRepository.calculateGrandTotal(storedOrder.getOrderId()));
					if (orderPrice.isPresent()) {
						storedOrder.setOrderPrice(orderPrice.get());
					}
					
					storedOrderAddress.setOrder(storedOrder);
					orderAddressRepository.saveAndFlush(storedOrderAddress);

					Integer bookQuantity = getBookQuantity(storedOrder.getOrderId());
					storedOrder.setBookQuantity(bookQuantity);
					storedOrder.setOrderedItems(orderedItems);
					storedOrder = orderRepository.saveAndFlush(storedOrder);
					returnValue = tempConverter.orderEntityToDto(storedOrder);

				}
			}
		}

		return returnValue;
	}

	@Override
	public List<OrderDto> listAll() {
		// TODO Auto-generated method stub
		List<OrderDto> returnValue = new ArrayList<OrderDto>();
		Optional<List<OrderEntity>> allOrders = Optional.ofNullable(orderRepository.findAll());
		if (!allOrders.isEmpty()) {
			for (OrderEntity order : allOrders.get()) {
				OrderDto orderDto = tempConverter.orderEntityToDto(order);
				returnValue.add(orderDto);
			}
		}
		return returnValue;
	}

	@Override
	public Double calculateOrderTotal(Integer orderId) {
		// TODO Auto-generated method stub
		Double returnValue = null;
		Optional<Double> orderTotal = Optional.ofNullable(orderItemRepository.calculateGrandTotal(orderId));
		if (orderTotal.isPresent()) {
			returnValue = orderTotal.get();
		}
		return returnValue;
	}

	@Override
	public OrderDto getOrder(Integer orderId) {
		// TODO Auto-generated method stub
		OrderDto returnValue = null;
		Optional<OrderEntity> orderEntity = Optional.ofNullable(orderRepository.getById(orderId));
		if (orderEntity.isPresent()) {
			returnValue = tempConverter.orderEntityToDto(orderEntity.get());
		}
		return returnValue;
	}

	@Override
	public void deleteOrder(Integer orderId) {
		// TODO Auto-generated method stub
		orderRepository.deleteById(orderId);
		orderRepository.flush();
	}

	@Override
	public Integer getBookQuantity(Integer orderId) {
		// TODO Auto-generated method stub
		Integer returnValue = null;
		Optional<Integer> bookQuantity = Optional.ofNullable(orderItemRepository.findBookQuantity(orderId));
		if (bookQuantity.isPresent()) {
			returnValue = bookQuantity.get();
		}
		return returnValue;
	}

	@Override
	public OrderDto refreshOrder(Integer orderId,OrderDto order) {
		// TODO Auto-generated method stub

		OrderEntity orderEntity = tempConverter.orderDtoToEntity(order);
		orderEntity.setOrderId(orderId);
		OrderEntity storedOrder = orderRepository.saveAndFlush(orderEntity);
		OrderDto returnValue = tempConverter.orderEntityToDto(storedOrder);
		return returnValue;
	}

	@Override
	public List<OrderDto> listAllByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		List<OrderDto> returnValue = new ArrayList<OrderDto>();
		Optional<List<OrderEntity>> allOrders = Optional.ofNullable(orderRepository.findAllByCustomerId(customerId));
		if (!allOrders.isEmpty()) {
			for (OrderEntity order : allOrders.get()) {
				OrderDto orderDto = tempConverter.orderEntityToDto(order);
				returnValue.add(orderDto);
			}
		}
		return returnValue;
	}

	@Override
	public Double calculateOrdersValue(Integer customerId) {
		// TODO Auto-generated method stub
		Double returnValue = 0d;
		Optional<Double> ordersAmount = Optional.ofNullable(orderRepository.getOrdersValue(customerId));
		if(ordersAmount.isPresent()) {
			returnValue = ordersAmount.get();
		}
		return returnValue;
	}

}
