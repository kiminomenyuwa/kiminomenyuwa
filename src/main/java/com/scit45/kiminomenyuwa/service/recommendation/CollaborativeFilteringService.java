package com.scit45.kiminomenyuwa.service.recommendation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class CollaborativeFilteringService {

	private final UserItemMatrixService userItemMatrixService;
	private final SimilarityService similarityService;
	private final MenuRepository menuRepository;

	/**
	 * 협업 필터링을 통해 추천 메뉴를 생성합니다.
	 *
	 * @param userId 추천 대상 유저 ID
	 * @param limit  추천할 메뉴 개수
	 * @return 추천 메뉴 목록
	 */
	public List<MenuEntity> recommendMenus(String userId, int limit) {
		Map<String, Map<Integer, Integer>> userItemMatrix = userItemMatrixService.createUserItemMatrix();

		if (!userItemMatrix.containsKey(userId)) {
			return Collections.emptyList();
		}

		Map<Integer, Integer> targetUserVector = userItemMatrix.get(userId);

		// 모든 다른 사용자와의 유사도를 계산합니다.
		Map<String, Double> similarityMap = new HashMap<>();
		for (String otherUserId : userItemMatrix.keySet()) {
			if (!otherUserId.equals(userId)) {
				double similarity = similarityService.cosineSimilarity(targetUserVector,
					userItemMatrix.get(otherUserId));
				similarityMap.put(otherUserId, similarity);
			}
		}

		// 유사도 기준으로 상위 N명의 유사한 사용자를 선택합니다.
		int topN = 5;
		List<Map.Entry<String, Double>> sortedSimilarUsers = similarityMap.entrySet().stream()
			.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
			.limit(topN)
			.toList();

		// 유사한 사용자들이 선호하는 메뉴를 수집합니다.
		Map<Integer, Double> recommendedScores = new HashMap<>();

		for (Map.Entry<String, Double> entry : sortedSimilarUsers) {
			String similarUserId = entry.getKey();
			double similarity = entry.getValue();
			Map<Integer, Integer> similarUserVector = userItemMatrix.get(similarUserId);

			for (Map.Entry<Integer, Integer> menuEntry : similarUserVector.entrySet()) {
				Integer menuId = menuEntry.getKey();
				int score = menuEntry.getValue();

				// 이미 대상 사용자가 가진 메뉴는 제외
				if (!targetUserVector.containsKey(menuId)) {
					recommendedScores.put(menuId, recommendedScores.getOrDefault(menuId, 0.0) + similarity * score);
				}
			}
		}

		// 점수 기준으로 메뉴를 정렬하고 상위 N개를 선택합니다.
		List<Integer> recommendedMenuIds = recommendedScores.entrySet().stream()
			.sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
			.limit(limit)
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());

		return menuRepository.findAllById(recommendedMenuIds).stream()
			.filter(MenuEntity::getEnabled)
			.collect(Collectors.toList());
	}

	/**
	 * 사용자 간의 유사도를 계산하여 반환합니다.
	 *
	 * @param userId 기준 유저 ID
	 * @return 다른 사용자들과의 유사도 Map
	 */
	public Map<String, Double> calculateSimilarity(String userId) {
		Map<String, Map<Integer, Integer>> userItemMatrix = userItemMatrixService.createUserItemMatrix();

		if (!userItemMatrix.containsKey(userId)) {
			log.warn("User ID {} not found in user-item matrix.", userId);
			return Collections.emptyMap();
		}

		Map<Integer, Integer> targetUserVector = userItemMatrix.get(userId);

		Map<String, Double> similarityMap = new HashMap<>();
		for (String otherUserId : userItemMatrix.keySet()) {
			if (!otherUserId.equals(userId)) {
				double similarity = similarityService.cosineSimilarity(targetUserVector,
					userItemMatrix.get(otherUserId));
				similarityMap.put(otherUserId, similarity);
			}
		}

		return similarityMap;
	}

	/**
	 * 다중 유저에 대한 협업 필터링을 통해 추천 메뉴를 생성합니다.
	 *
	 * @param userIds 추천 대상 유저 ID 리스트
	 * @param limit   추천할 메뉴 개수
	 * @return 추천 메뉴 목록
	 */
	public List<MenuEntity> recommendMenusForUsers(List<String> userIds, int limit) {
		Map<String, Map<Integer, Integer>> userItemMatrix = userItemMatrixService.createUserItemMatrix();

		// 존재하지 않는 유저는 제외
		List<String> validUserIds = userIds.stream()
			.filter(userItemMatrix::containsKey)
			.collect(Collectors.toList());

		if (validUserIds.isEmpty()) {
			log.warn("No valid user IDs found in user-item matrix.");
			return Collections.emptyList();
		}

		Map<Integer, Double> recommendedScores = new HashMap<>();

		for (String userId : validUserIds) {
			Map<String, Double> similarityMap = calculateSimilarity(userId);

			int topN = 5; // 상위 유사 사용자 수
			List<Map.Entry<String, Double>> sortedSimilarUsers = similarityMap.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.limit(topN)
				.collect(Collectors.toList());

			for (Map.Entry<String, Double> entry : sortedSimilarUsers) {
				String similarUserId = entry.getKey();
				double similarity = entry.getValue();
				Map<Integer, Integer> similarUserVector = userItemMatrix.get(similarUserId);

				for (Map.Entry<Integer, Integer> menuEntry : similarUserVector.entrySet()) {
					Integer menuId = menuEntry.getKey();
					int score = menuEntry.getValue();

					// 대상 사용자가 이미 가진 메뉴는 제외
					if (userItemMatrix.get(userId).containsKey(menuId)) {
						continue;
					}

					// 점수 합산 (가중치 조정 가능)
					double calculatedScore = similarity * score;
					recommendedScores.put(menuId, recommendedScores.getOrDefault(menuId, 0.0) + calculatedScore);
				}
			}
		}

		// 점수 기준으로 메뉴를 정렬하고 상위 N개를 선택합니다.
		List<Integer> recommendedMenuIds = recommendedScores.entrySet().stream()
			.sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
			.limit(limit)
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());

		return menuRepository.findAllById(recommendedMenuIds).stream()
			.filter(MenuEntity::getEnabled)
			.collect(Collectors.toList());
	}
}

