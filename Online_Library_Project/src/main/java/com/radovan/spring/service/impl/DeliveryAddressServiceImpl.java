package com.radovan.spring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.entity.DeliveryAddressEntity;
import com.radovan.spring.repository.DeliveryAddressRepository;
import com.radovan.spring.service.DeliveryAddressService;

@Service
@Transactional
public class DeliveryAddressServiceImpl implements DeliveryAddressService{
	
	@Autowired
	private DeliveryAddressRepository addressRepository;
	
	@Autowired
	private TempConverter tempConverter;

	@Override
	public DeliveryAddressDto getAddressById(Integer addressId) {
		// TODO Auto-generated method stub
		DeliveryAddressDto returnValue = null;
		Optional<DeliveryAddressEntity> addressEntity = 
				Optional.ofNullable(addressRepository.getById(addressId));
		if(addressEntity.isPresent()) {
			returnValue = tempConverter.deliveryAddressEntityToDto(addressEntity.get());
		}
		return returnValue;
	}

	@Override
	public DeliveryAddressDto createAddress(DeliveryAddressDto address) {
		// TODO Auto-generated method stub
		DeliveryAddressEntity addressEntity = tempConverter.deliveryAddressDtoToEntity(address);
		DeliveryAddressEntity storedAddress = addressRepository.save(addressEntity);
		DeliveryAddressDto returnValue = tempConverter.deliveryAddressEntityToDto(storedAddress);
		return returnValue;
	}

	@Override
	public void deleteAddress(Integer deliveryAddressId) {
		// TODO Auto-generated method stub
		addressRepository.deleteById(deliveryAddressId);
		addressRepository.flush();
	}

}
