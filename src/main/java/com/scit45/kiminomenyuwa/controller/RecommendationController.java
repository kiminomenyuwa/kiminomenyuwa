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

import com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.domain.dto.recommendation.MenuRecommendationDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.MenuService;
import com.scit45.kiminomenyuwa.service.MiniGameService;
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
	private final MiniGameService minigameService;

	@PostMapping("/nearby-menus")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getRecommendedNearbyMenus(
		@AuthenticationPrincipal AuthenticatedUser user, @RequestParam("latitude") double latitude,
		@RequestParam("longitude") double longitude, @RequestParam(value = "radius", defaultValue = "400") int radius,
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
			user.getUsername(), nearbyMenus, limit);

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
	 * 미니게임 기반 추천 목록을 반환하는 API
	 * 위치 정보를 기반으로 추천 목록을 반환합니다.
	 */
	@ResponseBody
	@GetMapping("/api/minigame/recommendations")
	public ResponseEntity<Map<String, Object>> getMiniGameRecommendations(
		@RequestParam double latitude,
		@RequestParam double longitude,
		@AuthenticationPrincipal AuthenticatedUser user) {
		String userId = user.getId();

		// 1. 미니게임 카테고리 점수 가져오기
		List<CategoryCountDTO> minigameCategoryScoreList = minigameService.getCategoryScoresByUserId(userId);
		log.debug("minigameCategoryScoreList: {}", minigameCategoryScoreList);

		// 2. 메뉴 점수 맵 가져오기
		Map<MenuDTO, Integer> menuScoreMap = minigameService.getMenuScoreMap(userId);
		log.debug("menuScoreMap: {}", menuScoreMap);

		// 3. 메뉴 점수를 내림차순으로 정렬된 리스트로 변환
		List<Map<String, Object>> sortedMenuList = menuScoreMap.entrySet()
			.stream()
			.sorted(Map.Entry.<MenuDTO, Integer>comparingByValue().reversed())
			.map(entry -> {
				Map<String, Object> map = new HashMap<>();
				map.put("menu", entry.getKey());
				map.put("score", entry.getValue());
				return map;
			})
			.collect(Collectors.toList());

		// 4. 추천 메뉴 가져오기 (상위 10개)
		List<MenuDTO> recommendedMenus = sortedMenuList.stream()
			.limit(10)
			.map(entry -> (MenuDTO) entry.get("menu"))
			.collect(Collectors.toList());

		// 5. 응답 데이터 구성
		Map<String, Object> response = new HashMap<>();
		response.put("minigameCategoryScoreList", minigameCategoryScoreList);
		response.put("sortedMenuList", sortedMenuList);
		response.put("recommendedMenus", recommendedMenus);

		return ResponseEntity.ok(response);
	}

}
