package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.ReviewEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
	List<ReviewEntity> findByStore(StoreEntity store);

	List<ReviewEntity> findByStoreStoreId(Integer storeId);
}