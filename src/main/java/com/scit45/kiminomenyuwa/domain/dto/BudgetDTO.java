package com.scit45.kiminomenyuwa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {
	Integer budgetId;         // 예산 ID
	String userId;            // 사용자 ID
	Integer month;            // 해당 월
	Integer year;             // 해당 연도
	Integer budget;           // 예산 금액
}
