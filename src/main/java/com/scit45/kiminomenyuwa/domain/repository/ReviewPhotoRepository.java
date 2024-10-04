package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.ReviewEntity;
import com.scit45.kiminomenyuwa.domain.entity.ReviewPhotoEntity;

@Repository
public interface ReviewPhotoRepository extends JpaRepository<ReviewPhotoEntity, Integer> {
	List<ReviewPhotoEntity> findByReviewReviewId(Integer reviewId);

	List<ReviewPhotoEntity> findByReview(ReviewEntity review);
}
