package com.scit45.kiminomenyuwa.domain.dto.receipt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.scit45.kiminomenyuwa.domain.dto.ItemDTO;

import lombok.Data;

@Data
public class ReceiptDTO {
	Integer storeId;
	String receiptType;
	String merchantName; //필수
	String merchantAddress; //필수
	String merchantPhoneNumber; //필수
	LocalDateTime transactionDate;
	List<ItemDTO> items = new ArrayList<>();
	Double totalPrice;
}
