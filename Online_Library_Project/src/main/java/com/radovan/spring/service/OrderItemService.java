package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.OrderItemDto;

public interface OrderItemService {

	List<OrderItemDto> listAllByOrderId(Integer orderId);
	
	void eraseAllByOrderId(Integer orderId);
}
