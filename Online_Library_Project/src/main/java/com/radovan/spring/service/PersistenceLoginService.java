package com.radovan.spring.service;

import com.radovan.spring.dto.PersistenceLoginDto;

public interface PersistenceLoginService {

	PersistenceLoginDto addPersistenceLogin();
	
	PersistenceLoginDto getLastLogin(Integer customerId);
	
	void clearCustomerLogins(Integer customerId);
}
