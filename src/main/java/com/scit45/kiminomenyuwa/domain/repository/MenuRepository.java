package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;

/**
 * 메뉴 엔티티와 관련된 데이터베이스 접근을 처리하는 리포지토리 인터페이스.
 * JpaRepository를 확장하여 기본적인 CRUD 기능과 사용자 정의 쿼리 메서드를 제공합니다.
 */
@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
	// 가게 ID로 메뉴 목록 조회
	List<MenuEntity> findByStoreId(Integer storeId);
}
