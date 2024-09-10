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

	private Integer menuId;
	private Integer storeId;
	private String name;
	private Integer price;
	private String pictureUrl;
	private Boolean enabled;
	private List<String> categories = new ArrayList<>(); // 카테고리 리스트

	public MenuDTO(Integer menuId, Integer storeId, String name, Integer price, String pictureUrl, Boolean enabled) {
		this.menuId = menuId;
		this.storeId = storeId;
		this.name = name;
		this.price = price;
		this.pictureUrl = pictureUrl;
		this.enabled = enabled;
	}

}
