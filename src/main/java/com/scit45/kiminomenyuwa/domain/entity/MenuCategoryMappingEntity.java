package com.scit45.kiminomenyuwa.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 메뉴와 카테고리 간의 관계를 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "menu_category_mapping")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class MenuCategoryMappingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mapping_id")
	private Integer mappingId;

	@ManyToOne
	@JoinColumn(name = "menu_id", nullable = false)
	private MenuEntity menu;

	@ManyToOne
	@JoinColumn(name = "category_name", nullable = false)
	private FoodCategoryEntity foodCategory; // food_category 테이블과 매핑
}
