package com.radovan.spring.converter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.BookGenreDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.LoyaltyCardRequestDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.PersistenceLoginDto;
import com.radovan.spring.dto.ReviewDto;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.dto.WishListDto;
import com.radovan.spring.entity.BookEntity;
import com.radovan.spring.entity.BookGenreEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.DeliveryAddressEntity;
import com.radovan.spring.entity.LoyaltyCardEntity;
import com.radovan.spring.entity.LoyaltyCardRequestEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.PersistenceLoginEntity;
import com.radovan.spring.entity.ReviewEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.entity.WishListEntity;
import com.radovan.spring.repository.BookGenreRepository;
import com.radovan.spring.repository.BookRepository;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.DeliveryAddressRepository;
import com.radovan.spring.repository.LoyaltyCardRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.repository.PersistenceLoginRepository;
import com.radovan.spring.repository.ReviewRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.repository.WishListRepository;

public class TempConverter {

	@Autowired
	private BookGenreRepository genreRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private WishListRepository wishListRepository;

	@Autowired
	private LoyaltyCardRepository loyaltyCardRepository;

	@Autowired
	private DeliveryAddressRepository addressRepository;

	@Autowired
	private PersistenceLoginRepository persistenceRepository;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	@Autowired
	private ModelMapper mapper;

	public BookDto bookEntityToDto(BookEntity bookEntity) {
		BookDto returnValue = mapper.map(bookEntity, BookDto.class);
		Optional<BookGenreEntity> genre = Optional.ofNullable(bookEntity.getGenre());
		if (genre.isPresent()) {
			returnValue.setGenreId(genre.get().getGenreId());
		}

		Optional<List<ReviewEntity>> reviews = Optional.ofNullable(bookEntity.getReviews());
		List<Integer> reviewsIds = new ArrayList<Integer>();
		if (!reviews.isEmpty()) {
			for (ReviewEntity review : reviews.get()) {
				reviewsIds.add(review.getReviewId());
			}
		}

		returnValue.setReviewsIds(reviewsIds);
		return returnValue;
	}

	public BookEntity bookDtoToEntity(BookDto book) {
		BookEntity returnValue = mapper.map(book, BookEntity.class);
		Optional<Integer> genreId = Optional.ofNullable(book.getGenreId());
		if (genreId.isPresent()) {
			BookGenreEntity genre = genreRepository.getById(genreId.get());
			returnValue.setGenre(genre);
		}

		Optional<List<Integer>> reviewsIds = Optional.ofNullable(book.getReviewsIds());
		List<ReviewEntity> reviews = new ArrayList<ReviewEntity>();
		if (!reviewsIds.isEmpty()) {
			for (Integer reviewId : reviewsIds.get()) {
				ReviewEntity review = reviewRepository.getById(reviewId);
				reviews.add(review);
			}
		}

		returnValue.setReviews(reviews);
		return returnValue;
	}

	public BookGenreDto bookGenreEntityToDto(BookGenreEntity genreEntity) {
		BookGenreDto returnValue = mapper.map(genreEntity, BookGenreDto.class);
		Optional<List<BookEntity>> books = Optional.ofNullable(genreEntity.getBooks());
		List<Integer> booksIds = new ArrayList<Integer>();
		if (!books.isEmpty()) {
			for (BookEntity book : books.get()) {
				booksIds.add(book.getBookId());
			}
		}

		returnValue.setBooksIds(booksIds);
		return returnValue;

	}

	public BookGenreEntity bookGenreDtoToEntity(BookGenreDto genre) {
		BookGenreEntity returnValue = mapper.map(genre, BookGenreEntity.class);
		Optional<List<Integer>> booksIds = Optional.ofNullable(genre.getBooksIds());
		List<BookEntity> books = new ArrayList<BookEntity>();
		if (!booksIds.isEmpty()) {
			for (Integer bookId : booksIds.get()) {
				BookEntity bookEntity = bookRepository.getById(bookId);
				books.add(bookEntity);
			}
		}

		returnValue.setBooks(books);
		return returnValue;
	}

