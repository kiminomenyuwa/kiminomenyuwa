package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.ReviewEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
	List<ReviewEntity> findByStore(StoreEntity store);

	// 사용자 ID와 평점으로 리뷰를 필터링하고 정렬하여 조회
	@Query("SELECT r FROM ReviewEntity r WHERE r.user.userId = :userId " +
		"AND (:rating IS NULL OR r.rating = :rating) " +
		"ORDER BY " +
		"CASE WHEN :sortBy = 'rating' THEN r.rating " +
		"     WHEN :sortBy = 'createdTime' THEN r.createdTime " +
		"END DESC")
	Page<ReviewEntity> findByUserIdAndRatingOrderBy(
		@Param("userId") String userId,
		@Param("rating") Integer rating,
		@Param("sortBy") String sortBy,
		Pageable pageable
	);

	// 사용자 ID로 리뷰를 페이지네이션하여 조회
	Page<ReviewEntity> findByUserUserId(String userId, Pageable pageable);

	// 로그인 중인 사용자의 모든 리뷰 내역을 불러오는 메서드
	List<ReviewEntity> findAllByUserUserId(String userId);
}