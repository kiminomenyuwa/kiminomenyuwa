package com.scit45.kiminomenyuwa.domain.dto.store;

import java.util.List;

import lombok.Data;

@Data
public class BaseStoreDTO {
	private Integer storeId;
	private String name;
	private String roadNameAddress;
	private String detailAddress;
	private String zipcode;
	private String phoneNumber;
	private String category;
	private List<String> photoUrls;
	private Double latitude;
	private Double longitude;
}
