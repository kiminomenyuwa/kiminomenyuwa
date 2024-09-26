package com.scit45.kiminomenyuwa.controller;// RecommendationController.java

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.MenuService;
import com.scit45.kiminomenyuwa.service.StoreSearchService;
import com.scit45.kiminomenyuwa.service.recommendation.HybridRecommendationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {

	private final HybridRecommendationService hybridRecommendationService;
	private final StoreSearchService storeService;
	private final MenuService menuService;

	/**
	 * 추천 메뉴를 입력받는 폼을 표시합니다.
	 *
	 * @param model 모델 객체
	 * @return 추천 폼 뷰
	 */
	@GetMapping
	public String showRecommendationForm(Model model) {
		return "recommendView/recommendationForm"; // Thymeleaf 템플릿 이름
	}

	/**
	 * 사용자의 위치 주변 가게들의 메뉴를 추천 순으로 정렬하여 반환하는 메소드
	 *
	 * @param user    추천 대상 유저
	 * @param latitude  사용자 현재 위치의 위도
	 * @param longitude 사용자 현재 위치의 경도
	 * @param limit     추천할 메뉴 개수 (기본값: 10)
	 * @param model     모델 객체
	 * @return 추천 결과 뷰
	 */
	@PostMapping("/nearby-menus")
	public String getRecommendedNearbyMenus(
		@AuthenticationPrincipal AuthenticatedUser user,
		@RequestParam("latitude") double latitude,
		@RequestParam("longitude") double longitude,
		@RequestParam(value = "radius", defaultValue = "999999999") int radius,
		@RequestParam(defaultValue = "10", name = "limit") int limit,
		Model model) {

		// 사용자의 위치를 기반으로 주변 가게들을 검색합니다.
		List<StoreResponseDTO> nearbyStores = storeService.getStoresNearby(latitude, longitude, radius);

		// 주변 가게들의 메뉴 리스트를 수집합니다.
		List<MenuEntity> nearbyMenus = nearbyStores.stream()
			.flatMap(store -> menuService.findByStoreId(store.getStoreId()).stream())
			.collect(Collectors.toList());

		// List<MenuEntity> nearbyMenus = menuService.findByStoreId(1);

		// 추천 순으로 메뉴를 정렬합니다.
		List<MenuEntity> recommendedMenus = hybridRecommendationService.sortMenusByRecommendation(user.getUsername(),
			nearbyMenus,
			limit);

		model.addAttribute("recommendedMenus", recommendedMenus);
		return "recommendView/recommendations"; // Thymeleaf 템플릿 이름
	}
}
