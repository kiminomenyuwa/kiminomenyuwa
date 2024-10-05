package com.scit45.kiminomenyuwa.service.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.recommendation.MenuRecommendationDTO;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.service.MenuService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class HybridRecommendationService {

	private final CollaborativeFilteringService collaborativeFilteringService;
	private final ContentBasedFilteringService contentBasedFilteringService;
	private final MenuRepository menuRepository;
	private final UserItemMatrixService userItemMatrixService;
	private final MenuService menuService;


	/**
	 * 입력된 메뉴 리스트를 하이브리드 추천 점수에 따라 정렬하고, 추천 이유를 추가한 DTO 리스트를 반환합니다.
	 *
	 * @param userId      추천 대상 유저 ID
	 * @param nearbyMenus 사용자의 위치 주변 가게들의 메뉴 리스트
	 * @param limit       추천할 메뉴 개수
	 * @return 추천 순으로 정렬된 메뉴 목록 (추천 이유 포함)
	 */
	public List<MenuRecommendationDTO> sortMenusByRecommendation(String userId, List<MenuDTO> nearbyMenus, int limit) {
		// 사용자 아이템 매트릭스 생성
		Map<String, Map<Integer, Integer>> userItemMatrix = userItemMatrixService.createUserItemMatrix();

		if (!userItemMatrix.containsKey(userId)) {
			log.warn("User ID {} not found in user-item matrix.", userId);
			return Collections.emptyList();
		}

		Map<Integer, Double> menuScoreMap = new HashMap<>();
		Map<Integer, List<String>> menuReasonsMap = new HashMap<>(); // 추천 이유를 저장할 맵

		// 가중치 설정 (필요에 따라 조정 가능)
		double cfWeight = 1.0;  // Collaborative Filtering 가중치
		double cbfWeight = 1.0; // Content-Based Filtering 가중치

		// 협업 필터링 점수 계산
		Map<String, Double> similarityMap = collaborativeFilteringService.calculateSimilarity(userId);

		int topN = 5; // 상위 유사 사용자 수
		List<Map.Entry<String, Double>> sortedSimilarUsers = similarityMap.entrySet().stream()
			.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
			.limit(topN)
			.collect(Collectors.toList());

		for (Map.Entry<String, Double> entry : sortedSimilarUsers) {
			String similarUserId = entry.getKey();
			double similarity = entry.getValue();
			Map<Integer, Integer> similarUserVector = userItemMatrix.get(similarUserId);

			for (MenuDTO menu : nearbyMenus) {
				Integer menuId = menu.getMenuId();
				int score = similarUserVector.getOrDefault(menuId, 0);

				// 대상 사용자가 이미 가진 메뉴는 제외
				if (userItemMatrix.get(userId).containsKey(menuId)) {
					continue;
				}

				if (score > 0) {
					double calculatedScore = cfWeight * similarity * score;
					menuScoreMap.put(menuId, menuScoreMap.getOrDefault(menuId, 0.0) + calculatedScore);

					// 추천 이유 추가
					menuReasonsMap.computeIfAbsent(menuId, k -> new ArrayList<>()).add("유사한 사용자들이 선호하는 음식");
				}
			}
		}

		// 콘텐츠 기반 필터링 점수 계산
		Set<String> preferredCategories = contentBasedFilteringService.getUserPreferredCategories(userId);

		for (MenuDTO menu : nearbyMenus) {
			Integer menuId = menu.getMenuId();
			List<String> menuCategories = contentBasedFilteringService.getMenuCategories(menuId);

			int matchingCategories = 0;
			for (String category : menuCategories) {
				if (preferredCategories.contains(category)) {
					matchingCategories++;
				}
			}

			if (matchingCategories > 0) {
				double calculatedScore = cbfWeight * matchingCategories;
				menuScoreMap.put(menuId, menuScoreMap.getOrDefault(menuId, 0.0) + calculatedScore);

				// 추천 이유 추가
				menuReasonsMap.computeIfAbsent(menuId, k -> new ArrayList<>()).add("사용자가 선호하는 카테고리");
			}
		}

		// 점수 기준으로 메뉴를 정렬하고 상위 N개를 선택합니다.
		List<MenuRecommendationDTO> sortedMenus = menuScoreMap.entrySet().stream()
			.sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
			.limit(limit)
			.map(entry -> {
				MenuDTO menuDTO = menuService.findById(entry.getKey());
				if (menuDTO == null || !menuDTO.getEnabled()) {
					return null;
				}

				List<String> reasons = menuReasonsMap.getOrDefault(entry.getKey(), new ArrayList<>());

				return MenuRecommendationDTO.builder()
					.menuId(menuDTO.getMenuId())
					.storeId(menuDTO.getStoreId())
					.name(menuDTO.getName())
					.price(menuDTO.getPrice())
					.pictureUrl(menuDTO.getPictureUrl())
					.enabled(menuDTO.getEnabled())
					.categories(menuDTO.getCategories())
					.recommendationReasons(reasons)
					.build();
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		log.info("Sorted recommendations for user {}: {}", userId, sortedMenus);

		return sortedMenus;
	}

	/**
	 * 다중 유저의 추천 점수를 종합하여 메뉴를 정렬하고, 추천 이유를 추가한 DTO 리스트를 반환합니다.
	 *
	 * @param userIds            추천 대상 유저 ID 리스트
	 * @param nearbyMenus        사용자의 위치 주변 가게들의 메뉴 리스트
	 * @param limit              추천할 메뉴 개수
	 * @param preferredCategories 사용자가 선택한 선호 카테고리 리스트
	 * @param keywords           사용자가 입력한 키워드
	 * @return 추천 순으로 정렬된 메뉴 목록 (추천 이유 포함)
	 */
	public List<MenuRecommendationDTO> sortMenusByRecommendation(List<String> userIds, List<MenuDTO> nearbyMenus,
		int limit, List<String> preferredCategories, String keywords) {
		// 사용자 아이템 매트릭스 생성
		Map<String, Map<Integer, Integer>> userItemMatrix = userItemMatrixService.createUserItemMatrix();

		// 유효한 유저 ID 필터링
		List<String> validUserIds = userIds.stream()
			.filter(userItemMatrix::containsKey)
			.collect(Collectors.toList());

		if (validUserIds.isEmpty()) {
			log.warn("No valid user IDs found in user-item matrix.");
			return Collections.emptyList();
		}

		Map<Integer, Double> menuScoreMap = new HashMap<>();
		Map<Integer, List<String>> menuReasonsMap = new HashMap<>(); // 추천 이유를 저장할 맵

		// 가중치 설정 (필요에 따라 조정 가능)
		double cfWeight = 1.0;  // Collaborative Filtering 가중치
		double cbfWeight = 1.0; // Content-Based Filtering 가중치
		double userPrefWeight = 1.5; // 사용자 선호도 가중치
		double keywordWeight = 2.0; // 키워드 가중치

		// 1. 협업 필터링 점수 계산 (다중 유저)
		List<MenuEntity> cfRecommendedMenus = collaborativeFilteringService.recommendMenusForUsers(validUserIds, limit);
		for (MenuEntity menu : cfRecommendedMenus) {
			Integer menuId = menu.getMenuId();
			// 협업 필터링 점수 추가
			double cfScore = 1.0; // 필요 시 구체적인 점수 로직 적용
			menuScoreMap.put(menuId, menuScoreMap.getOrDefault(menuId, 0.0) + cfWeight * cfScore);

			// 추천 이유 추가
			menuReasonsMap.computeIfAbsent(menuId, k -> new ArrayList<>()).add("유사한 사용자들이 선호하는 음식");
		}

		// 2. 콘텐츠 기반 필터링 점수 계산 (다중 유저의 선호 카테고리 종합)
		Set<String> combinedPreferredCategories = contentBasedFilteringService.getCombinedUserPreferredCategories(
			validUserIds);
		if (preferredCategories != null && !preferredCategories.isEmpty()) {
			combinedPreferredCategories.addAll(preferredCategories); // 사용자 지정 선호 카테고리 추가
		}

		for (MenuDTO menu : nearbyMenus) {
			Integer menuId = menu.getMenuId();
			List<String> menuCategories = contentBasedFilteringService.getMenuCategories(menuId);

			int matchingCategories = 0;
			for (String category : menuCategories) {
				if (combinedPreferredCategories.contains(category)) {
					matchingCategories++;
				}
			}

			if (matchingCategories > 0) {
				double calculatedScore = cbfWeight * matchingCategories;
				menuScoreMap.put(menuId, menuScoreMap.getOrDefault(menuId, 0.0) + calculatedScore);

				// 추천 이유 추가
				menuReasonsMap.computeIfAbsent(menuId, k -> new ArrayList<>()).add("사용자들이 선호하는 카테고리");
			}
		}

		// 3. 키워드 기반 점수 계산
		if (keywords != null && !keywords.isEmpty()) {
			String[] keywordArray = keywords.split(",");
			for (MenuDTO menu : nearbyMenus) {
				Integer menuId = menu.getMenuId();
				String menuName = menu.getName().toLowerCase();
				boolean keywordMatched = false;
				for (String keyword : keywordArray) {
					if (menuName.contains(keyword.trim().toLowerCase())) {
						keywordMatched = true;
						break;
					}
				}

				if (keywordMatched) {
					double calculatedScore = keywordWeight; // 필요 시 구체적인 점수 로직 적용
					menuScoreMap.put(menuId, menuScoreMap.getOrDefault(menuId, 0.0) + calculatedScore);

					// 추천 이유 추가
					menuReasonsMap.computeIfAbsent(menuId, k -> new ArrayList<>()).add("사용자가 입력한 키워드와 일치");
				}
			}
		}

		// 4. 사용자 선호도 기반 점수 조정
		if (preferredCategories != null && !preferredCategories.isEmpty()) {
			for (MenuDTO menu : nearbyMenus) {
				Integer menuId = menu.getMenuId();
				List<String> menuCategories = contentBasedFilteringService.getMenuCategories(menuId);

				int matchingUserPrefs = 0;
				for (String category : menuCategories) {
					if (preferredCategories.contains(category)) {
						matchingUserPrefs++;
					}
				}

				if (matchingUserPrefs > 0) {
					double calculatedScore = userPrefWeight * matchingUserPrefs;
					menuScoreMap.put(menuId, menuScoreMap.getOrDefault(menuId, 0.0) + calculatedScore);

					// 추천 이유 추가
					menuReasonsMap.computeIfAbsent(menuId, k -> new ArrayList<>()).add("선호하는 카테고리에 속함");
				}
			}
		}

		// 점수 기준으로 메뉴를 정렬하고 상위 N개를 선택합니다.
		List<MenuRecommendationDTO> sortedMenus = menuScoreMap.entrySet().stream()
			.sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
			.limit(limit)
			.map(entry -> {
				MenuDTO menuDTO = menuService.findById(entry.getKey());
				if (menuDTO == null || !menuDTO.getEnabled()) {
					return null;
				}

				List<String> reasons = menuReasonsMap.getOrDefault(entry.getKey(), new ArrayList<>());

				return MenuRecommendationDTO.builder()
					.menuId(menuDTO.getMenuId())
					.storeId(menuDTO.getStoreId())
					.name(menuDTO.getName())
					.price(menuDTO.getPrice())
					.pictureUrl(menuDTO.getPictureUrl())
					.enabled(menuDTO.getEnabled())
					.categories(menuDTO.getCategories())
					.recommendationReasons(reasons)
					.build();
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		log.info("Sorted recommendations for users {}: {}", userIds, sortedMenus);

		return sortedMenus;
	}
}
