package com.scit45.kiminomenyuwa.domain.dto.store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewResponseDTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StoreInfoDTO {
	Integer storeId;
	String name;
	String roadNameAddress;
	String detailAddress;
	String zipcode;
	String phoneNumber;
	String category;
	List<String> photoUrls = new ArrayList<>();
	Double latitude;
	Double longitude;
	String certification;
	String description; // 가게 설명
	Boolean favorited; // 찜 여부
	LocalDateTime favoritedTime; // 찜한 시간
	List<MenuDTO> menus = new ArrayList<>(); // 가게 메뉴 리스트
	List<ReviewResponseDTO> reviews = new ArrayList<>(); // 가게 리뷰 리스트
}