	public CartDto cartEntityToDto(CartEntity cartEntity) {
		CartDto returnValue = mapper.map(cartEntity, CartDto.class);
		Optional<Double> cartPrice = Optional.ofNullable(cartEntity.getCartPrice());
		if (!cartPrice.isPresent()) {
			returnValue.setCartPrice(0d);
		}
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(cartEntity.getCustomer());
		if (customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}

		List<Integer> itemsIds = new ArrayList<>();
		Optional<List<CartItemEntity>> cartItems = Optional.ofNullable(cartEntity.getCartItems());
		if (!cartItems.isEmpty()) {
			for (CartItemEntity itemEntity : cartItems.get()) {
				Integer itemId = itemEntity.getCartItemId();
				itemsIds.add(itemId);
			}

		}
		returnValue.setCartItemsIds(itemsIds);
		return returnValue;

	}

	public CartEntity cartDtoToEntity(CartDto cartDto) {
		CartEntity returnValue = mapper.map(cartDto, CartEntity.class);
		Optional<Double> cartPrice = Optional.ofNullable(cartDto.getCartPrice());
		if (!cartPrice.isPresent()) {
			returnValue.setCartPrice(0d);
		}
		Optional<Integer> customerId = Optional.ofNullable(cartDto.getCustomerId());
		if (customerId.isPresent()) {
			CustomerEntity customerEntity = customerRepository.getById(customerId.get());
			returnValue.setCustomer(customerEntity);
		}

		List<CartItemEntity> cartItems = new ArrayList<>();
		Optional<List<Integer>> itemIds = Optional.ofNullable(cartDto.getCartItemsIds());

		if (!itemIds.isEmpty()) {
			for (Integer itemId : itemIds.get()) {
				CartItemEntity itemEntity = cartItemRepository.getById(itemId);
				cartItems.add(itemEntity);
			}

		}
		returnValue.setCartItems(cartItems);
		return returnValue;
	}

	public CartItemDto cartItemEntityToDto(CartItemEntity cartItemEntity) {
		CartItemDto returnValue = mapper.map(cartItemEntity, CartItemDto.class);
		Optional<BookEntity> book = Optional.ofNullable(cartItemEntity.getBook());
		if (book.isPresent()) {
			returnValue.setBookId(book.get().getBookId());
		}

		Optional<CartEntity> cart = Optional.ofNullable(cartItemEntity.getCart());
		if (cart.isPresent()) {
			returnValue.setCartId(cart.get().getCartId());
		}

		return returnValue;
	}

	public CartItemEntity cartItemDtoToEntity(CartItemDto cartItemDto) {
		CartItemEntity returnValue = mapper.map(cartItemDto, CartItemEntity.class);
		Optional<Integer> cartId = Optional.ofNullable(cartItemDto.getCartId());
		if (cartId.isPresent()) {
			CartEntity cartEntity = cartRepository.getById(cartId.get());
			returnValue.setCart(cartEntity);
		}

		Optional<Integer> bookId = Optional.ofNullable(cartItemDto.getBookId());
		if (bookId.isPresent()) {
			BookEntity bookEntity = bookRepository.getById(bookId.get());
			returnValue.setBook(bookEntity);
		}

		return returnValue;
	}

	public CustomerDto customerEntityToDto(CustomerEntity customerEntity) {
		CustomerDto returnValue = mapper.map(customerEntity, CustomerDto.class);
		Optional<UserEntity> userEntity = Optional.ofNullable(customerEntity.getUser());
		if (userEntity.isPresent()) {
			returnValue.setUserId(userEntity.get().getId());
		}

		Optional<CartEntity> cartEntity = Optional.ofNullable(customerEntity.getCart());
		if (cartEntity.isPresent()) {
			returnValue.setCartId(cartEntity.get().getCartId());
		}

		Optional<WishListEntity> wishListEntity = Optional.ofNullable(customerEntity.getWishList());
		if (wishListEntity.isPresent()) {
			returnValue.setWishListId(wishListEntity.get().getWishListId());
		}

		Optional<LoyaltyCardEntity> loyaltyCard = Optional.ofNullable(customerEntity.getLoyaltyCard());
		if (loyaltyCard.isPresent()) {
			returnValue.setLoyaltyCardId(loyaltyCard.get().getLoyaltyCardId());
		}

		Optional<DeliveryAddressEntity> address = Optional.ofNullable(customerEntity.getDeliveryAddress());
		if (address.isPresent()) {
			returnValue.setDeliveryAddressId(address.get().getDeliveryAddressId());
		}

		Optional<List<ReviewEntity>> reviews = Optional.ofNullable(customerEntity.getReviews());
		List<Integer> reviewsIds = new ArrayList<Integer>();
		if (reviews.isPresent()) {
			for (ReviewEntity review : reviews.get()) {
				reviewsIds.add(review.getReviewId());
			}
		}

		returnValue.setReviewsIds(reviewsIds);

		List<Integer> persistenceLoginsIds = new ArrayList<Integer>();
		Optional<List<PersistenceLoginEntity>> persistenceLogins = Optional
				.ofNullable(customerEntity.getPersistenceLogins());
		if (!persistenceLogins.isEmpty()) {
			for (PersistenceLoginEntity persistence : persistenceLogins.get()) {
				persistenceLoginsIds.add(persistence.getId());
			}
		}

		returnValue.setPersistenceLoginsIds(persistenceLoginsIds);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Optional<Timestamp> registrationDateOpt = Optional.ofNullable(customerEntity.getRegistrationDate());
		if (registrationDateOpt.isPresent()) {
			String registrationDateStr = registrationDateOpt.get().toLocalDateTime()
					.atZone(ZoneId.of("Europe/Belgrade")).format(formatter);
			returnValue.setRegistrationDateStr(registrationDateStr);
		}
		return returnValue;
	}

