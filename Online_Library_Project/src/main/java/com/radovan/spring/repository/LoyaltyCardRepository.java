package com.radovan.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.LoyaltyCardEntity;

@Repository
public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCardEntity, Integer> {

}
