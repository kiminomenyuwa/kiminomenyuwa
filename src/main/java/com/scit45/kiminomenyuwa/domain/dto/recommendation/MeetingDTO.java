package com.scit45.kiminomenyuwa.domain.dto.recommendation;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDTO {
	String title;
	String description;
	LocalDateTime meetingDate;
	String location;
	List<String> participantUserIds;
	List<String> preferredCategories;
	String keywords;
}