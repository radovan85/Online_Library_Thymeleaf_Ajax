package com.radovan.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.RoleEntity;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

	RoleEntity findByRole(String roleName);
	
	@Query(value="select user_id from users_roles where roles_id = :roleId",nativeQuery = true)
	List<Integer> findUsersIds(@Param ("roleId") Integer roleId);
}
