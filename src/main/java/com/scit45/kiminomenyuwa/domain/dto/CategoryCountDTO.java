package com.scit45.kiminomenyuwa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDiningHistoryService에서 사용자가 먹은 음식 내역의
 * 카테고리 카운트 TOP 10를 조작하기 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCountDTO {
	String categoryName;
	Long categoryCount;
}
