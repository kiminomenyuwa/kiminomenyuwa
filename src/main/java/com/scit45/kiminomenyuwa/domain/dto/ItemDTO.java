package com.scit45.kiminomenyuwa.domain.dto;

import lombok.Data;

@Data
public class ItemDTO {
	Double quantity;
	Double price;
	String itemName;
	String description;
	Double totalPrice;
}