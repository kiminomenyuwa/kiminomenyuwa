package com.scit45.kiminomenyuwa.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReviewResponseDTO {
	Integer reviewId;            // 리뷰 ID
	Integer storeId;             // 상점 ID
	String userId;               // 사용자 ID
	Byte rating;                 // 리뷰 평점
	String comment;              // 리뷰 내용
	String createdTime;          // 작성 시간
	List<String> photoUrls;      // 저장된 사진 URL 리스트
}