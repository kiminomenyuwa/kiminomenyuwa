package com.scit45.kiminomenyuwa.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ReceiptDTO {
	String receiptType;
	String merchantName;
	List<ItemDTO> items = new ArrayList<>();
	Double totalPrice;
	String transactionDate;
}
