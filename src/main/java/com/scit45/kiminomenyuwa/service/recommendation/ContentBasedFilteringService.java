package com.scit45.kiminomenyuwa.service.recommendation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.entity.MenuCategoryMappingEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuCategoryMappingRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserDiningHistoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class ContentBasedFilteringService {

	private final UserDiningHistoryRepository userDiningHistoryRepository;
	private final MenuRepository menuRepository;
	private final MenuCategoryMappingRepository menuCategoryMappingRepository;

	/**
	 * 콘텐츠 기반 필터링을 통해 추천 메뉴를 생성합니다.
	 *
	 * @param userId 추천 대상 유저 ID
	 * @param limit  추천할 메뉴 개수
	 * @return 추천 메뉴 목록
	 */
	public List<MenuEntity> recommendMenus(String userId, int limit) {
		// 사용자의 선호 카테고리를 분석합니다.
		Set<String> preferredCategories = getUserPreferredCategories(userId);

		// 선호 카테고리를 기반으로 메뉴를 필터링합니다.
		List<MenuEntity> candidateMenus = menuRepository.findByEnabledTrue();

		// 선호 카테고리와 메뉴의 카테고리 간의 유사도를 계산하여 추천 점수를 부여합니다.
		Map<MenuEntity, Integer> menuScores = new HashMap<>();

		for (MenuEntity menu : candidateMenus) {
			List<MenuCategoryMappingEntity> mappings = menuCategoryMappingRepository.findByMenu_MenuId(
				menu.getMenuId());
			int score = 0;
			for (MenuCategoryMappingEntity mapping : mappings) {
				if (preferredCategories.contains(mapping.getFoodCategory().getCategoryName())) {
					score += 1; // 카테고리가 일치할 때 점수 증가
				}
			}
			if (score > 0) {
				menuScores.put(menu, score);
			}
		}

		// 점수 기준으로 메뉴를 정렬하고 상위 N개를 선택합니다.
		return menuScores.entrySet()
			.stream()
			.sorted(Map.Entry.<MenuEntity, Integer>comparingByValue().reversed())
			.limit(limit)
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
	}

	/**
	 * 사용자의 선호 카테고리를 분석합니다.
	 *
	 * @param userId 유저 ID
	 * @return 선호 카테고리 Set
	 */
	private Set<String> getUserPreferredCategories(String userId) {
		List<UserDiningHistoryEntity> diningHistories = userDiningHistoryRepository.findByUser_UserId(userId);

		Map<String, Integer> categoryCount = new HashMap<>();

		for (UserDiningHistoryEntity history : diningHistories) {
			Integer menuId = history.getMenu().getMenuId();
			List<MenuCategoryMappingEntity> mappings = menuCategoryMappingRepository.findByMenu_MenuId(menuId);
			for (MenuCategoryMappingEntity mapping : mappings) {
				String category = mapping.getFoodCategory().getCategoryName();
				categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
			}
		}

		// 선호도가 높은 카테고리를 선택 (예: 상위 3개)
		return categoryCount.entrySet()
			.stream()
			.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
			.limit(3)
			.map(Map.Entry::getKey)
			.collect(Collectors.toSet());
	}
}