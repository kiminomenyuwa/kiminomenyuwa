package com.scit45.kiminomenyuwa.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO;
import com.scit45.kiminomenyuwa.domain.dto.FoodCategoryDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuCountDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.repository.UserDiningHistoryRepository;
import com.scit45.kiminomenyuwa.utils.AgeUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 메뉴 추천 기능 관련 서비스
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecommandService {
	private final UserDiningHistoryService userDiningHistoryService;
	private final MiniGameService miniGameService;
	private final MenuService menuService;
	private final UserDiningHistoryRepository userDiningHistoryRepository;

	/**
	 * 사용자가 아직 먹지 않은 메뉴 추천 메서드
	 * @param userId 현재 로그인 중인 userId
	 * @return 카테고리 기반 추천된 메뉴 리스트
	 */
	public List<MenuDTO> recommendMenusByUntriedCategory(String userId) {
		// 사용자가 먹지 않은 메뉴 리스트
		List<MenuDTO> notTriedMenuList = userDiningHistoryService.getMenusNotTried(userId);

		// 사용자가 먹은 음식 내역 중 카테고리 TOP 10
		List<CategoryCountDTO> categoryTop10 = userDiningHistoryService.getTopCategoriesByUserId(userId);

		// 추천할 메뉴 리스트 (최종 결과)
		List<MenuDTO> recommendedMenus = new ArrayList<>();

		// 각 카테고리당 추천할 메뉴 수 제한 (각 카테고리에서 최대 3개 추천)
		int maxRecommendationsPerCategory = 3;

		// 추천된 메뉴를 중복해서 추가하지 않도록 Set 사용
		Set<Integer> addedMenuIds = new HashSet<>();

		// 카테고리 TOP 10을 기준으로 아직 먹지 않은 메뉴 추천
		for (CategoryCountDTO category : categoryTop10) {
			String categoryName = category.getCategoryName();  // 카테고리 이름
			int count = 0; // 현재 카테고리에서 추천된 메뉴 수

			// 해당 카테고리에 속한 메뉴 필터링
			for (MenuDTO menu : notTriedMenuList) {
				// 이미 추천된 메뉴는 제외
				if (addedMenuIds.contains(menu.getMenuId())) {
					continue;
				}

				// 메뉴의 categories 필드가 null이 아닌지 확인한 후 contains 호출
				if (menu.getCategories() != null && menu.getCategories().contains(categoryName)) {
					recommendedMenus.add(menu);
					addedMenuIds.add(menu.getMenuId());
					count++;

					// 해당 카테고리에서 최대 추천 개수를 초과하면 루프 종료
					if (count >= maxRecommendationsPerCategory) {
						break;
					}
				}
			}
		}

		return recommendedMenus;
	}

	/**
	 * 미니게임 점수 메뉴 추천 메서드
	 * @param userId 로그인 중인 userId
	 * @return 미니게임 점수 기반 추천된 메뉴(미니게임 점수 기반 내림차순 정렬)
	 */
	public List<MenuDTO> recommendMenusByCategoryScores(String userId) {
		// 1. 사용자 카테고리 점수 불러오기 (내림차순으로 정렬됨)
		List<CategoryCountDTO> categoryScores = miniGameService.getCategoryScoresByUserId(userId);

		// 2. 모든 메뉴 불러오기
		List<MenuDTO> allMenus = menuService.getAllMenus();

		// 3. 카테고리 점수를 빠르게 조회하기 위한 Map 생성
		Map<String, Integer> categoryScoreMap = new HashMap<>();
		for (CategoryCountDTO categoryScore : categoryScores) {
			categoryScoreMap.put(categoryScore.getCategoryName(), categoryScore.getCategoryCount().intValue());
		}

		// 4. 각 메뉴별 점수를 저장할 Map 생성
		Map<MenuDTO, Integer> menuScoreMap = new HashMap<>();

		// 5. 각 메뉴의 카테고리 점수를 합산하여 메뉴 점수 계산
		for (MenuDTO menu : allMenus) {
			int totalScore = 0;

			if (menu.getCategories() != null) {
				for (FoodCategoryDTO category : menu.getCategories()) {
					// 카테고리가 categoryScoreMap에 존재하면 해당 카테고리의 점수를 더함
					if (categoryScoreMap.containsKey(category.getCategoryName())) {
						totalScore += categoryScoreMap.get(category.getCategoryName());
					}
				}
			}

			// 메뉴의 총 점수를 Map에 저장
			menuScoreMap.put(menu, totalScore);
		}
		log.debug("menuScoreMap: {}", menuScoreMap);
		// 6. 점수에 따라 메뉴를 내림차순으로 정렬
		List<MenuDTO> recommendedMenus = new ArrayList<>(menuScoreMap.keySet());
		recommendedMenus.sort((m1, m2) -> menuScoreMap.get(m2) - menuScoreMap.get(m1));

		return recommendedMenus;
	}

	/**
	 * 연령대별 인기 메뉴를 가져오는 메서드.
	 *
	 * @param ageGroup 연령대 (예: "20대")
	 * @return 연령대별 인기 메뉴 리스트
	 */
	public List<MenuDTO> recommendPopularMenusByAgeGroup(String ageGroup) {
		// AgeUtils의 getAgeRange 메서드로 연령대 시작과 끝 값을 가져옴
		int[] ageRange = AgeUtils.getAgeRange(ageGroup);
		int ageStart = ageRange[0];
		int ageEnd = ageRange[1];

		// 7일 전 날짜 계산
		LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

		// 연령대와 7일 전 날짜를 기준으로 인기 메뉴 가져오기
		List<MenuCountDTO> popularMenus = userDiningHistoryRepository.findTopMenusByAgeGroupAndDateRange(
			ageStart, ageEnd, sevenDaysAgo, PageRequest.of(0, 5)); // 상위 5개 메뉴 가져오기

		// MenuCountDTO를 MenuDTO로 변환
		List<MenuDTO> menuDTOList = popularMenus.stream()
			.map(menu -> MenuDTO.builder()
				.menuId(menu.getMenuId())
				.name(menu.getName())
				.price(menu.getPrice())
				.pictureUrl(menu.getPictureUrl())
				.build())
			.collect(Collectors.toList());

		return menuDTOList;
	}

}
