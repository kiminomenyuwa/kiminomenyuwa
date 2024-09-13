package com.scit45.kiminomenyuwa.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDiningHistoryDTO {
	Integer diningId;
	String userId;
	Integer menuId;
	LocalDateTime diningDate;
}
