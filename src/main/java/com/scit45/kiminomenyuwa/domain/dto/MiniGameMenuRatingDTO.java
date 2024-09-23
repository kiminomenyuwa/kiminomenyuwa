package com.scit45.kiminomenyuwa.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 미니게임 점수를 저장하기 위한 DTO 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiniGameMenuRatingDTO {

	Integer ratingId;
	String userId;
	Integer menuId;
	Float rating;
	LocalDateTime ratingDate;

}
