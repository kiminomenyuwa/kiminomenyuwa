// DiscountService.java
package com.scit45.kiminomenyuwa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scit45.kiminomenyuwa.domain.dto.DiscountDTO;
import com.scit45.kiminomenyuwa.domain.entity.DiscountEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.repository.DiscountRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DiscountService {

	private final DiscountRepository discountRepository;
	private final MenuRepository menuRepository;

	// 할인 정보 저장
	public void saveDiscount(DiscountDTO discountDTO) {
		MenuEntity menu = menuRepository.findById(discountDTO.getMenuId())
			.orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

		DiscountEntity discount = DiscountEntity.builder()
			.menu(menu)
			.originalPrice(discountDTO.getOriginalPrice())
			.discountedPrice(discountDTO.getDiscountedPrice())
			.discountRate(discountDTO.getDiscountRate())
			.build();

		discountRepository.save(discount);
	}

	// 특정 메뉴의 할인 정보 조회
	public DiscountDTO getDiscountByMenuId(Integer menuId) {
		DiscountEntity discount = discountRepository.findByMenu_MenuId(menuId);
		if (discount != null) {
			return DiscountDTO.builder()
				.discountId(discount.getDiscountId())
				.menuId(discount.getMenu().getMenuId())
				.originalPrice(discount.getOriginalPrice())
				.discountedPrice(discount.getDiscountedPrice())
				.discountRate(discount.getDiscountRate())
				.build();
		} else {
			return null;
		}
	}

	// 할인 정보 삭제
	public void deleteDiscountByMenuId(Integer menuId) {
		discountRepository.deleteByMenu_MenuId(menuId);
	}
}
