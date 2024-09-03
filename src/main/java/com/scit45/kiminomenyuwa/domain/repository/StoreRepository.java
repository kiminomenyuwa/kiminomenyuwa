package com.scit45.kiminomenyuwa.domain.repository;

import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
}