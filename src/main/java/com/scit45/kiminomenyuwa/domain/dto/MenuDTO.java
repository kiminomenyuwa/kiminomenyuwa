package com.scit45.kiminomenyuwa.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 메뉴 데이터를 전송하기 위한 DTO 클래스입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDTO {

	Integer menuId;
	Integer storeId;
	String name;
	Integer price;
	String pictureUrl;
	Boolean enabled;
	List<FoodCategoryDTO> categories = new ArrayList<>(); // 카테고리 리스트
	private DiscountDTO discount; // 할인 정보 추가

	// StoreDTO를 추가하여 메뉴와 관련된 상점 정보를 함께 전달
	private StoreDTO store;

	public MenuDTO(Integer menuId, Integer storeId, String name, Integer price, String pictureUrl, Boolean enabled) {
		this.menuId = menuId;
		this.storeId = storeId;
		this.name = name;
		this.price = price;
		this.pictureUrl = pictureUrl;
		this.enabled = enabled;
	}

	public MenuDTO(Integer menuId, String name) {
		this.menuId = menuId;
		this.name = name;
	}

	public MenuDTO(Integer menuId, String name, Integer price, String pictureUrl) {
		this.menuId = menuId;
		this.name = name;
		this.price = price;
		this.pictureUrl = pictureUrl;
	}

	public MenuDTO(Integer menuId, String name, Integer price) {
		this.menuId = menuId;
		this.name = name;
		this.price = price;
	}

	public List<FoodCategoryDTO> getCategories() {
		// categories 필드가 null일 경우 빈 리스트 반환
		return categories != null ? categories : new ArrayList<>();
	}
}
