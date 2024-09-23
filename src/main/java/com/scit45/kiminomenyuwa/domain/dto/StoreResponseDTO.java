package com.scit45.kiminomenyuwa.domain.dto;

import lombok.Data;

@Data
public class StoreResponseDTO {
	Integer storeId;
	String merchantId;
	String name;
	String certification;
	String roadNameAddress;
	String detailAddress;
	String zipcode;
	String phoneNumber;
	String category;
	String description;
	Boolean enabled;
	Double longitude;
	Double latitude;
}
