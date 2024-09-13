package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.MiniGameMenuRatingEntity;

/**
 * 미니게임에서 메뉴에 대한 평가 기록을 관리하는 리포지토리 인터페이스.
 * JpaRepository를 확장하여 기본적인 CRUD 기능과 사용자 정의 쿼리 메서드를 제공합니다.
 */
@Repository
public interface MiniGameMenuRatingRepository extends JpaRepository<MiniGameMenuRatingEntity, Integer> {

	/**
	 * 특정 유저가 평가한 메뉴들의 정보를 가져오는 메서드.
	 *
	 * @param userId 평가를 조회할 사용자 ID
	 * @return 해당 사용자가 평가한 메뉴 평가 기록 리스트
	 */
	List<MiniGameMenuRatingEntity> findByUserId(String userId);
}
