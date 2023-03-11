package com.radovan.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.ReviewEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

	@Query(value = "select * from reviews where book_id = :bookId and authorized = 1",nativeQuery = true)
	List<ReviewEntity> findAllAuthorizedByBookId(@Param ("bookId") Integer bookId);
	
	@Query(value="select * from reviews where customer_id = :customerId and book_id = :bookId",nativeQuery = true)
	ReviewEntity findByCustomerIdAndBookId(@Param ("customerId") Integer customerId,
			@Param ("bookId") Integer bookId);
	
	@Query(value = "select * from reviews where authorized = 1",nativeQuery = true)
	List<ReviewEntity> findAllAuthorized();
	
	@Query(value = "select * from reviews where authorized = 0",nativeQuery = true)
	List<ReviewEntity> findAllOnHold();

	@Modifying
	@Query(value = "delete from reviews where customer_id = :customerId",nativeQuery = true)
	void deleteAllByCustomerId(@Param ("customerId") Integer customerId);
}
