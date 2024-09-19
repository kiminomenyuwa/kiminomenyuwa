package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;

@Repository
public interface UserDiningHistoryRepository extends JpaRepository<UserDiningHistoryEntity, Integer> {
	//현재 로그인 중인 userId의 먹은 음식 내역
	List<UserDiningHistoryEntity> findByUserId(String userId);

	//음식 내역중 중복으로 먹은 내역 제거
	@Query("SELECT DISTINCT udh.menuId FROM UserDiningHistoryEntity udh WHERE udh.userId = :userId")
	List<Long> findDistinctMenuIdsByUserId(@Param("userId") String userId);

	//사용자가 먹은 음식 내역의 카테고리 카운트 TOP 10
	@Query(
		"SELECT new com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO(mcm.foodCategory.categoryName, COUNT(mcm.foodCategory.categoryName)) "
			+
			"FROM UserDiningHistoryEntity udh " +
			"JOIN MenuCategoryMappingEntity mcm ON udh.menuId = mcm.menu.menuId " +
			"WHERE udh.userId = :userId " +
			"GROUP BY mcm.foodCategory.categoryName " +
			"ORDER BY COUNT(mcm.foodCategory.categoryName) DESC")
	List<CategoryCountDTO> findTopCategoriesByUserId(@Param("userId") String userId);
	
	List<UserDiningHistoryEntity> findByUser_UserId(String userId);
}