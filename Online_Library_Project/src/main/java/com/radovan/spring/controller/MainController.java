package com.radovan.spring.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.exceptions.SuspendedUserException;
import com.radovan.spring.model.RegistrationForm;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.DeliveryAddressService;
import com.radovan.spring.service.PersistenceLoginService;
import com.radovan.spring.service.UserService;

@Controller
public class MainController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PersistenceLoginService persistenceService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DeliveryAddressService addressService;
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String sayIndex() {

		return "index";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home() {

		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, ModelMap map) {
		if (error != null) {
			map.put("error", "Invalid username and Password");
		}

		if (logout != null) {
			map.put("logout", "You have logged out successfully");
		}
		return "fragments/login :: ajaxLoadedContent";
	}

	

	@RequestMapping(value = "/userRegistration", method = RequestMethod.GET)
	public String register(ModelMap map) {

		RegistrationForm tempForm = new RegistrationForm();
		map.put("tempForm", tempForm);
		return "fragments/registration :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/userRegistration", method = RequestMethod.POST)
	public String storeUser(@ModelAttribute("tempForm") RegistrationForm form) {
		customerService.storeCustomer(form);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/registerComplete", method = RequestMethod.GET)
	public String registrationCompl() {
		return "fragments/registration_completed :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/registerFail", method = RequestMethod.GET)
	public String registrationFail() {
		return "fragments/registration_failed :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/loggedout", method = RequestMethod.POST)
	public String logout(RedirectAttributes redirectAttributes) {
		SecurityContextHolder.clearContext();
		return "fragments/homePage :: ajaxLoadedContent";
	}
	
	@RequestMapping(value="/loginErrorPage",method = RequestMethod.GET)
	public String logError(ModelMap map) {
		map.put("alert", "Invalid username or password!");
		return "fragments/login :: ajaxLoadedContent";
	}
	
	
	

	@RequestMapping(value = "/loginPassConfirm", method = RequestMethod.POST)
	public String confirmLoginPass(Principal principal) {
		Optional<Principal> authPrincipal = Optional.ofNullable(principal);
		if (!authPrincipal.isPresent()) {
			Error error = new Error("Invalid user");
			throw new InvalidUserException(error);
		}

		return "fragments/homePage :: ajaxLoadedContent";
	}
	
	
	@RequestMapping(value = "/suspensionChecker", method = RequestMethod.POST)
	public String checkForSuspension() {
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(authUser.getEnabled() == (byte) 0) {
			Error error = new Error("Account suspended!");
			throw new SuspendedUserException(error);
		}
		
		persistenceService.addPersistenceLogin();
		
		return "fragments/homePage :: ajaxLoadedContent";
	}
	
	@RequestMapping(value="/suspensionPage",method = RequestMethod.GET)
	public String suspensionAlert(ModelMap map) {
		map.put("alert", "Account suspended!");
		return "fragments/login :: ajaxLoadedContent";
	}
	
	
	@RequestMapping(value="/aboutUs",method = RequestMethod.GET)
	public String aboutPage() {
		return "fragments/about :: ajaxLoadedContent";
	}
	
	@Secured(value="ROLE_USER")
	@RequestMapping(value="/accountInfo")
	public String userAccountInfo(ModelMap map) {
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		DeliveryAddressDto address = addressService.getAddressById(customer.getDeliveryAddressId());
		map.put("authUser", authUser);
		map.put("address", address);
		return "fragments/accountDetails :: ajaxLoadedContent";
	}
	
	
}
