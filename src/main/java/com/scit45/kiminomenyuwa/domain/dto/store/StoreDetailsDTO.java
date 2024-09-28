package com.scit45.kiminomenyuwa.domain.dto.store;

import java.time.LocalDateTime;
import java.util.List;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewResponseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreDetailsDTO extends BaseStoreDTO {
	String certification;
	Boolean enabled;
	String description;
	LocalDateTime createdTime;
	LocalDateTime updatedTime;
	List<MenuDTO> menus;       // 메뉴 목록
	List<ReviewResponseDTO> reviews;   // 리뷰 목록 (추가 시)
	// 추가적인 사장님용 필드 (예: 매출 통계 등)
}