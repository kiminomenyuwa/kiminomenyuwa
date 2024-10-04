package com.scit45.kiminomenyuwa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.entity.MenuCategoryMappingEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.MiniGameMenuRatingEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
import com.scit45.kiminomenyuwa.domain.repository.FoodCategoryRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuCategoryMappingRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.MiniGameMenuRatingRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserDiningHistoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class RecommendationService {

	private final UserDiningHistoryRepository userDiningHistoryRepository;
	private final MenuCategoryMappingRepository menuCategoryMappingRepository;
	private final MenuRepository menuRepository;
	private final MiniGameMenuRatingRepository miniGameMenuRatingRepository;
	private final FoodCategoryRepository foodCategoryRepository;

	/**
	 * 유저의 선호 카테고리를 분석합니다.
	 *
	 * @param userId 유저 ID
	 * @return 선호 카테고리 목록 (우선순위 높은 순)
	 */
	public List<String> analyzeUserPreferences(String userId) {
		List<UserDiningHistoryEntity> diningHistories = userDiningHistoryRepository.findByUser_UserId(userId);
		List<MiniGameMenuRatingEntity> menuRatings = miniGameMenuRatingRepository.findByUser_UserId(userId);

		Map<String, Integer> categoryCount = new HashMap<>();

		// 식사 내역 기반 카테고리 카운트
		for (UserDiningHistoryEntity history : diningHistories) {
			Integer menuId = history.getMenu().getMenuId();
			List<MenuCategoryMappingEntity> mappings = menuCategoryMappingRepository.findByMenu_MenuId(menuId);
			for (MenuCategoryMappingEntity mapping : mappings) {
				String category = mapping.getFoodCategory().getCategoryName();
				categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
			}
		}

		// 별점 기반 가중치 추가
		for (MiniGameMenuRatingEntity rating : menuRatings) {
			Integer menuId = rating.getMenu().getMenuId();
			List<MenuCategoryMappingEntity> mappings = menuCategoryMappingRepository.findByMenu_MenuId(menuId);
			for (MenuCategoryMappingEntity mapping : mappings) {
				String category = mapping.getFoodCategory().getCategoryName();
				// 별점 1~5을 카운트에 가중치로 추가
				categoryCount.put(category, categoryCount.getOrDefault(category, 0) + rating.getRating().intValue());
			}
		}

		// 카테고리를 카운트 기준으로 정렬
		return categoryCount.entrySet().stream()
			.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
	}

	/**
	 * 선호도 분석을 기반으로 메뉴를 추천합니다.
	 *
	 * @param userId 유저 ID
	 * @param limit 추천할 메뉴 개수
	 * @return 추천 메뉴 목록
	 */
	public List<MenuEntity> recommendMenus(String userId, int limit) {
		List<String> preferredCategories = analyzeUserPreferences(userId);
		if (preferredCategories.isEmpty()) {
			// 선호도가 없을 경우 랜덤으로 추천
			return menuRepository.findByEnabledTrue().stream()
				.limit(limit)
				.collect(Collectors.toList());
		}

		// 선호 카테고리 순으로 메뉴를 추천
		Set<Integer> recommendedMenuIds = new HashSet<>();
		List<MenuEntity> recommendedMenus = new ArrayList<>();

		for (String category : preferredCategories) {
			List<MenuCategoryMappingEntity> mappings = menuCategoryMappingRepository.findAll()
				.stream()
				.filter(m -> m.getFoodCategory().getCategoryName().equals(category))
				.toList();

			for (MenuCategoryMappingEntity mapping : mappings) {
				MenuEntity menu = mapping.getMenu();
				if (menu.getEnabled() && !recommendedMenuIds.contains(menu.getMenuId())) {
					recommendedMenus.add(menu);
					recommendedMenuIds.add(menu.getMenuId());
					if (recommendedMenus.size() >= limit) {
						return recommendedMenus;
					}
				}
			}
		}

		// 만약 선호 카테고리로 충분한 추천이 안될 경우, 추가적으로 랜덤 메뉴 추천
		if (recommendedMenus.size() < limit) {
			List<MenuEntity> additionalMenus = menuRepository.findByEnabledTrue().stream()
				.filter(menu -> !recommendedMenuIds.contains(menu.getMenuId()))
				.limit(limit - recommendedMenus.size())
				.toList();
			recommendedMenus.addAll(additionalMenus);
		}

		return recommendedMenus;
	}
}
