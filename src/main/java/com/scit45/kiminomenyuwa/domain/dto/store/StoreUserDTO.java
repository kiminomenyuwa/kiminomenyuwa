package com.scit45.kiminomenyuwa.domain.dto.store;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreUserDTO extends BaseStoreDTO {
	private String description;
	private Boolean favorited;
	private LocalDateTime favoritedTime;
}