// DiscountDTO.java
package com.scit45.kiminomenyuwa.domain.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountDTO {
	private Integer discountId;
	private Integer menuId;
	private BigDecimal originalPrice; // 타입 변경
	private BigDecimal discountedPrice; // 타입 변경
	private Integer discountRate;
}
