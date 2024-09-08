package com.scit45.kiminomenyuwa.domain.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 상점에서 제공하는 메뉴 항목을 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "menu")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class MenuEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Integer menuId;

	@Column(name = "store_id", nullable = false)
	private Integer storeId;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private Integer price;

	@Column(name = "picture_url")
	private String pictureUrl;

	@Column(name = "enabled", nullable = false)
	private Boolean enabled;

	// 메뉴와 카테고리 간의 관계를 나타내는 매핑
	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
	private List<MenuCategoryMappingEntity> categoryMappings;
}