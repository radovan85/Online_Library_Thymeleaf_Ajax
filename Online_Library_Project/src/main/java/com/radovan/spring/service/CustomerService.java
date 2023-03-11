package com.radovan.spring.service;

import java.util.List;


import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.model.RegistrationForm;


public interface CustomerService {

	CustomerDto storeCustomer(RegistrationForm form);

	List<CustomerDto> getAllCustomers();

	CustomerDto getCustomer(Integer id);
	
	CustomerDto getCustomerByUserId(Integer userId);
	
	CustomerDto getCustomerByCartId(Integer cartId);
	
	CustomerDto updateCustomer(Integer customerId,CustomerDto customer);
	
	CustomerDto addCustomer(CustomerDto customer);
	
	void deleteCustomer(Integer customerId);
	
	void resetCustomer(Integer customerId);
}
