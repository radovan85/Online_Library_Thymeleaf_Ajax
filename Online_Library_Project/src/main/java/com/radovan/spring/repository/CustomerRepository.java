package com.radovan.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

	CustomerEntity findByUserId(@Param ("userId") Integer userId);

	@Query(value="select * from customers where cart_id = :cartId",nativeQuery = true)
	CustomerEntity findByCartId(@Param ("cartId") Integer cartId);
}
