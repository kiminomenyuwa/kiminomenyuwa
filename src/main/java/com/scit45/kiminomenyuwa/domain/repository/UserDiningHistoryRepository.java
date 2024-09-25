package com.scit45.kiminomenyuwa.domain.repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;

@Repository
public interface UserDiningHistoryRepository extends JpaRepository<UserDiningHistoryEntity, Integer> {

	//음식 내역중 중복으로 먹은 내역 제거
	@Query("SELECT DISTINCT udh.menu.menuId FROM UserDiningHistoryEntity udh WHERE udh.user.userId = :userId")
	List<Long> findDistinctMenuIdsByUserId(@Param("userId") String userId);

	//사용자가 먹은 음식 내역의 카테고리 카운트 TOP 10
	@Query(
		"SELECT new com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO(mcm.foodCategory.categoryName, COUNT(mcm.foodCategory.categoryName)) "
			+ "FROM UserDiningHistoryEntity udh "
			+ "JOIN MenuCategoryMappingEntity mcm ON udh.menu.menuId = mcm.menu.menuId "
			+ "WHERE udh.user.userId = :userId " + "GROUP BY mcm.foodCategory.categoryName "
			+ "ORDER BY COUNT(mcm.foodCategory.categoryName) DESC")
	List<CategoryCountDTO> findTopCategoriesByUserId(@Param("userId") String userId);

	List<UserDiningHistoryEntity> findByUser_UserId(String userId);

	// 먹은 음식의 날짜
	List<UserDiningHistoryEntity> findByUserAndDiningDateBetween(UserEntity user, LocalDateTime start, LocalDateTime end);
}