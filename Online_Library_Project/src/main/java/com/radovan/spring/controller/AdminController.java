package com.radovan.spring.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.BookGenreDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.LoyaltyCardRequestDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.PersistenceLoginDto;
import com.radovan.spring.dto.ReviewDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.exceptions.ImagePathException;
import com.radovan.spring.service.BookGenreService;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.DeliveryAddressService;
import com.radovan.spring.service.LoyaltyCardService;
import com.radovan.spring.service.OrderAddressService;
import com.radovan.spring.service.OrderItemService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.PersistenceLoginService;
import com.radovan.spring.service.ReviewService;
import com.radovan.spring.service.UserService;
import com.radovan.spring.service.WishListService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	private BookService bookService;

	@Autowired
	private BookGenreService genreService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private LoyaltyCardService loyaltyCardService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private DeliveryAddressService addressService;

	@Autowired
	private PersistenceLoginService persistenceService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private OrderAddressService orderAddressService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private WishListService wishListService;
	
	

	@RequestMapping(value = "/createBook")
	public String renderBookForm(ModelMap map) {
		BookDto book = new BookDto();
		List<BookGenreDto> allGenres = genreService.listAll();
		map.put("book", book);
		map.put("allGenres", allGenres);
		return "fragments/bookForm :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/createBook", method = RequestMethod.POST)
	public String createBook(@ModelAttribute("book") BookDto book, ModelMap map,
			@RequestParam("bookImage") MultipartFile file, @RequestParam("imgName") String imgName) throws Throwable {

		String fileLocation = "C:\\Users\\Radovan\\eclipse-workspace\\Online_Library_Project\\src\\main\\resources\\static\\images\\bookImages";
		String imageUUID;

		Path locationPath = Paths.get(fileLocation);

		if (!Files.exists(locationPath)) {
			Error error = new Error("Invalid file path!");
			throw new ImagePathException(error);
		}

		imageUUID = file.getOriginalFilename();
		Path fileNameAndPath = Paths.get(fileLocation, imageUUID);

		if (file != null && !file.isEmpty()) {
			Files.write(fileNameAndPath, file.getBytes());
			System.out.println("IMage Save at:" + fileNameAndPath.toString());
		} else {
			imageUUID = imgName;
		}

		book.setImageName(imageUUID);
		bookService.addBook(book);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/deleteBook/{bookId}")
	public String deleteBook(@PathVariable("bookId") Integer bookId) throws Throwable {

		BookDto book = bookService.getBookById(bookId);
		Path path = Paths.get(
				"C:\\Users\\Radovan\\eclipse-workspace\\Online_Library_Project\\src\\main\\resources\\static\\images\\bookImages\\"
						+ book.getImageName());

		if (Files.exists(path)) {
			Files.delete(path);

		} else {
			Error error = new Error("Invalid file path!");
			throw new ImagePathException(error);
		}

		cartItemService.eraseAllByBookId(bookId);
		bookService.removeBookFromAllWishlist(bookId);
		bookService.deleteBook(bookId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/updateBook/{bookId}")
	public String renderUpdateForm(@PathVariable("bookId") Integer bookId, ModelMap map) {
		BookDto book = new BookDto();
		BookDto currentBook = bookService.getBookById(bookId);
		List<BookGenreDto> allGenres = genreService.listAll();
		map.put("book", book);
		map.put("currentBook", currentBook);
		map.put("allGenres", allGenres);
		return "fragments/bookUpdateForm :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/createGenre")
	public String renderGenreForm(ModelMap map) {
		BookGenreDto genre = new BookGenreDto();
		map.put("genre", genre);
		return "fragments/genreForm :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/createGenre", method = RequestMethod.POST)
	public String createGenre(@ModelAttribute("genre") BookGenreDto genre, ModelMap map) {
		genreService.addGenre(genre);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/allGenres")
	public String listAllGenres(ModelMap map) {
		List<BookGenreDto> allGenres = genreService.listAll();
		map.put("allGenres", allGenres);
		map.put("recordsPerPage", 6);
		return "fragments/genreList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/deleteGenre/{genreId}")
	public String removeGenre(@PathVariable("genreId") Integer genreId) {
		genreService.deleteGenre(genreId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/updateGenre/{genreId}")
	public String updateGenre(@PathVariable("genreId") Integer genreId, ModelMap map) {
		BookGenreDto genre = new BookGenreDto();
		BookGenreDto currentGenre = genreService.getGenreById(genreId);
		map.put("genre", genre);
		map.put("currentGenre", currentGenre);
		return "fragments/genreUpdateForm :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/allReviews")
	public String reviewList(ModelMap map) {
		List<ReviewDto> approvedReviews = reviewService.listAllAuthorized();
		List<BookDto> allBooks = bookService.listAll();
		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("approvedReviews", approvedReviews);
		map.put("allBooks", allBooks);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/reviewList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/allRequestedReviews")
	public String requestedReviewsList(ModelMap map) {
		List<ReviewDto> pendingReviews = reviewService.listAllOnHold();
		List<BookDto> allBooks = bookService.listAll();
		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("pendingReviews", pendingReviews);
		map.put("allBooks", allBooks);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/reviewRequestsList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/pendingReviewDetails/{reviewId}")
	public String getPendingReview(@PathVariable("reviewId") Integer reviewId, ModelMap map) {
		ReviewDto currentReview = reviewService.getReviewById(reviewId);
		CustomerDto tempCustomer = customerService.getCustomer(currentReview.getAuthorId());
		UserDto tempUser = userService.getUserById(tempCustomer.getUserId());
		BookDto currentBook = bookService.getBookById(currentReview.getBookId());
		map.put("currentReview", currentReview);
		map.put("tempUser", tempUser);
		map.put("currentBook", currentBook);
		return "fragments/pendingReviewDetails :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/reviewDetails/{reviewId}")
	public String getReviewDetails(@PathVariable("reviewId") Integer reviewId, ModelMap map) {
		ReviewDto currentReview = reviewService.getReviewById(reviewId);
		CustomerDto tempCustomer = customerService.getCustomer(currentReview.getAuthorId());
		UserDto tempUser = userService.getUserById(tempCustomer.getUserId());
		BookDto currentBook = bookService.getBookById(currentReview.getBookId());
		map.put("currentReview", currentReview);
		map.put("tempUser", tempUser);
		map.put("currentBook", currentBook);
		return "fragments/reviewDetails :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/authorizeReview/{reviewId}", method = RequestMethod.POST)
	public String reviewAuthorization(@PathVariable("reviewId") Integer reviewId) {
		reviewService.authorizeReview(reviewId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/rejectReview/{reviewId}")
	public String rejectReview(@PathVariable("reviewId") Integer reviewId) {
		reviewService.deleteReview(reviewId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/checkCardRequests")
	public String allCardRequests(ModelMap map) {
		List<LoyaltyCardRequestDto> allRequests = loyaltyCardService.listAllCardRequests();
		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allRequests", allRequests);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/cardRequestList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/getAllCards")
	public String getAllCards(ModelMap map) {
		List<LoyaltyCardDto> allCards = loyaltyCardService.listAllLoyaltyCards();
		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allCards", allCards);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/loyaltyCardList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/authorizeCard/{cardRequestId}")
	public String authorizeLoyaltyCard(@PathVariable("cardRequestId") Integer cardRequestId, ModelMap map) {
		loyaltyCardService.authorizeRequest(cardRequestId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/rejectCard/{cardRequestId}")
	public String rejectLoyaltyCard(@PathVariable("cardRequestId") Integer cardRequestId, ModelMap map) {
		loyaltyCardService.rejectRequest(cardRequestId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/allOrders", method = RequestMethod.GET)
	public String listAllOrders(ModelMap map) {

		List<OrderDto> allOrders = orderService.listAll();
		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allOrders", allOrders);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/orderList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/allOrders/{customerId}")
	public String allOrdersByCustomerId(@PathVariable("customerId") Integer customerId, ModelMap map) {
		List<OrderDto> allOrders = orderService.listAllByCustomerId(customerId);
		CustomerDto customer = customerService.getCustomer(customerId);
		UserDto user = userService.getUserById(customer.getUserId());
		map.put("allOrders", allOrders);
		map.put("tempUser", user);
		map.put("recordsPerPage", 6);
		return "fragments/customerOrderList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/deleteOrder/{orderId}", method = RequestMethod.GET)
	public String deleteOrder(@PathVariable("orderId") Integer orderId) {

		orderItemService.eraseAllByOrderId(orderId);
		orderService.deleteOrder(orderId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/getOrder/{orderId}", method = RequestMethod.GET)
	public String orderDetails(@PathVariable("orderId") Integer orderId, ModelMap map) {

		OrderDto order = orderService.getOrder(orderId);
		OrderAddressDto address = orderAddressService.getAddressById(order.getAddressId());
		Double orderPrice = orderService.calculateOrderTotal(orderId);
		List<OrderItemDto> orderedItems = orderItemService.listAllByOrderId(orderId);
		map.put("order", order);
		map.put("address", address);
		map.put("orderPrice", orderPrice);
		map.put("orderedItems", orderedItems);
		return "fragments/orderDetails :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/allCustomers", method = RequestMethod.GET)
	public String customerList(ModelMap map) {

		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/customerList :: ajaxLoadedContent";

	}
	
	@RequestMapping(value = "/deleteCustomer/{customerId}", method = RequestMethod.GET)
	public String removeCustomer(@PathVariable("customerId") Integer customerId) {
		CustomerDto customer = customerService.getCustomer(customerId);
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		DeliveryAddressDto address = addressService.getAddressById(customer.getDeliveryAddressId());
		UserDto user = userService.getUserById(customer.getUserId());
		
		
		List<OrderDto> allOrders = orderService.listAllByCustomerId(customerId);
		for(OrderDto order:allOrders) {
			orderItemService.eraseAllByOrderId(order.getOrderId());
			orderService.deleteOrder(order.getOrderId());
		}
		
		cartItemService.eraseAllCartItems(cart.getCartId());
		reviewService.deleteAllByCustomerId(customerId);
		persistenceService.clearCustomerLogins(customerId);
			
		customerService.resetCustomer(customerId);
		Optional<Integer> wishListIdOpt = Optional.ofNullable(customer.getWishListId());
		if(wishListIdOpt.isPresent()) {
			wishListService.deleteWishList(wishListIdOpt.get());
		}
		
		Optional<Integer> loyaltyCardIdOpt = Optional.ofNullable(customer.getLoyaltyCardId());
		if(loyaltyCardIdOpt.isPresent()) {
			loyaltyCardService.deleteLoyaltyCard(loyaltyCardIdOpt.get());
		}
		
		Optional<LoyaltyCardRequestDto> cardRequestOpt = 
				Optional.ofNullable(loyaltyCardService.getRequestByCustomerId(customerId));
		if(cardRequestOpt.isPresent()) {
			loyaltyCardService.rejectRequest(cardRequestOpt.get().getId());
		}
		
		addressService.deleteAddress(address.getDeliveryAddressId());
		cartService.deleteCart(cart.getCartId());		
		customerService.deleteCustomer(customerId);
		userService.deleteUser(user.getId());
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/getCustomer/{customerId}", method = RequestMethod.GET)
	public String getCustomer(@PathVariable("customerId") Integer customerId, ModelMap map) {

		CustomerDto customer = customerService.getCustomer(customerId);
		UserDto tempUser = userService.getUserById(customer.getUserId());
		DeliveryAddressDto address = addressService.getAddressById(customer.getDeliveryAddressId());
		PersistenceLoginDto persistence = persistenceService.getLastLogin(customerId);
		Double ordersValue = orderService.calculateOrdersValue(customerId);
		map.put("tempCustomer", customer);
		map.put("tempUser", tempUser);
		map.put("address", address);
		map.put("persistence", persistence);
		map.put("ordersValue", ordersValue);
		return "fragments/customerDetails :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/suspendUser/{userId}")
	public String suspendUser(@PathVariable("userId") Integer userId) {
		userService.suspendUser(userId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/reactivateUser/{userId}")
	public String reactivateUser(@PathVariable("userId") Integer userId) {
		userService.reactivateUser(userId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/invalidPath", method = RequestMethod.GET)
	public String invalidImagePath() {
		return "fragments/invalidImagePath :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/genreError", method = RequestMethod.GET)
	public String fireGenreExc() {
		return "fragments/genreError :: ajaxLoadedContent";
	}
}
