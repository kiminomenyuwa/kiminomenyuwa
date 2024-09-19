package com.scit45.kiminomenyuwa.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReviewRequestDTO {
	Integer storeId;               // 상점 ID
	Byte rating;                   // 리뷰 평점
	String comment;                // 리뷰 내용
	List<MultipartFile> photos;    // 업로드할 리뷰 사진 파일들
}