package com.scit45.kiminomenyuwa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class UserSimpleDTO {
	String userId;
	String userName;
	String profilePhotoUrl;
}
