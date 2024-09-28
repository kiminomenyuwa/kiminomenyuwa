package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;

/**
 * 메뉴 엔티티와 관련된 데이터베이스 접근을 처리하는 리포지토리 인터페이스.
 * JpaRepository를 확장하여 기본적인 CRUD 기능과 사용자 정의 쿼리 메서드를 제공합니다.
 */
@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
	/**
	 * 해당 유저가 아직 평가하지 않은 메뉴 리스트를 가져오는 쿼리.
	 *
	 * @param userId 평가를 조회할 사용자 ID
	 * @return 해당 사용자가 평가하지 않은 메뉴들의 리스트
	 */
	@Query("SELECT m FROM MenuEntity m WHERE m.menuId NOT IN (SELECT r.menu.menuId FROM MiniGameMenuRatingEntity r WHERE r.user.userId = :userId)")
	List<MenuEntity> findUnratedMenusByUserId(@Param("userId")
	String userId);

	List<MenuEntity> findByStore(StoreEntity store);

	// 가게 ID로 메뉴 목록 조회
	List<MenuEntity> findByStore_StoreId(Integer storeId);

	@Query("SELECT m FROM MenuEntity m JOIN StoreEntity s ON m.store.storeId = s.storeId WHERE s.name = :storeName")
	List<MenuEntity> findMenusByStoreName(@Param("storeName") String storeName);

	// 이미 먹은 메뉴들(eatenMenuIds 리스트)을 제외한 나머지 메뉴들을 가져오는 쿼리
	@Query("SELECT m FROM MenuEntity m WHERE m.menuId NOT IN :eatenMenuIds")
	List<MenuEntity> findMenusNotInMenuIds(@Param("eatenMenuIds") List<Long> eatenMenuIds);

	@Query("SELECT m, GROUP_CONCAT(mc.foodCategory.categoryName) FROM MenuEntity m " +
		"LEFT JOIN MenuCategoryMappingEntity mc ON m.menuId = mc.menu.menuId " +
		"WHERE m.menuId NOT IN :eatenMenuIds " +
		"GROUP BY m.menuId")
	List<Object[]> findMenusWithCategoriesNotInMenuIds(@Param("eatenMenuIds") List<Long> eatenMenuIds);

	Optional<MenuEntity> findByNameAndStore_StoreId(String name, Integer storeId);

	// 남은 예산보다 적은 가격의 메뉴들을 가져옴
	List<MenuEntity> findByPriceLessThanEqual(int budget);

	List<MenuEntity> findByEnabledTrue();

	// 특정 가게의 활성화된 메뉴 조회
	List<MenuEntity> findByStore_StoreIdAndEnabledTrue(Integer storeId);
}


