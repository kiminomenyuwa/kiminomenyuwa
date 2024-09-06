package com.scit45.kiminomenyuwa.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

/**
 * 메뉴와 카테고리 간의 관계를 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "menu_category_mapping")
@NoArgsConstructor
public class MenuCategoryMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Integer mappingId;

    @Column(name = "menu_id", nullable = false)
    private Integer menuId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;
}
