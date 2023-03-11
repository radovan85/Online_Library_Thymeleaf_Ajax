package com.radovan.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.BookGenreEntity;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenreEntity, Integer> {

	@Query(value = "select * from genres where name = :name", nativeQuery = true)
	BookGenreEntity findByName(@Param("name") String name);
	

}
