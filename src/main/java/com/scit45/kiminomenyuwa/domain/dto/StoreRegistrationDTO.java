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
	Integer storeId;
	String userId; // UserEntity가 아닌 userId로 설정
	String name;
	String certification;
	String roadNameAddress;
	String detailAddress;
	String zipcode;
	String phoneNumber;
	String category;
	String description;
	Boolean enabled;

	public StoreRegistrationDTO(String name) {
		super();
		this.name = name;
	}

}
