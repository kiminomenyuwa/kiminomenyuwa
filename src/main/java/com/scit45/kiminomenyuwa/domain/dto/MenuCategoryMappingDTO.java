package com.scit45.kiminomenyuwa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 메뉴와 카테고리 간의 관계 데이터를 전송하기 위한 DTO 클래스입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCategoryMappingDTO {

    private Integer mappingId;
    private Integer menuId;
    private String categoryName;
}
