package com.scit45.kiminomenyuwa.domain.dto;

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
}