	public CustomerEntity customerDtoToEntity(CustomerDto customer) {
		CustomerEntity returnValue = mapper.map(customer, CustomerEntity.class);
		Optional<Integer> userId = Optional.ofNullable(customer.getUserId());
		if (userId.isPresent()) {
			UserEntity userEntity = userRepository.getById(userId.get());
			returnValue.setUser(userEntity);
		}

		Optional<Integer> cartId = Optional.ofNullable(customer.getCartId());
		if (cartId.isPresent()) {
			CartEntity cartEntity = cartRepository.getById(cartId.get());
			returnValue.setCart(cartEntity);
		}

		Optional<Integer> wishListId = Optional.ofNullable(customer.getWishListId());
		if (wishListId.isPresent()) {
			WishListEntity wishListEntity = wishListRepository.getById(wishListId.get());
			returnValue.setWishList(wishListEntity);
		}

		Optional<Integer> loyaltyCardId = Optional.ofNullable(customer.getLoyaltyCardId());
		if (loyaltyCardId.isPresent()) {
			LoyaltyCardEntity cardEntity = loyaltyCardRepository.getById(loyaltyCardId.get());
			returnValue.setLoyaltyCard(cardEntity);
		}

		Optional<Integer> delieryAddressId = Optional.ofNullable(customer.getDeliveryAddressId());
		if (delieryAddressId.isPresent()) {
			DeliveryAddressEntity addressEntity = addressRepository.getById(delieryAddressId.get());
			returnValue.setDeliveryAddress(addressEntity);
		}

		Optional<List<Integer>> reviewsIds = Optional.ofNullable(customer.getReviewsIds());
		List<ReviewEntity> reviews = new ArrayList<ReviewEntity>();
		if (!reviewsIds.isEmpty()) {
			for (Integer reviewId : reviewsIds.get()) {
				ReviewEntity reviewEntity = reviewRepository.getById(reviewId);
				reviews.add(reviewEntity);
			}
		}

		returnValue.setReviews(reviews);

		Optional<List<Integer>> persistenceLoginsIds = Optional.ofNullable(customer.getPersistenceLoginsIds());
		List<PersistenceLoginEntity> persistenceLogins = new ArrayList<PersistenceLoginEntity>();
		if (!persistenceLoginsIds.isEmpty()) {
			for (Integer persistenceId : persistenceLoginsIds.get()) {
				PersistenceLoginEntity persistence = persistenceRepository.getById(persistenceId);
				persistenceLogins.add(persistence);
			}
		}

		returnValue.setPersistenceLogins(persistenceLogins);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		Optional<String> dateOfBirthStrOpt = Optional.ofNullable(customer.getDateOfBirthStr());
		if (dateOfBirthStrOpt.isPresent()) {
			LocalDate dateOfBirthLocal = LocalDate.parse(dateOfBirthStrOpt.get(), formatter);
			Timestamp dateOfBirth = Timestamp.valueOf(dateOfBirthLocal.atStartOfDay());
			returnValue.setDateOfBirth(dateOfBirth);
		}

		return returnValue;
	}

