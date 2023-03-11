package com.radovan.spring.service;

import java.util.List;


import com.radovan.spring.dto.RoleDto;


public interface RoleService {

	List<RoleDto> listAllAuthorities();
	
	RoleDto getRoleById(Integer id);
}
