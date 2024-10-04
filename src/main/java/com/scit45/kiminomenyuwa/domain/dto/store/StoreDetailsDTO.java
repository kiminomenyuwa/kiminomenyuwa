package com.scit45.kiminomenyuwa.domain.dto.store;

import java.time.LocalDateTime;
import java.util.List;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StoreDetailsDTO {
	Integer storeId;
	String name;
	String roadNameAddress;
	String detailAddress;
	String zipcode;
	String phoneNumber;
	String category;
	List<String> photoUrls;
	Double latitude;
	Double longitude;
	String certification;
	Boolean enabled;
	String description;
	LocalDateTime createdTime;
	LocalDateTime updatedTime;
	List<MenuDTO> menus;       // 메뉴 목록
	List<ReviewResponseDTO> reviews;   // 리뷰 목록 (추가 시)
	// 추가적인 사장님용 필드 (예: 매출 통계 등)
}