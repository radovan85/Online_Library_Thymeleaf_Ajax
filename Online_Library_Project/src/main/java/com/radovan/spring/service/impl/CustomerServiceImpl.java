package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.DeliveryAddressEntity;
import com.radovan.spring.entity.LoyaltyCardEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.entity.WishListEntity;
import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.model.RegistrationForm;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.DeliveryAddressRepository;
import com.radovan.spring.repository.LoyaltyCardRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.repository.WishListRepository;
import com.radovan.spring.service.CustomerService;

@Service("customerServiceHandler")
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private WishListRepository wishListRepository;
	
	@Autowired
	private DeliveryAddressRepository addressRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private LoyaltyCardRepository loyaltyCardRepository;

	@Override
	public List<CustomerDto> getAllCustomers() {
		// TODO Auto-generated method stub
		Optional<List<CustomerEntity>> allCustomers = Optional.ofNullable(customerRepository.findAll());
		List<CustomerDto> returnValue = new ArrayList<CustomerDto>();
		if (!allCustomers.isEmpty()) {
			for (CustomerEntity customer : allCustomers.get()) {
				CustomerDto customerDto = tempConverter.customerEntityToDto(customer);
				returnValue.add(customerDto);
			}
		}

		return returnValue;
	}

	@Override
	public CustomerDto getCustomer(Integer id) {
		// TODO Auto-generated method stub
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(customerRepository.getById(id));
		CustomerDto returnValue = new CustomerDto();
		if (customerEntity.isPresent()) {
			returnValue = tempConverter.customerEntityToDto(customerEntity.get());
		} else {
			Error error = new Error("Invalid Customer");
			throw new RuntimeErrorException(error);
		}
		return returnValue;
	}

	@Override
	public CustomerDto getCustomerByUserId(Integer userId) {
		// TODO Auto-generated method stub

		Optional<CustomerEntity> customerEntity = Optional.ofNullable(customerRepository.findByUserId(userId));
		CustomerDto returnValue = new CustomerDto();
		if (customerEntity.isPresent()) {
			returnValue = tempConverter.customerEntityToDto(customerEntity.get());
		} else {
			Error error = new Error("Invalid Customer");
			throw new RuntimeErrorException(error);
		}
		return returnValue;
	}

	@Override
	public CustomerDto getCustomerByCartId(Integer cartId) {
		// TODO Auto-generated method stub

		Optional<CustomerEntity> customerEntity = Optional.ofNullable(customerRepository.findByCartId(cartId));
		CustomerDto returnValue = new CustomerDto();
		if (customerEntity.isPresent()) {
			returnValue = tempConverter.customerEntityToDto(customerEntity.get());
		} else {
			Error error = new Error("Invalid Customer");
			throw new RuntimeErrorException(error);
		}
		return returnValue;
	}

	@Override
	public CustomerDto updateCustomer(Integer customerId, CustomerDto customer) {
		// TODO Auto-generated method stub
		Optional<CustomerEntity> tempCustomer = Optional.ofNullable(customerRepository.getById(customerId));
		CustomerDto returnValue = null;

		if (tempCustomer.isPresent()) {
			CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customer);
			customerEntity.setCustomerId(tempCustomer.get().getCustomerId());
			customerEntity.setCart(tempCustomer.get().getCart());
			customerEntity.setUser(tempCustomer.get().getUser());

			CustomerEntity updatedCustomer = customerRepository.saveAndFlush(customerEntity);
			returnValue = tempConverter.customerEntityToDto(updatedCustomer);
		}
		return returnValue;
	}

	@Override
	public CustomerDto storeCustomer(RegistrationForm form) {
		// TODO Auto-generated method stub
		UserDto userDto = form.getUser();
		Optional<UserEntity> testUser = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
		if (testUser.isPresent()) {
			Error error = new Error("Email exists");
			throw new ExistingEmailException(error);
		}

		RoleEntity role = roleRepository.findByRole("ROLE_USER");
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userDto.setEnabled((byte) 1);
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		roles.add(role);
		UserEntity userEntity = tempConverter.userDtoToEntity(userDto);
		userEntity.setRoles(roles);
		userEntity.setEnabled((byte) 1);
		UserEntity storedUser = userRepository.save(userEntity);
		List<UserEntity> users = new ArrayList<UserEntity>();
		users.add(storedUser);
		role.setUsers(users);
		roleRepository.saveAndFlush(role);

		CartEntity cartEntity = new CartEntity();
		cartEntity.setCartPrice(0d);
		CartEntity storedCart = cartRepository.save(cartEntity);
		
		WishListEntity wishListEntity = new WishListEntity();
		WishListEntity storedWishList = wishListRepository.save(wishListEntity);
		
		DeliveryAddressDto deliveryAddress = form.getAddress();
		DeliveryAddressEntity deliveryAddressEntity = 
				tempConverter.deliveryAddressDtoToEntity(deliveryAddress);
		DeliveryAddressEntity storedAddress = addressRepository.save(deliveryAddressEntity);

		CustomerDto customerDto = form.getCustomer();
		customerDto.setUserId(storedUser.getId());
		customerDto.setCartId(storedCart.getCartId());
		customerDto.setWishListId(storedWishList.getWishListId());
		customerDto.setDeliveryAddressId(storedAddress.getDeliveryAddressId());

		CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customerDto);
		ZonedDateTime currentTime = LocalDateTime.now().atZone(ZoneId.of("Europe/Belgrade"));
		Timestamp registrationDate = new Timestamp(currentTime.toInstant().getEpochSecond() * 1000L);
		customerEntity.setRegistrationDate(registrationDate);
		CustomerEntity storedCustomer = customerRepository.save(customerEntity);

		storedCart.setCustomer(storedCustomer);
		cartRepository.saveAndFlush(storedCart);
		
		storedWishList.setCustomer(storedCustomer);
		wishListRepository.saveAndFlush(storedWishList);
		
		storedAddress.setCustomer(storedCustomer);
		addressRepository.saveAndFlush(storedAddress);

		CustomerDto returnValue = tempConverter.customerEntityToDto(storedCustomer);
		return returnValue;
	}

	@Override
	public CustomerDto addCustomer(CustomerDto customer) {
		// TODO Auto-generated method stub

		CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customer);
		CustomerEntity storedCustomer = customerRepository.save(customerEntity);
		CustomerDto returnValue = tempConverter.customerEntityToDto(storedCustomer);
		return returnValue;
	}
	
	@Override
	public void deleteCustomer(Integer customerId) {
		// TODO Auto-generated method stub
		customerRepository.deleteById(customerId);
		customerRepository.flush();
	}

	@Override
	public void resetCustomer(Integer customerId) {
		// TODO Auto-generated method stub
		Optional<CustomerEntity> customerOptional = Optional.ofNullable(customerRepository.getById(customerId));
		if (customerOptional.isPresent()) {
			CustomerEntity customer = customerOptional.get();
			Optional<CartEntity> cartOptional = Optional.ofNullable(customer.getCart());
			if (cartOptional.isPresent()) {
				CartEntity cartEntity = cartOptional.get();
				cartEntity.setCustomer(null);
				cartRepository.saveAndFlush(cartEntity);
				
				Optional<WishListEntity> wishListOpt = 
						Optional.ofNullable(customer.getWishList());
				if(wishListOpt.isPresent()) {
					WishListEntity wishlist = wishListOpt.get();
					wishlist.setCustomer(null);
					wishListRepository.saveAndFlush(wishlist);
				}
				
				Optional<LoyaltyCardEntity> loyaltyCardOpt = 
						Optional.ofNullable(customer.getLoyaltyCard());
				if(loyaltyCardOpt.isPresent()) {
					LoyaltyCardEntity loyaltyCard = loyaltyCardOpt.get();
					loyaltyCard.setCustomer(null);
					loyaltyCardRepository.saveAndFlush(loyaltyCard);
				}
				
				customer.setCart(null);
				customer.setDeliveryAddress(null);
				customer.setUser(null);
				customer.setWishList(null);
				customer.setLoyaltyCard(null);
				customerRepository.saveAndFlush(customer);
			}
		}
	}

}
