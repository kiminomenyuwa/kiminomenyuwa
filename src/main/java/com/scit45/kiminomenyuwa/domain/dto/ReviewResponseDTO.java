package com.scit45.kiminomenyuwa.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewResponseDTO {
	Integer reviewId;            // 리뷰 ID
	Integer storeId;             // 상점 ID
	String userId;               // 사용자 ID
	String userProfileUrl;        // 사용자 프로필 사진
	Byte rating;                 // 리뷰 평점
	String comment;              // 리뷰 내용
	LocalDateTime createdTime;          // 작성 시간
	List<String> photoUrls;      // 저장된 사진 URL 리스트
}