package com.scit45.kiminomenyuwa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreRegistrationDTO {
	private Integer storeId;
	private String userId; // UserEntity가 아닌 userId로 설정
	private String name;
	private String certification;
	private String roadNameAddress;
	private String detailAddress;
	private String zipcode;
	private String phoneNumber;
	private String category;
	private String description;
	private Boolean enabled;
}
