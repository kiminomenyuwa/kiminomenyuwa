package com.scit45.kiminomenyuwa.domain.dto;

import lombok.Data;

@Data
public class ItemDTO {
	String description;   // 상품명
	Double quantity;      // 수량
	Double price;         // 단가
	Double totalPrice;    // 총액
}