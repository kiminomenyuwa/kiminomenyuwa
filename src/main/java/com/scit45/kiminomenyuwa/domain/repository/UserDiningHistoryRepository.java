package com.scit45.kiminomenyuwa.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuCountDTO;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;

@Repository
public interface UserDiningHistoryRepository extends JpaRepository<UserDiningHistoryEntity, Integer> {

	//음식 내역중 중복으로 먹은 내역 제거
	@Query("SELECT DISTINCT udh.menu.menuId FROM UserDiningHistoryEntity udh WHERE udh.user.userId = :userId")
	List<Integer> findDistinctMenuIdsByUserId(@Param("userId") String userId);

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

	/**
	 * 주어진 연령대와 기간에 따라 가장 많이 먹은 메뉴를 조회하는 메서드.
	 *
	 * @param ageStart     연령대 시작 값 (예: 20대의 경우 20)
	 * @param ageEnd       연령대 끝 값 (예: 20대의 경우 29)
	 * @param sevenDaysAgo 7일 전 날짜 및 시간을 나타내는 값
	 * @param pageable     결과 페이징 처리를 위한 Pageable 객체 (예: 상위 5개의 메뉴를 가져오기 위해 사용)
	 * @return MenuCountDTO 리스트로, 각 메뉴의 ID, 먹은 횟수, 이름, 사진 URL이 포함됨
	 */
	@Query("SELECT new com.scit45.kiminomenyuwa.domain.dto.MenuCountDTO(udh.menu.menuId, udh.menu.store.storeId, COUNT(udh.menu.menuId), m.name, m.pictureUrl, m.price) "
		+
		"FROM UserDiningHistoryEntity udh " +
		"JOIN udh.user u " +
		"JOIN udh.menu m " +
		"WHERE (YEAR(CURRENT_DATE) - YEAR(u.birthDate)) BETWEEN :ageStart AND :ageEnd " +
		"AND udh.diningDate >= :sevenDaysAgo " +
		"GROUP BY udh.menu.menuId, m.name, m.pictureUrl, m.price " +
		"ORDER BY COUNT(udh.menu.menuId) DESC")
	List<MenuCountDTO> findTopMenusByAgeGroupAndDateRange(
		@Param("ageStart") int ageStart,
		@Param("ageEnd") int ageEnd,
		@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo,
		Pageable pageable
	);

}