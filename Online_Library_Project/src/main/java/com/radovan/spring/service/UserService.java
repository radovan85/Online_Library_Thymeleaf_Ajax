package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.UserEntity;

public interface UserService {

	UserDto updateUser(Integer id, UserDto user);

	void deleteUser(Integer id);

	UserDto getUserById(Integer id);

	List<UserDto> listAllUsers();

	UserEntity getUserByEmail(String email);

	UserDto storeUser(UserDto user);

	UserDto getCurrentUser();
	
	void suspendUser(Integer userId);
	
	void reactivateUser(Integer userId);

}
