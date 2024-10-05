package com.scit45.kiminomenyuwa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCountDTO {
	Integer menuId;
	Integer storeId;
	Long menuCount;
	String name;
	String pictureUrl;
	Integer price;
}