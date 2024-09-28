package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.FoodCategoryEntity;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity, String> {
	Optional<FoodCategoryEntity> findByCategoryName(String categoryName);

	Optional<FoodCategoryEntity> findByCategoryId(Integer categoryId);

	// 카테고리 이름에 검색어가 포함된 결과를 반환
	List<FoodCategoryEntity> findByCategoryNameContaining(String query);

	List<FoodCategoryEntity> findByCategoryType_TypeId(Integer typeId);
}
