package com.scit45.kiminomenyuwa.controller;// RecommendationController.java

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
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

import com.scit45.kiminomenyuwa.domain.dto.BudgetDTO;
import com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDTO;
import com.scit45.kiminomenyuwa.domain.dto.recommendation.MenuRecommendationDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.BudgetRecommendService;
import com.scit45.kiminomenyuwa.service.MenuService;
import com.scit45.kiminomenyuwa.service.MiniGameService;
import com.scit45.kiminomenyuwa.service.MyPageService;
import com.scit45.kiminomenyuwa.service.RecommendService;
import com.scit45.kiminomenyuwa.service.StoreSearchService;
import com.scit45.kiminomenyuwa.service.UserService;
import com.scit45.kiminomenyuwa.service.recommendation.HybridRecommendationService;
import com.scit45.kiminomenyuwa.utils.AgeUtils;

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
	private final MyPageService mypageService;
	private final BudgetRecommendService budgetRecommendService;
	private final RecommendService recommendService;
	private final UserService userService;

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
		List<MenuRecommendationDTO> recommendedByHybrid = hybridRecommendationService.sortMenusByRecommendation(
			user.getUsername(), nearbyMenus, limit);
		log.debug("recommendedByHybrid = {}", recommendedByHybrid);

		// 카테고리 점수 기반 추천된 메뉴 리스트
		List<MenuDTO> recommendedByCategoryScores = recommendService.recommendMenusByCategoryScores(user.getId(),
			nearbyMenus);

		// 사용자의 연령대 기반
		// userId를 통해 UserDTO 조회
		UserDTO userDTO = userService.getUserByUserId(user.getId());
		// UserDTO에서 birthDate를 가져와서 연령대 계산
		String ageGroup = AgeUtils.getAgeGroup(userDTO.getBirthDate());
		List<MenuDTO> recommendedByUserAgeGroup = recommendService.recommendPopularMenusByAgeGroup(ageGroup,
			nearbyMenus);

		// 미니게임 기반 추천 순위
		Map<MenuDTO, Integer> menusWithScore = minigameService.getMenuScoreMap(user.getId(), nearbyMenus);
		// Map을 점수 기준으로 내림차순 정렬하여 List<MenuDTO> 생성
		List<MenuDTO> recommendedByMinigameScores = menusWithScore.entrySet()
			.stream()
			.sorted(Map.Entry.<MenuDTO, Integer>comparingByValue(Comparator.reverseOrder()))
			.map(Map.Entry::getKey)
			.toList();

		// 두 리스트를 포함하는 맵을 생성하여 반환합니다.
		Map<String, Object> response = new HashMap<>();
		response.put("nearbyStores", nearbyStores);
		response.put("recommendedMenus", recommendedByHybrid.subList(0, Math.min(recommendedByHybrid.size(), 9)));
		response.put("recommendedByCategoryScores",
			recommendedByCategoryScores.subList(0, Math.min(recommendedByCategoryScores.size(), 9)));
		response.put("recommendedByUserAgeGroup",
			recommendedByUserAgeGroup.subList(0, Math.min(recommendedByUserAgeGroup.size(), 9)));
		response.put("recommendedByMinigameScores",
			recommendedByMinigameScores.subList(0, Math.min(recommendedByMinigameScores.size(), 9)));

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

	/**
	 * 미니게임 기반 추천 목록을 반환하는 API
	 * 위치 정보를 기반으로 추천 목록을 반환합니다.
	 */
	@ResponseBody
	@GetMapping("/api/minigame/recommendations")
	public ResponseEntity<Map<String, Object>> getMiniGameRecommendations(@RequestParam double latitude,
		@RequestParam double longitude, @RequestParam(defaultValue = "false") boolean budgetConsidered,
		@AuthenticationPrincipal AuthenticatedUser user) {
		String userId = user.getId();

		// 현재 연도와 월을 가져옴
		YearMonth currentYearMonth = YearMonth.now();
		int year = currentYearMonth.getYear();
		int month = currentYearMonth.getMonthValue();

		Map<String, Object> response = new HashMap<>();

		if (budgetConsidered) {
			// 예산을 고려한 추천 메뉴 가져오기
			int remainingBudget = budgetRecommendService.calculateRemainingBudget(userId, year, month);
			if (remainingBudget <= 0) {
				response.put("budgetSet", false);
				response.put("message", "예산이 설정되지 않았거나 남은 예산이 부족합니다. 예산을 먼저 등록해주세요.");
				return ResponseEntity.ok(response);
			}

			// 예산을 고려한 추천 메뉴 가져오기
			List<MenuDTO> budgetRecommendedMenus = budgetRecommendService.getRecommendedMenus(remainingBudget,
				30); // 남은 일수를 30일로 가정

			response.put("budgetSet", true);
			response.put("recommendedMenus", budgetRecommendedMenus);
		} else {
			// 미니게임 카테고리 점수 가져오기
			List<CategoryCountDTO> minigameCategoryScoreList = minigameService.getCategoryScoresByUserId(userId);
			log.debug("minigameCategoryScoreList: {}", minigameCategoryScoreList);

			// 메뉴 점수 맵 가져오기
			Map<MenuDTO, Integer> menuScoreMap = minigameService.getMenuScoreMap(userId);
			log.debug("menuScoreMap: {}", menuScoreMap);

			// 메뉴 점수를 내림차순으로 정렬된 리스트로 변환
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

			// 추천 메뉴 가져오기 (상위 10개)
			List<MenuDTO> recommendedMenus = sortedMenuList.stream()
				.limit(10)
				.map(entry -> (MenuDTO)entry.get("menu"))
				.collect(Collectors.toList());

			// 응답 데이터 구성
			response.put("minigameCategoryScoreList", minigameCategoryScoreList);
			response.put("sortedMenuList", sortedMenuList);
			response.put("recommendedMenus", recommendedMenus);
			response.put("budgetSet", true); // 미니게임 추천은 기본적으로 예산을 고려하지 않으므로 true
		}

		return ResponseEntity.ok(response);
	}

	// 	// 남은 예산 조회
	// 	BudgetDTO budgetDTO = mypageService.getRemainingBudget(userId, year, month);
	// 	int remainingBudget = budgetDTO.getBudget();
	//
	// 	if (budget == null || budget <= 0) {
	// 		Map<String, Object> response = new HashMap<>();
	// 		response.put("budgetSet", false);
	// 		return ResponseEntity.ok(response);
	// 	}
	//
	// 	// 예산이 설정되지 않았거나 0일 경우
	// 	if (remainingBudget <= 0) {
	// 		Map<String, Object> response = new HashMap<>();
	// 		response.put("budgetSet", false);
	// 		return ResponseEntity.ok(response);
	// 	}
	//
	// 	// 예산이 설정되어 있는 경우
	// 	List<MenuDTO> budgetRecommendedMenus = budgetRecommendService.getRecommendedMenus(remainingBudget,
	// 		30); // 남은 일수를 30일로 가정
	//
	// 	// 1. 미니게임 카테고리 점수 가져오기
	// 	List<CategoryCountDTO> minigameCategoryScoreList = minigameService.getCategoryScoresByUserId(userId);
	// 	log.debug("minigameCategoryScoreList: {}", minigameCategoryScoreList);
	//
	// 	// 2. 메뉴 점수 맵 가져오기
	// 	Map<MenuDTO, Integer> menuScoreMap = minigameService.getMenuScoreMap(userId);
	// 	log.debug("menuScoreMap: {}", menuScoreMap);
	//
	// 	// 3. 메뉴 점수를 내림차순으로 정렬된 리스트로 변환
	// 	List<Map<String, Object>> sortedMenuList = menuScoreMap.entrySet()
	// 		.stream()
	// 		.sorted(Map.Entry.<MenuDTO, Integer>comparingByValue().reversed())
	// 		.map(entry -> {
	// 			Map<String, Object> map = new HashMap<>();
	// 			map.put("menu", entry.getKey());
	// 			map.put("score", entry.getValue());
	// 			return map;
	// 		})
	// 		.collect(Collectors.toList());
	//
	// 	// 4. 추천 메뉴 가져오기 (상위 10개)
	// 	List<MenuDTO> recommendedMenus = sortedMenuList.stream()
	// 		.limit(10)
	// 		.map(entry -> (MenuDTO)entry.get("menu"))
	// 		.collect(Collectors.toList());
	//
	// 	// 5. 응답 데이터 구성
	// 	Map<String, Object> response = new HashMap<>();
	// 	response.put("minigameCategoryScoreList", minigameCategoryScoreList);
	// 	response.put("sortedMenuList", sortedMenuList);
	// 	response.put("recommendedMenus", recommendedMenus);
	// 	response.put("budgetSet", true);
	// 	response.put("budgetRecommendedMenus", budgetRecommendedMenus);
	//
	// 	return ResponseEntity.ok(response);
	// }

	/**
	 * 사용자 예산 조회 API
	 */
	@GetMapping("/budget")
	@ResponseBody
	public ResponseEntity<BudgetDTO> getUserBudget(@AuthenticationPrincipal AuthenticatedUser user) {
		String userId = user.getId();
		YearMonth currentYearMonth = YearMonth.now();
		int year = currentYearMonth.getYear();
		int month = currentYearMonth.getMonthValue();

		BudgetDTO budget = mypageService.getRemainingBudget(userId, year, month);

		return ResponseEntity.ok(budget);
	}

}
