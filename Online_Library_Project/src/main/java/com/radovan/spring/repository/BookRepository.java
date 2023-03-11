package com.radovan.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

	@Query(value = "select * from books where isbn = :isbn",nativeQuery = true)
	BookEntity findByISBN(@Param ("isbn") String isbn);

	@Query(value = "select * from books where genre_id = :genreId",nativeQuery = true)
	List<BookEntity> findAllByGenreId(@Param ("genreId") Integer genreId);

	@Query(value= "select * from books where isbn ilike CONCAT('%', :keyword, '%')"
			+ " or publisher ilike CONCAT('%' ,:keyword, '%')"
			+ " or author ilike CONCAT('%', :keyword, '%')"
			+ " or description ilike CONCAT('%', :keyword, '%')"
			+ " or name ilike CONCAT('%', :keyword, '%')",nativeQuery=true)
	List<BookEntity> findAllByKeyword(@Param ("keyword") String keyword);
	
	@Query(value="select cast(avg(rating) as decimal(4,2)) from reviews where book_id = :bookId and authorized=1",nativeQuery = true)
	Double calculateAverageRating(@Param ("bookId") Integer bookId);
	
	@Query(value="select * from books order by book_id asc",nativeQuery = true)
	List<BookEntity> findAllSortedById();
	
	@Query(value="select * from books order by average_rating desc",nativeQuery = true)
	List<BookEntity> findAllSortedByRating();
	
	@Query(value="select * from books order by price asc",nativeQuery = true)
	List<BookEntity> findAllSortedByPrice();
	
	@Modifying
	@Query(value = "delete from books_wishlists where book_id = :bookId",nativeQuery = true)
	void eraseBookFromAllWishlists(@Param ("bookId") Integer bookId);

}
