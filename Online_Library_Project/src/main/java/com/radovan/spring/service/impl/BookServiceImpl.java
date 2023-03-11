package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.WishListDto;
import com.radovan.spring.entity.BookEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.entity.WishListEntity;
import com.radovan.spring.repository.BookRepository;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.WishListRepository;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.utils.RandomStringUtil;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private RandomStringUtil randomStringUtil;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private WishListRepository wishListRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartItemService cartItemService;

	@Override
	public BookDto addBook(BookDto book) {
		// TODO Auto-generated method stub
		Optional<Integer> bookId = Optional.ofNullable(book.getBookId());
		Optional<String> isbn = Optional.ofNullable(book.getISBN());
		if (!isbn.isPresent()) {
			book.setISBN(randomStringUtil.getAlphaNumericString(13).toUpperCase());
		}
		
		BookEntity bookEntity = tempConverter.bookDtoToEntity(book);
		BookEntity storedBook = bookRepository.save(bookEntity);
		BookDto returnValue = tempConverter.bookEntityToDto(storedBook);
		
		if(bookId.isPresent()) {
			Optional<List<CartItemEntity>> allCartItems = 
					Optional.ofNullable(cartItemRepository.findAllByBookId(returnValue.getBookId()));
			if(!allCartItems.isEmpty()) {
				for(CartItemEntity itemEntity:allCartItems.get()) {
					Double itemPrice = returnValue.getPrice() * itemEntity.getQuantity();
					if(cartItemService.hasDiscount(itemEntity.getCartItemId())) {
						itemPrice = itemPrice - ((itemPrice / 100) * 35);	
					}
					
					itemEntity.setPrice(itemPrice);
					cartItemRepository.saveAndFlush(itemEntity);
				}
				
				Optional<List<CartEntity>> allCarts = Optional.ofNullable(cartRepository.findAll());
				if(!allCarts.isEmpty()) {
					for(CartEntity cartEntity:allCarts.get()) {
						cartService.refreshCartState(cartEntity.getCartId());
					}
				}
			}
		}
		return returnValue;
	}

	@Override
	public BookDto getBookById(Integer bookId) {
		// TODO Auto-generated method stub
		BookDto returnValue = null;
		Optional<BookEntity> bookEntity = Optional.ofNullable(bookRepository.getById(bookId));
		if (bookEntity.isPresent()) {
			returnValue = tempConverter.bookEntityToDto(bookEntity.get());
		}
		return returnValue;
	}

	@Override
	public BookDto getBookByISBN(String isbn) {
		// TODO Auto-generated method stub
		BookDto returnValue = null;
		Optional<BookEntity> bookEntity = Optional.ofNullable(bookRepository.findByISBN(isbn));
		if (bookEntity.isPresent()) {
			returnValue = tempConverter.bookEntityToDto(bookEntity.get());
		}
		return returnValue;
	}

	@Override
	public void deleteBook(Integer bookId) {
		// TODO Auto-generated method stub
		bookRepository.deleteById(bookId);
		bookRepository.flush();
	}

	@Override
	public List<BookDto> listAll() {
		// TODO Auto-generated method stub
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooks = Optional.ofNullable(bookRepository.findAll());
		if (!allBooks.isEmpty()) {
			for (BookEntity book : allBooks.get()) {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			}
		}
		return returnValue;
	}

	@Override
	public List<BookDto> listAllByGenreId(Integer genreId) {
		// TODO Auto-generated method stub
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooks = Optional.ofNullable(bookRepository.findAllByGenreId(genreId));
		if (!allBooks.isEmpty()) {
			for (BookEntity book : allBooks.get()) {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			}
		}
		return returnValue;
	}

	@Override
	public List<BookDto> search(String keyword) {
		// TODO Auto-generated method stub
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooks = Optional.ofNullable(bookRepository.findAllByKeyword(keyword));
		if (!allBooks.isEmpty()) {
			for (BookEntity book : allBooks.get()) {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			}
		}
		return returnValue;
	}

	@Override
	public void addToWishList(Integer bookId) {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<CustomerEntity> customerEntity = Optional
				.ofNullable(customerRepository.findByUserId(authUser.getId()));
		if (customerEntity.isPresent()) {
			Integer customerId = customerEntity.get().getCustomerId();
			Optional<WishListEntity> wishListEntity = Optional
					.ofNullable(wishListRepository.findByCustomerId(customerId));
			if (wishListEntity.isPresent()) {
				Optional<BookEntity> bookEntity = Optional.ofNullable(bookRepository.getById(bookId));
				if (bookEntity.isPresent()) {
					WishListEntity wishListValue = wishListEntity.get();
					BookEntity bookValue = bookEntity.get();
					List<BookEntity> booksList = wishListValue.getBooks();
					Optional<List<Integer>> booksIds = Optional
							.ofNullable(wishListRepository.findBookIds(wishListValue.getWishListId()));
					if (!booksIds.isEmpty()) {
						if (!(booksIds.get().contains(bookValue.getBookId()))) {
							booksList.add(bookValue);
							wishListValue.setBooks(booksList);
							wishListRepository.saveAndFlush(wishListValue);
						}

					}

				}
			}
		}
	}

	@Override
	public List<BookDto> listAllFromWishList() {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<CustomerEntity> customerEntity = Optional
				.ofNullable(customerRepository.findByUserId(authUser.getId()));
		if (customerEntity.isPresent()) {
			Optional<WishListEntity> wishListEntity = Optional
					.ofNullable(wishListRepository.findByCustomerId(customerEntity.get().getCustomerId()));
			if (wishListEntity.isPresent()) {
				Optional<List<BookEntity>> allBooks = Optional.ofNullable(wishListEntity.get().getBooks());
				if (!allBooks.isEmpty()) {
					for (BookEntity book : allBooks.get()) {
						BookDto bookDto = tempConverter.bookEntityToDto(book);
						returnValue.add(bookDto);
					}
				}

			}
		}
		return returnValue;
	}

	@Override
	public void removeFromWishList(Integer bookId) {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<CustomerEntity> customerEntity = Optional
				.ofNullable(customerRepository.findByUserId(authUser.getId()));
		if (customerEntity.isPresent()) {
			Optional<WishListEntity> wishListEntity = Optional
					.ofNullable(wishListRepository.findByCustomerId(customerEntity.get().getCustomerId()));
			if (wishListEntity.isPresent()) {
				WishListDto wishList = tempConverter.wishListEntityToDto(wishListEntity.get());
				List<Integer> booksIds = wishList.getBooksIds();
				booksIds.remove(Integer.valueOf(bookId));
				wishList.setBooksIds(booksIds);
				WishListEntity wishListValue = tempConverter.wishListDtoToEntity(wishList);
				wishListRepository.saveAndFlush(wishListValue);
			}
		}
	}

	@Override
	public List<BookDto> listAllByBookId() {
		// TODO Auto-generated method stub
		
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooks = Optional.ofNullable(bookRepository.findAllSortedById());
		if (!allBooks.isEmpty()) {
			for (BookEntity book : allBooks.get()) {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			}
		}
		return returnValue;
	}

	@Override
	public List<BookDto> listAllByRating() {
		// TODO Auto-generated method stub
		
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooks = Optional.ofNullable(bookRepository.findAllSortedByRating());
		if (!allBooks.isEmpty()) {
			for (BookEntity book : allBooks.get()) {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			}
		}
		return returnValue;
	}

	@Override
	public List<BookDto> listAllByPrice() {
		// TODO Auto-generated method stub
		
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooks = Optional.ofNullable(bookRepository.findAllSortedByPrice());
		if (!allBooks.isEmpty()) {
			for (BookEntity book : allBooks.get()) {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			}
		}
		return returnValue;
	}

	@Override
	public void removeBookFromAllWishlist(Integer bookId) {
		// TODO Auto-generated method stub
		bookRepository.eraseBookFromAllWishlists(bookId);
		bookRepository.flush();
	}

	@Override
	public void refreshAvgRating() {
		// TODO Auto-generated method stub
		Optional<List<BookEntity>> allBooks = Optional.ofNullable(bookRepository.findAll());
		if(!allBooks.isEmpty()) {
			for(BookEntity book : allBooks.get()) {
				Optional<Double> avgRatingOpt = 
						Optional.ofNullable(bookRepository.calculateAverageRating(book.getBookId()));
				if(avgRatingOpt.isPresent()) {
					book.setAverageRating(avgRatingOpt.get());
				}else {
					book.setAverageRating(null);
				}
				
				bookRepository.saveAndFlush(book);
			}
		}
	}

}
