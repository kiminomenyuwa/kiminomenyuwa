package com.scit45.kiminomenyuwa.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 음식 카테고리를 정의하는 엔티티 클래스입니다.
 * 예를 들어, '고추', '중식', '굽기' 등의 카테고리 항목을 포함합니다.
 */
@Entity
@Table(name = "food_category")
@NoArgsConstructor
@Data
public class FoodCategoryEntity {

	@Id
	@Column(name = "category_name")
	private String categoryName;

	@Column(name = "type_id", nullable = false)
	private Integer typeId;
}