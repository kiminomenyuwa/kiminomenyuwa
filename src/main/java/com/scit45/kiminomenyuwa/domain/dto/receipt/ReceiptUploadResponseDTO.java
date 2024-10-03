package com.scit45.kiminomenyuwa.domain.dto.receipt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 영수증 인증 성공시 인증내역 반환하는 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptUploadResponseDTO {
	boolean success;
	String message;
	ReceiptDTO receipt;
}