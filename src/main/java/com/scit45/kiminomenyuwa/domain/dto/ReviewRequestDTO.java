package com.scit45.kiminomenyuwa.domain.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ReviewRequestDTO {
	Integer storeId;               // 상점 ID
	Byte rating;                   // 리뷰 평점
	String comment;                // 리뷰 내용
	List<MultipartFile> photos = new ArrayList<>();    // 업로드할 리뷰 사진 파일들
}