package com.radovan.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.WishListEntity;

@Repository
public interface WishListRepository extends JpaRepository<WishListEntity, Integer> {

	@Query(value = "select * from wishlists where customer_id = :customerId",nativeQuery = true)
	WishListEntity findByCustomerId(@Param ("customerId") Integer customerId);
	
	@Query(value = "select book_id from books_wishlists where wishlist_id = :wishlistId",nativeQuery = true)
	List<Integer> findBookIds(@Param ("wishlistId") Integer wishlistId);

	@Modifying
	@Query(value = "delete from books_wishlists where wishlist_id = :wishListId",nativeQuery = true)
	void clearWishList(@Param ("wishListId") Integer wishListId);
}
