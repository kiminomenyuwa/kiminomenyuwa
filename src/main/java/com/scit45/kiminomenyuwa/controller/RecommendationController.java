package com.scit45.kiminomenyuwa.controller;// RecommendationController.java

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.MenuService;
import com.scit45.kiminomenyuwa.service.StoreSearchService;
import com.scit45.kiminomenyuwa.service.recommendation.HybridRecommendationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {

	private final HybridRecommendationService hybridRecommendationService;
	private final StoreSearchService storeService;
	private final MenuService menuService;

	@PostMapping("/nearby-menus")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getRecommendedNearbyMenus(
		@AuthenticationPrincipal AuthenticatedUser user,
		@RequestParam("latitude") double latitude,
		@RequestParam("longitude") double longitude,
		@RequestParam(value = "radius", defaultValue = "1000") int radius,
		@RequestParam(defaultValue = "10", name = "limit") int limit) {

		// 사용자의 위치를 기반으로 주변 가게들을 검색합니다.
		List<StoreResponseDTO> nearbyStores = storeService.getStoresNearby(latitude, longitude, radius);
		log.debug("nearbyStores = {}", nearbyStores);

		// 주변 가게들의 메뉴 리스트를 수집합니다.
		List<MenuDTO> nearbyMenus = nearbyStores.stream()
			.flatMap(store -> menuService.findMenusByStoreId(store.getStoreId()).stream())
			.collect(Collectors.toList());

		log.debug("nearbyMenus = {}", nearbyMenus);
		// 추천 순으로 메뉴를 정렬합니다.
		List<MenuDTO> recommendedMenus = hybridRecommendationService.sortMenusByRecommendation(
			user.getUsername(),
			nearbyMenus,
			limit
		);

		log.debug("recommendedMenus = {}", recommendedMenus);

		// 두 리스트를 포함하는 맵을 생성하여 반환합니다.
		Map<String, Object> response = new HashMap<>();
		response.put("nearbyStores", nearbyStores);
		response.put("recommendedMenus", recommendedMenus);

		return ResponseEntity.ok(response);
	}

	@GetMapping("map")
	public String map() {
		return "recommendView/map";
	}
}
