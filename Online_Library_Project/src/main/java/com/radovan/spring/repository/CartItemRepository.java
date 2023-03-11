package com.radovan.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.CartItemEntity;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {

	@Query(value = "select * from cart_items where cart_id = :cartId", nativeQuery = true) 
	List<CartItemEntity> findAllByCartId(@Param ("cartId") Integer cartId);
	
	@Query(value = "select * from cart_items where book_id = :bookId", nativeQuery = true) 
	List<CartItemEntity> findAllByBookId(@Param ("bookId") Integer bookId);
	
	@Modifying
	@Query(value="delete from cart_items where cart_id = :cartId",nativeQuery=true)
	void removeAllByCartId(@Param ("cartId") Integer cartId);
	
	@Modifying
	@Query(value="delete from cart_items where book_id = :bookId",nativeQuery=true)
	void removeAllByBookId(@Param ("bookId") Integer bookId);
	
	@Modifying
	@Query(value = "delete from cart_items where cart_id = :cartId and id = :itemId",nativeQuery = true)
	void removeCartItem(@Param ("cartId") Integer cartId, @Param ("itemId") Integer itemId);

	@Query(value="select sum(price) from cart_items where cart_id = :cartId",nativeQuery = true)
	Double calculateGrandTotal(@Param ("cartId") Integer cartId);
	
	@Query(value="select sum(pr.price * items.quantity) from cart_items as items inner join products as pr on items.product_id = pr.id where items.cart_id = :cartId",nativeQuery=true)
	Double calculateFullPrice(@Param ("cartId") Integer cartId);
	
	@Query(value = "select * from cart_items where cart_id = :cartId and book_id = :bookId",nativeQuery = true)
	CartItemEntity findByCartIdAndBookId(@Param ("cartId") Integer cartId,@Param ("bookId") Integer bookId);
	
	@Query(value = "select sum(quantity) from cart_items where cart_id=:cartId",nativeQuery = true)
	Integer findBookQuantity(@Param ("cartId") Integer cartId);
}
