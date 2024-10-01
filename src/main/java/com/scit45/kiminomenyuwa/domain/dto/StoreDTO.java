package com.scit45.kiminomenyuwa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDTO {
	private Integer storeId;
	private String name;
	private String roadNameAddress;
	private String detailAddress;
	private Double latitude;
	private Double longitude;
}
