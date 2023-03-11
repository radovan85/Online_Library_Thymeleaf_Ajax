package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.service.UserService;

@Service("userServiceHandler")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public UserDto updateUser(Integer id, UserDto user) {
		// TODO Auto-generated method stub
		Optional<UserEntity> tempUser = Optional.ofNullable(userRepository.getById(id));
		UserDto returnValue = null;

		if (tempUser.isPresent()) {
			UserEntity userEntity = tempConverter.userDtoToEntity(user);
			userEntity.setEnabled(tempUser.get().getEnabled());
			userEntity.setRoles(tempUser.get().getRoles());
			userEntity.setId(tempUser.get().getId());
			UserEntity updatedUser = userRepository.saveAndFlush(userEntity);
			returnValue = tempConverter.userEntityToDto(updatedUser);
		} else {
			Error error = new Error("Invalid user");
			throw new InvalidUserException(error);
		}

		return returnValue;
	}

	@Override
	public void deleteUser(Integer id) {
		// TODO Auto-generated method stub
		userRepository.clearUserRoles(id);
		userRepository.deleteById(id);
		userRepository.flush();
	}

	@Override
	public UserDto getUserById(Integer id) {
		// TODO Auto-generated method stub
		Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.getById(id));
		UserDto returnValue = null;

		if (userEntity.isPresent()) {
			returnValue = tempConverter.userEntityToDto(userEntity.get());
		} else {
			Error error = new Error("Invalid User");
			throw new InvalidUserException(error);
		}

		return returnValue;
	}

	@Override
	public List<UserDto> listAllUsers() {
		// TODO Auto-generated method stub
		Optional<List<UserEntity>> allUsers = Optional.ofNullable(userRepository.findAll());
		List<UserDto> returnValue = new ArrayList<UserDto>();

		if (!allUsers.isEmpty()) {
			for (UserEntity userEntity : allUsers.get()) {
				UserDto userDto = tempConverter.userEntityToDto(userEntity);
				returnValue.add(userDto);
			}
		}
		return returnValue;
	}

	@Override
	public UserEntity getUserByEmail(String email) {
		// TODO Auto-generated method stub

		Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findByEmail(email));
		UserEntity returnValue = null;

		if (userEntity.isPresent()) {
			returnValue = userEntity.get();
		} else {
			Error error = new Error("Invalid User");
			throw new InvalidUserException(error);
		}

		return returnValue;
	}

	@Override
	public UserDto storeUser(UserDto user) {
		// TODO Auto-generated method stub

		Optional<UserEntity> testUser = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
		if (testUser.isPresent()) {
			Error error = new Error("Email exists");
			throw new ExistingEmailException(error);
		}
		RoleEntity role = roleRepository.findByRole("ROLE_USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled((byte) 1);
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		roles.add(role);
		UserEntity userEntity = tempConverter.userDtoToEntity(user);
		userEntity.setRoles(roles);
		userEntity.setEnabled((byte) 1);
		UserEntity storedUser = userRepository.save(userEntity);
		List<UserEntity> users = new ArrayList<UserEntity>();
		users.add(storedUser);
		role.setUsers(users);
		roleRepository.saveAndFlush(role);

		UserDto returnValue = tempConverter.userEntityToDto(storedUser);
		return returnValue;
	}

	@Override
	public UserDto getCurrentUser() {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity userEntity = userRepository.getById(authUser.getId());
		UserDto returnValue = tempConverter.userEntityToDto(userEntity);
		return returnValue;
	}

	@Override
	public void suspendUser(Integer userId) {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepository.getById(userId);
		userEntity.setEnabled((byte) 0);
		userRepository.saveAndFlush(userEntity);
	}

	@Override
	public void reactivateUser(Integer userId) {
		// TODO Auto-generated method stub
		Optional<UserEntity> user = Optional.ofNullable(userRepository.getById(userId));
		if(user.isPresent()) {
			UserEntity userEntity = user.get();
			userEntity.setEnabled((byte) 1);
			userRepository.saveAndFlush(userEntity);
		}
	}

}
