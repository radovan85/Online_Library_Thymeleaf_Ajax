package com.radovan.spring.service;

import com.radovan.spring.dto.OrderAddressDto;

public interface OrderAddressService {

	OrderAddressDto getAddressById(Integer addressId);
	
	void deleteAddress(Integer addressId);
}
