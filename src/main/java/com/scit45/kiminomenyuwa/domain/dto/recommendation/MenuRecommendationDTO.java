package com.scit45.kiminomenyuwa.domain.dto.recommendation;

import java.util.ArrayList;
import java.util.List;

import com.scit45.kiminomenyuwa.domain.dto.FoodCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 메뉴 추천 데이터를 전송하기 위한 DTO 클래스입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRecommendationDTO {
	Integer menuId;
	Integer storeId;
	String name;
	Integer price;
	String pictureUrl;
	Boolean enabled;
	List<FoodCategoryDTO> categories = new ArrayList<>(); // 카테고리 리스트
	List<String> recommendationReasons = new ArrayList<>(); // 추천 이유 필드 추가
}
