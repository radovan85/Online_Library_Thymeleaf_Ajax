package com.radovan.spring.model;

import java.io.Serializable;

import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.UserDto;

public class RegistrationForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UserDto user;
	
	private CustomerDto customer;
	
	private DeliveryAddressDto address;

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public CustomerDto getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}

	public DeliveryAddressDto getAddress() {
		return address;
	}

	public void setAddress(DeliveryAddressDto address) {
		this.address = address;
	}
	
	

}
