package com.scit45.kiminomenyuwa.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.FoodCategoryEntity;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity, String> {
	Optional<FoodCategoryEntity> findByCategoryName(String categoryName);
}
