package com.radovan.spring.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;

@Component
public class LoadData {

	private RoleRepository roleRepository;

	private UserRepository userRepository;

	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public LoadData(RoleRepository roleRepository, UserRepository userRepository,
			BCryptPasswordEncoder passwordEncoder) {
		super();
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		addRolesData();
		addAdminData();
	}

	public void addRolesData() {
		RoleEntity role1 = new RoleEntity("ADMIN");
		RoleEntity role2 = new RoleEntity("ROLE_USER");

		try {
			roleRepository.save(role1);
			roleRepository.save(role2);

		} catch (Exception exc) {
			System.out.println("Roles are already added");
		}

	}

	public void addAdminData() {

		RoleEntity role = roleRepository.findByRole("ADMIN");
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		roles.add(role);
		UserEntity adminEntity = new UserEntity("John", "Doe", "doe@luv2code.com", "admin123",(byte) 1);
		adminEntity.setPassword(passwordEncoder.encode(adminEntity.getPassword()));

		adminEntity.setRoles(roles);
		try {
		UserEntity storedAdmin = userRepository.save(adminEntity);

		List<UserEntity> users = new ArrayList<UserEntity>();
		users.add(storedAdmin);
		role.setUsers(users);
		roleRepository.saveAndFlush(role);
		}
		catch(Exception exc) {
			System.out.println("Admin already added");
		}

	}
}
