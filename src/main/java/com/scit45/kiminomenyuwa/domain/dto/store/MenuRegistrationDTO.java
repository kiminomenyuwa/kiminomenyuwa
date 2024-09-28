package com.scit45.kiminomenyuwa.domain.dto.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 가게 사장 전용 메뉴 등록 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRegistrationDTO {
	Integer menuId;
	Integer storeId;
	String name;
	Integer price;
	MultipartFile photo;
	Boolean enabled;
	List<String> categories = new ArrayList<>(); // 카테고리 리스트

	public List<String> getCategories() {
		// categories 필드가 null일 경우 빈 리스트 반환
		return categories != null ? categories : new ArrayList<>();
	}
}
