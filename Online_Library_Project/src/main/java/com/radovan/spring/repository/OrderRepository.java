package com.radovan.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
	
	@Query(value="select * from orders where customer_id = :customerId",nativeQuery = true)
	List<OrderEntity> findAllByCustomerId(@Param ("customerId") Integer customerId);
	
	@Query(value="select sum(order_price) from orders where customer_id = :customerId",nativeQuery = true)
	Double getOrdersValue(@Param ("customerId") Integer customerId);

}
