package com.scit45.kiminomenyuwa.service.recommendation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;

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

	/**
	 * 하이브리드 필터링을 통해 추천 메뉴를 생성합니다.
	 *
	 * @param userId 추천 대상 유저 ID
	 * @param limit  추천할 메뉴 개수
	 * @return 추천 메뉴 목록
	 */
	public List<MenuEntity> recommendMenus(String userId, int limit) {
		// 기존의 추천 방식 그대로 사용
		List<MenuEntity> cfRecommendations = collaborativeFilteringService.recommendMenus(userId, limit);
		List<MenuEntity> cbfRecommendations = contentBasedFilteringService.recommendMenus(userId, limit);

		// 메뉴 ID를 키로 하고 점수를 값으로 하는 Map을 생성합니다.
		Map<Integer, Double> menuScoreMap = new HashMap<>();

		// 가중치 설정 (필요에 따라 조정 가능)
		double cfWeight = 1.0;  // Collaborative Filtering 가중치
		double cbfWeight = 1.0; // Content-Based Filtering 가중치

		// Collaborative Filtering 추천 메뉴에 점수 부여
		for (int i = 0; i < cfRecommendations.size(); i++) {
			MenuEntity menu = cfRecommendations.get(i);
			// 리스트 상위에 위치할수록 높은 점수를 부여
			double score = cfWeight * (cfRecommendations.size() - i);
			menuScoreMap.put(menu.getMenuId(), menuScoreMap.getOrDefault(menu.getMenuId(), 0.0) + score);
		}

		// Content-Based Filtering 추천 메뉴에 점수 부여
		for (int i = 0; i < cbfRecommendations.size(); i++) {
			MenuEntity menu = cbfRecommendations.get(i);
			// 리스트 상위에 위치할수록 높은 점수를 부여
			double score = cbfWeight * (cbfRecommendations.size() - i);
			menuScoreMap.put(menu.getMenuId(), menuScoreMap.getOrDefault(menu.getMenuId(), 0.0) + score);
		}

		// 점수 기준으로 메뉴를 정렬하고 상위 N개를 선택합니다.
		List<MenuEntity> combinedList = menuScoreMap.entrySet().stream()
			.sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
			.limit(limit)
			.map(entry -> {
				Optional<MenuEntity> menuOpt = menuRepository.findById(entry.getKey());
				return menuOpt.orElse(null);
			})
			.filter(Objects::nonNull)
			.filter(MenuEntity::getEnabled) // 메뉴가 활성화된 경우만 포함
			.collect(Collectors.toList());

		log.info("Hybrid recommendations for user {}: {}", userId, combinedList);

		return combinedList;
	}

	/**
	 * 입력된 메뉴 리스트를 하이브리드 추천 점수에 따라 정렬합니다.
	 *
	 * @param userId      추천 대상 유저 ID
	 * @param nearbyMenus 사용자의 위치 주변 가게들의 메뉴 리스트
	 * @param limit       추천할 메뉴 개수
	 * @return 추천 순으로 정렬된 메뉴 목록
	 */
	public List<MenuEntity> sortMenusByRecommendation(String userId, List<MenuEntity> nearbyMenus, int limit) {
		// 사용자 아이템 매트릭스 생성
		Map<String, Map<Integer, Integer>> userItemMatrix = userItemMatrixService.createUserItemMatrix();

		if (!userItemMatrix.containsKey(userId)) {
			log.warn("User ID {} not found in user-item matrix.", userId);
			return Collections.emptyList();
		}

		Map<Integer, Double> menuScoreMap = new HashMap<>();

		// 가중치 설정 (필요에 따라 조정 가능)
		double cfWeight = 1.0;  // Collaborative Filtering 가중치
		double cbfWeight = 1.0; // Content-Based Filtering 가중치

		// 협업 필터링 점수 계산
		Map<String, Double> similarityMap = collaborativeFilteringService.calculateSimilarity(userId);

		int topN = 5; // 상위 유사 사용자 수
		List<Map.Entry<String, Double>> sortedSimilarUsers = similarityMap.entrySet().stream()
			.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
			.limit(topN)
			.toList();

		for (Map.Entry<String, Double> entry : sortedSimilarUsers) {
			String similarUserId = entry.getKey();
			double similarity = entry.getValue();
			Map<Integer, Integer> similarUserVector = userItemMatrix.get(similarUserId);

			for (MenuEntity menu : nearbyMenus) {
				Integer menuId = menu.getMenuId();
				int score = similarUserVector.getOrDefault(menuId, 0);

				// 대상 사용자가 이미 가진 메뉴는 제외
				if (userItemMatrix.get(userId).containsKey(menuId)) {
					continue;
				}

				if (score > 0) {
					menuScoreMap.put(menuId, menuScoreMap.getOrDefault(menuId, 0.0) + (cfWeight * similarity * score));
				}
			}
		}

		// 콘텐츠 기반 필터링 점수 계산
		Set<String> preferredCategories = contentBasedFilteringService.getUserPreferredCategories(userId);

		for (MenuEntity menu : nearbyMenus) {
			Integer menuId = menu.getMenuId();
			List<String> menuCategories = contentBasedFilteringService.getMenuCategories(menuId);

			int matchingCategories = 0;
			for (String category : menuCategories) {
				if (preferredCategories.contains(category)) {
					matchingCategories++;
				}
			}

			if (matchingCategories > 0) {
				menuScoreMap.put(menuId, menuScoreMap.getOrDefault(menuId, 0.0) + (cbfWeight * matchingCategories));
			}
		}

		// 점수 기준으로 메뉴를 정렬하고 상위 N개를 선택합니다.
		List<MenuEntity> sortedMenus = menuScoreMap.entrySet().stream()
			.sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
			.limit(limit)
			.map(entry -> {
				Optional<MenuEntity> menuOpt = menuRepository.findById(entry.getKey());
				return menuOpt.orElse(null);
			})
			.filter(Objects::nonNull)
			.filter(MenuEntity::getEnabled) // 메뉴가 활성화된 경우만 포함
			.collect(Collectors.toList());

		log.info("Sorted recommendations for user {}: {}", userId, sortedMenus);

		return sortedMenus;
	}
}