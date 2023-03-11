package com.radovan.spring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.service.OrderAddressService;

@Service
@Transactional
public class OrderAddressServiceImpl implements OrderAddressService{
	
	@Autowired
	private OrderAddressRepository addressRepository;
	
	@Autowired
	private TempConverter tempConverter;

	@Override
	public OrderAddressDto getAddressById(Integer addressId) {
		// TODO Auto-generated method stub
		OrderAddressDto returnValue = null;
		Optional<OrderAddressEntity> addressEntity = 
				Optional.ofNullable(addressRepository.getById(addressId));
		if(addressEntity.isPresent()) {
			returnValue = tempConverter.orderAddressEntityToDto(addressEntity.get());
		}
		return returnValue;
	}

	@Override
	public void deleteAddress(Integer addressId) {
		// TODO Auto-generated method stub
		addressRepository.deleteById(addressId);
		addressRepository.flush();
	}

}
