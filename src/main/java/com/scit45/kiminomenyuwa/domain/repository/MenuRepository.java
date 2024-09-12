package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
	/**
	    * 해당 유저가 아직 평가하지 않은 메뉴 리스트를 가져오는 쿼리.
	    *
	    * @param userId 평가를 조회할 사용자 ID
	    * @return 해당 사용자가 평가하지 않은 메뉴들의 리스트
	    */
	@Query("SELECT m FROM MenuEntity m WHERE m.menuId NOT IN (SELECT r.menuId FROM MiniGameMenuRatingEntity r WHERE r.userId = :userId)")
	List<MenuEntity> findUnratedMenusByUserId(@Param("userId")
	String userId);

	List<MenuEntity> findByStoreId(Integer storeId);
}