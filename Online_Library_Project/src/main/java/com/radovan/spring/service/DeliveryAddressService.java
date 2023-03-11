package com.radovan.spring.service;

import com.radovan.spring.dto.DeliveryAddressDto;

public interface DeliveryAddressService {

	DeliveryAddressDto getAddressById(Integer addressId);
	
	DeliveryAddressDto createAddress(DeliveryAddressDto address);

	void deleteAddress(Integer deliveryAddressId);
}
