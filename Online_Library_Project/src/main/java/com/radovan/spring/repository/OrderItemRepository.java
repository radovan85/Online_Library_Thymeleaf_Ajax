package com.radovan.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.OrderItemEntity;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {

	@Query(value="select sum(price) from order_items where order_id = :orderId",nativeQuery = true)
	Double calculateGrandTotal(@Param ("orderId") Integer orderId);
	
	@Query(value="select * from order_items where order_id = :orderId",nativeQuery=true)
	List<OrderItemEntity> findAllByOrderId(@Param ("orderId") Integer orderId);
	
	@Modifying
	@Query(value="delete from order_items where order_id = :orderId",nativeQuery=true)
	void deleteAllByOrderId(@Param ("orderId") Integer orderId);
	
	@Query(value = "select sum(quantity) from order_items where order_id=:orderId",nativeQuery = true)
	Integer findBookQuantity(@Param ("orderId") Integer orderId);
}