	public LoyaltyCardDto loyaltyCardEntityToDto(LoyaltyCardEntity card) {
		LoyaltyCardDto returnValue = mapper.map(card, LoyaltyCardDto.class);
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(card.getCustomer());
		if (customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}

		return returnValue;
	}

	public LoyaltyCardEntity loyaltyCardDtoToEntity(LoyaltyCardDto card) {
		LoyaltyCardEntity returnValue = mapper.map(card, LoyaltyCardEntity.class);
		Optional<Integer> customerId = Optional.ofNullable(card.getCustomerId());
		if (customerId.isPresent()) {
			CustomerEntity customerEntity = customerRepository.getById(customerId.get());
			returnValue.setCustomer(customerEntity);
		}

		return returnValue;
	}

	public OrderDto orderEntityToDto(OrderEntity orderEntity) {
		OrderDto returnValue = mapper.map(orderEntity, OrderDto.class);
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(orderEntity.getCustomer());
		if (customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}

		Optional<OrderAddressEntity> addressEntity = Optional.ofNullable(orderEntity.getAddress());
		if (addressEntity.isPresent()) {
			returnValue.setAddressId(addressEntity.get().getAddressId());
		}

		Optional<List<OrderItemEntity>> orderedItems = Optional.ofNullable(orderEntity.getOrderedItems());
		List<Integer> orderedItemsIds = new ArrayList<Integer>();
		if (!orderedItems.isEmpty()) {
			for (OrderItemEntity item : orderedItems.get()) {
				orderedItemsIds.add(item.getOrderItemId());
			}
		}

		returnValue.setOrderedItemsIds(orderedItemsIds);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Optional<Timestamp> createdAtOpt = Optional.ofNullable(orderEntity.getCreatedAt());
		if (createdAtOpt.isPresent()) {
			ZonedDateTime time = createdAtOpt.get().toLocalDateTime().atZone(ZoneId.of("Europe/Belgrade"));
			String createdAtStr = time.format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		return returnValue;
	}

	public OrderEntity orderDtoToEntity(OrderDto order) {
		OrderEntity returnValue = mapper.map(order, OrderEntity.class);
		Optional<Integer> customerId = Optional.ofNullable(order.getCustomerId());
		if (customerId.isPresent()) {
			CustomerEntity customerEntity = customerRepository.getById(customerId.get());
			returnValue.setCustomer(customerEntity);
		}

		Optional<Integer> addressId = Optional.ofNullable(order.getAddressId());
		if (addressId.isPresent()) {
			OrderAddressEntity addressEntity = orderAddressRepository.getById(addressId.get());
			returnValue.setAddress(addressEntity);
		}

		Optional<List<Integer>> orderedItemsIds = Optional.ofNullable(order.getOrderedItemsIds());
		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();
		if (!orderedItemsIds.isEmpty()) {
			for (Integer itemId : orderedItemsIds.get()) {
				OrderItemEntity item = orderItemRepository.getById(itemId);
				orderedItems.add(item);
			}
		}

		returnValue.setOrderedItems(orderedItems);
		return returnValue;
	}

	public OrderItemDto orderItemEntityToDto(OrderItemEntity itemEntity) {
		OrderItemDto returnValue = mapper.map(itemEntity, OrderItemDto.class);

		Optional<OrderEntity> orderEntity = Optional.ofNullable(itemEntity.getOrder());
		if (orderEntity.isPresent()) {
			returnValue.setOrderId(orderEntity.get().getOrderId());
		}

		return returnValue;
	}

	public OrderItemEntity orderItemDtoToEntity(OrderItemDto itemDto) {
		OrderItemEntity returnValue = mapper.map(itemDto, OrderItemEntity.class);

		Optional<Integer> orderId = Optional.ofNullable(itemDto.getOrderId());
		if (orderId.isPresent()) {
			OrderEntity orderEntity = orderRepository.getById(orderId.get());
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

	public ReviewDto reviewEntityToDto(ReviewEntity reviewEntity) {
		ReviewDto returnValue = mapper.map(reviewEntity, ReviewDto.class);
		Optional<CustomerEntity> author = Optional.ofNullable(reviewEntity.getAuthor());
		if (author.isPresent()) {
			returnValue.setAuthorId(author.get().getCustomerId());
		}

		Optional<BookEntity> bookEntity = Optional.ofNullable(reviewEntity.getBook());
		if (bookEntity.isPresent()) {
			returnValue.setBookId(bookEntity.get().getBookId());
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Optional<Timestamp> createdAtOpt = Optional.ofNullable(reviewEntity.getCreatedAt());
		if (createdAtOpt.isPresent()) {
			ZonedDateTime createdAtZoned = createdAtOpt.get().toLocalDateTime().atZone(ZoneId.of("Europe/Belgrade"));
			String createdAtStr = createdAtZoned.format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		return returnValue;
	}

	public ReviewEntity reviewDtoToEntity(ReviewDto review) {
		ReviewEntity returnValue = mapper.map(review, ReviewEntity.class);
		Optional<Integer> authorId = Optional.ofNullable(review.getAuthorId());
		if (authorId.isPresent()) {
			CustomerEntity author = customerRepository.getById(authorId.get());
			returnValue.setAuthor(author);
		}

		Optional<Integer> bookId = Optional.ofNullable(review.getBookId());
		if (bookId.isPresent()) {
			BookEntity bookEntity = bookRepository.getById(bookId.get());
			returnValue.setBook(bookEntity);
		}

		return returnValue;
	}

	public WishListDto wishListEntityToDto(WishListEntity wishList) {
		WishListDto returnValue = mapper.map(wishList, WishListDto.class);
		Optional<List<BookEntity>> books = Optional.ofNullable(wishList.getBooks());
		List<Integer> booksIds = new ArrayList<Integer>();
		if (!books.isEmpty()) {
			for (BookEntity book : books.get()) {
				booksIds.add(book.getBookId());
			}
		}

		returnValue.setBooksIds(booksIds);

		Optional<CustomerEntity> customerEntity = Optional.ofNullable(wishList.getCustomer());
		if (customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}

		return returnValue;
	}

	public WishListEntity wishListDtoToEntity(WishListDto wishList) {
		WishListEntity returnValue = mapper.map(wishList, WishListEntity.class);
		Optional<List<Integer>> booksIds = Optional.ofNullable(wishList.getBooksIds());
		List<BookEntity> books = new ArrayList<BookEntity>();
		if (!booksIds.isEmpty()) {
			for (Integer bookId : booksIds.get()) {
				BookEntity bookEntity = bookRepository.getById(bookId);
				books.add(bookEntity);
			}
		}

		returnValue.setBooks(books);

		Optional<Integer> customerId = Optional.ofNullable(wishList.getCustomerId());
		if (customerId.isPresent()) {
			CustomerEntity customerEntity = customerRepository.getById(customerId.get());
			returnValue.setCustomer(customerEntity);
		}

		return returnValue;
	}

	public LoyaltyCardRequestDto cardRequestEntityToDto(LoyaltyCardRequestEntity request) {
		LoyaltyCardRequestDto returnValue = mapper.map(request, LoyaltyCardRequestDto.class);
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(request.getCustomer());
		if (customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}
		return returnValue;
	}

	public LoyaltyCardRequestEntity cardRequestDtoToEntity(LoyaltyCardRequestDto request) {
		LoyaltyCardRequestEntity returnValue = mapper.map(request, LoyaltyCardRequestEntity.class);
		Optional<Integer> customerId = Optional.ofNullable(request.getCustomerId());
		if (customerId.isPresent()) {
			CustomerEntity customerEntity = customerRepository.getById(customerId.get());
			returnValue.setCustomer(customerEntity);
		}
		return returnValue;
	}

	public DeliveryAddressDto deliveryAddressEntityToDto(DeliveryAddressEntity address) {
		DeliveryAddressDto returnValue = mapper.map(address, DeliveryAddressDto.class);
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(address.getCustomer());
		if (customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}
		return returnValue;
	}

	public DeliveryAddressEntity deliveryAddressDtoToEntity(DeliveryAddressDto address) {
		DeliveryAddressEntity returnValue = mapper.map(address, DeliveryAddressEntity.class);
		Optional<Integer> customerId = Optional.ofNullable(address.getCustomerId());
		if (customerId.isPresent()) {
			CustomerEntity customerEntity = customerRepository.getById(customerId.get());
			returnValue.setCustomer(customerEntity);
		}
		return returnValue;
	}

	public OrderItemEntity cartItemToOrderItemEntity(CartItemEntity cartItemEntity) {
		OrderItemEntity returnValue = mapper.map(cartItemEntity, OrderItemEntity.class);

		Optional<BookEntity> book = Optional.ofNullable(cartItemEntity.getBook());
		if (book.isPresent()) {
			returnValue.setBookName(book.get().getName());
			returnValue.setBookPrice(book.get().getPrice());
		}

		return returnValue;
	}

	public PersistenceLoginDto persistenceEntityToDto(PersistenceLoginEntity persistence) {
		PersistenceLoginDto returnValue = mapper.map(persistence, PersistenceLoginDto.class);
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(persistence.getCustomer());
		if (customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Optional<Timestamp> createdAtOpt = Optional.ofNullable(persistence.getCreatedAt());
		if (createdAtOpt.isPresent()) {
			ZonedDateTime createdAtZoned = createdAtOpt.get().toLocalDateTime().atZone(ZoneId.of("Europe/Belgrade"));
			String createdAtStr = createdAtZoned.format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		return returnValue;
	}

	public PersistenceLoginEntity persistenceDtoToEntity(PersistenceLoginDto persistence) {
		PersistenceLoginEntity returnValue = mapper.map(persistence, PersistenceLoginEntity.class);
		Optional<Integer> customerId = Optional.ofNullable(persistence.getCustomerId());
		if (customerId.isPresent()) {
			CustomerEntity customerEntity = customerRepository.getById(customerId.get());
			returnValue.setCustomer(customerEntity);
		}

		return returnValue;
	}

	public OrderAddressDto orderAddressEntityToDto(OrderAddressEntity address) {
		OrderAddressDto returnValue = mapper.map(address, OrderAddressDto.class);
		Optional<OrderEntity> orderEntity = Optional.ofNullable(address.getOrder());
		if (orderEntity.isPresent()) {
			returnValue.setOrderId(orderEntity.get().getOrderId());
		}

		return returnValue;
	}

	public OrderAddressEntity orderAddressDtoToEntity(OrderAddressDto address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		Optional<Integer> orderId = Optional.ofNullable(address.getOrderId());
		if (orderId.isPresent()) {
			OrderEntity orderEntity = orderRepository.getById(orderId.get());
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

	public OrderAddressEntity addressToOrderAddress(DeliveryAddressEntity address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		return returnValue;
	}

	public UserDto userEntityToDto(UserEntity userEntity) {
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		returnValue.setEnabled(userEntity.getEnabled());
		Optional<List<RoleEntity>> roles = Optional.ofNullable(userEntity.getRoles());
		List<Integer> rolesIds = new ArrayList<Integer>();

		if (!roles.isEmpty()) {
			for (RoleEntity roleEntity : roles.get()) {
				rolesIds.add(roleEntity.getId());
			}
		}

		returnValue.setRolesIds(rolesIds);

		return returnValue;
	}

	public UserEntity userDtoToEntity(UserDto userDto) {
		UserEntity returnValue = mapper.map(userDto, UserEntity.class);
		List<RoleEntity> roles = new ArrayList<>();
		Optional<List<Integer>> rolesIds = Optional.ofNullable(userDto.getRolesIds());

		if (!rolesIds.isEmpty()) {
			for (Integer roleId : rolesIds.get()) {
				RoleEntity role = roleRepository.getById(roleId);
				roles.add(role);
			}
		}

		returnValue.setRoles(roles);

		return returnValue;
	}

	public RoleDto roleEntityToDto(RoleEntity roleEntity) {
		RoleDto returnValue = mapper.map(roleEntity, RoleDto.class);
		Optional<List<UserEntity>> users = Optional.ofNullable(roleEntity.getUsers());
		List<Integer> userIds = new ArrayList<>();

		if (!users.isEmpty()) {
			for (UserEntity user : users.get()) {
				userIds.add(user.getId());
			}
		}

		returnValue.setUserIds(userIds);
		return returnValue;
	}

	public RoleEntity roleDtoToEntity(RoleDto roleDto) {
		RoleEntity returnValue = mapper.map(roleDto, RoleEntity.class);
		Optional<List<Integer>> usersIds = Optional.ofNullable(roleDto.getUserIds());
		List<UserEntity> users = new ArrayList<>();

		if (!usersIds.isEmpty()) {
			for (Integer userId : usersIds.get()) {
				UserEntity userEntity = userRepository.getById(userId);
				users.add(userEntity);
			}
		}
		returnValue.setUsers(users);
		return returnValue;
	}
}
