// DiscountEntity.java
package com.scit45.kiminomenyuwa.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Discount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer discountId;

	@OneToOne
	@JoinColumn(name = "menu_id")
	private MenuEntity menu;

	@Column(nullable = false)
	private BigDecimal originalPrice; // 타입 변경

	@Column(nullable = false)
	private BigDecimal discountedPrice; // 타입 변경

	@Column(nullable = false)
	private Integer discountRate;
}
