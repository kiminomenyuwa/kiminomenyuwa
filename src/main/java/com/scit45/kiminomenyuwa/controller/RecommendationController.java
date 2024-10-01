package com.scit45.kiminomenyuwa.controller;// RecommendationController.java

import java.util.ArrayList;
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
import com.scit45.kiminomenyuwa.domain.dto.recommendation.MenuRecommendationDTO;
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
		@RequestParam(value = "radius", defaultValue = "400") int radius,
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
		List<MenuRecommendationDTO> recommendedMenus = hybridRecommendationService.sortMenusByRecommendation(
			user.getUsername(),
			nearbyMenus,
			limit
		);

		log.debug("recommendedMenus = {}", recommendedMenus);

		// 두 리스트를 포함하는 맵을 생성하여 반환합니다.
		Map<String, Object> response = new HashMap<>();
		response.put("nearbyStores", nearbyStores.stream().limit(10).collect(Collectors.toList()));
		response.put("recommendedMenus", recommendedMenus);

		return ResponseEntity.ok(response);
	}

	@GetMapping("map")
	public String map() {
		return "recommendView/map";
	}

	/**
	 * 다중 유저의 추천 메뉴를 반환하는 엔드포인트
	 *
	 * @param user 현재 로그인한 유저 (호스트)
	 * @param latitude 사용자 위치의 위도
	 * @param longitude 사용자 위치의 경도
	 * @param radius 검색 반경 (기본값: 400)
	 * @param limit 추천할 메뉴의 개수 (기본값: 10)
	 * @param participantUserIds 약속에 참여하는 다른 유저들의 ID 리스트
	 * @param preferredCategories 사용자가 선택한 선호 카테고리 리스트
	 * @param keywords 사용자가 입력한 키워드
	 * @return 추천된 메뉴와 주변 가게 목록
	 */
	@PostMapping("/nearby-menus-with-friends")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getRecommendedNearbyMenus(
		@AuthenticationPrincipal AuthenticatedUser user,
		@RequestParam("latitude") double latitude,
		@RequestParam("longitude") double longitude,
		@RequestParam(value = "radius", defaultValue = "400") int radius,
		@RequestParam(defaultValue = "10", name = "limit") int limit,
		@RequestParam("participantUserIds") List<String> participantUserIds,
		@RequestParam(value = "preferredCategories", required = false) List<String> preferredCategories,
		@RequestParam(value = "keywords", required = false) String keywords) { // 추가된 파라미터

		// 현재 유저와 참여 유저 목록을 합칩니다.
		List<String> allUserIds = new ArrayList<>();
		allUserIds.add(user.getUsername());
		allUserIds.addAll(participantUserIds);

		// 중복 제거
		allUserIds = allUserIds.stream().distinct().collect(Collectors.toList());

		// 사용자의 위치를 기반으로 주변 가게들을 검색합니다.
		List<StoreResponseDTO> nearbyStores = storeService.getStoresNearby(latitude, longitude, radius);
		log.debug("nearbyStores = {}", nearbyStores);

		// 주변 가게들의 메뉴 리스트를 수집합니다.
		List<MenuDTO> nearbyMenus = nearbyStores.stream()
			.flatMap(store -> menuService.findMenusByStoreId(store.getStoreId()).stream())
			.collect(Collectors.toList());

		log.debug("nearbyMenus = {}", nearbyMenus);

		// 다중 유저의 추천 메뉴를 종합하여 정렬합니다.
		List<MenuRecommendationDTO> recommendedMenus = hybridRecommendationService.sortMenusByRecommendation(
			allUserIds,
			nearbyMenus,
			limit,
			preferredCategories,
			keywords
		);

		log.debug("recommendedMenus = {}", recommendedMenus);

		// 두 리스트를 포함하는 맵을 생성하여 반환합니다.
		Map<String, Object> response = new HashMap<>();
		response.put("nearbyStores", nearbyStores.stream().limit(10).collect(Collectors.toList()));
		response.put("recommendedMenus", recommendedMenus);

		return ResponseEntity.ok(response);
	}
}
