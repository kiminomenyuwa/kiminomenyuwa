package com.scit45.kiminomenyuwa.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.MiniGameMenuRatingDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.MenuService;
import com.scit45.kiminomenyuwa.service.MiniGameService;
import com.scit45.kiminomenyuwa.service.UserDiningHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("recommand")
@RequiredArgsConstructor
public class RecommandController {

	private final MenuService menuService;
	private final UserDiningHistoryService userDiningHistoryService;
	private final MiniGameService miniGameService;

	/**
	 * 추천기능 테스트용 페이지
	 * @return recTest.html
	 */
	@GetMapping("test")
	public String recommandTest(Model model, @AuthenticationPrincipal AuthenticatedUser user) {

		//전체 메뉴 리스트 출력
		List<MenuDTO> menuDTOList = menuService.getAllMenus();
		model.addAttribute("menuList", menuDTOList);

		//현재 로그인 중인 사용자의 먹은 음식 내역
		List<UserDiningHistoryDTO> diningHistoryDTOList = userDiningHistoryService.getDiningHistory(user.getId());
		model.addAttribute("diningHistoryList", diningHistoryDTOList);

		//현재 로그인 중인 사용자의 중복 제거 먹은 음식 내역
		List<Long> distinctDiningHistory = userDiningHistoryService.getDistinctDiningHistory(user.getId());
		model.addAttribute("distinctDiningHistoryList", distinctDiningHistory);

		// 사용자가 먹지 않은 메뉴 리스트
		List<MenuDTO> notTriedMenuList = userDiningHistoryService.getMenusNotTried(user.getId());
		model.addAttribute("notTriedMenuList", notTriedMenuList);

		// 사용자가 먹은 음식 내역 중 카테고리 TOP 10
		List<CategoryCountDTO> categoryTop10 = userDiningHistoryService.getTopCategoriesByUserId(user.getId());
		model.addAttribute("categoryTop10List", categoryTop10);

		// 사용자의 미니게임 내역
		List<MiniGameMenuRatingDTO> miniGameRatingList = miniGameService.getUsersMiniGameRatingAll(user.getId());
		model.addAttribute("miniGameRatingList", miniGameRatingList);

		// 사용자 ID를 가져와서 카테고리 점수를 계산
		List<CategoryCountDTO> categoryScores = miniGameService.getCategoryScoresByUserId(user.getId());
		model.addAttribute("categoryScores", categoryScores);

		return "recommandView/recTest";
	}

	/**
	 * 먹지 않은 메뉴 추천
	 * 전체 메뉴에서 여태 먹은 메뉴를 제외한 후
	 * 여태 먹은 메뉴들에서 중복되는 카테고리를 count 하여 카테고리 TOP 10을 구합니다
	 * 추려진 카테고리를 사용하여 해당 카테고리에 맞는 메뉴를 추천합니다
	 * @param model 모델에 담아 html에 출력
	 * @param user 현재 로그인 중인 userId
	 * @return untriedMenu.html
	 */
	@GetMapping("recommendUntriedFood")
	public String recommendUntriedFood(Model model, @AuthenticationPrincipal AuthenticatedUser user) {
		// 전체 먹지 않은 메뉴 리스트 출력
		List<MenuDTO> notTriedMenuList = userDiningHistoryService.getMenusNotTried(user.getId());
		log.debug("notTriedMenuList: {}", notTriedMenuList);

		// 사용자가 먹은 음식 내역 중 카테고리 TOP 10
		List<CategoryCountDTO> categoryTop10 = userDiningHistoryService.getTopCategoriesByUserId(user.getId());
		log.debug("categoryTop10: {}", categoryTop10);

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

		// 추천된 메뉴 리스트를 모델에 추가
		model.addAttribute("recommendedMenus", recommendedMenus);

		return "recommandView/untriedMenu"; // 추천 결과를 보여줄 페이지로 이동
	}

	/**
	 * 미니게임 점수 기반 추천
	 * @param model 모델에 담아 출력
	 * @param user 현재 로그인 중인 userId
	 * @return recommandByMinigame.html
	 */
	@GetMapping("recommandByMinigame")
	public String recommendByMinigameScore(Model model, @AuthenticationPrincipal AuthenticatedUser user) {
		// 사용자의 미니게임 기반 카테고리 점수
		List<CategoryCountDTO> minigameCategoryScoreList = miniGameService.getCategoryScoresByUserId(user.getId());
		model.addAttribute("minigameCategoryScoreList", minigameCategoryScoreList);

		// 메뉴의 총 점수를 서비스에서 가져옴
		Map<MenuDTO, Integer> menuScoreMap = miniGameService.getMenuScoreMap(user.getId());
		// menuScoreMap을 정렬된 리스트로 변환
		List<Map.Entry<MenuDTO, Integer>> sortedMenuList = new ArrayList<>(menuScoreMap.entrySet());
		sortedMenuList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // 내림차순 정렬
		model.addAttribute("sortedMenuList", sortedMenuList); // 정렬된 리스트 추가

		// 사용자 ID를 기반으로 추천 메뉴 리스트 가져오기
		List<MenuDTO> recommendedMenus = miniGameService.recommendMenusByCategoryScores(user.getId());
		model.addAttribute("recommendedMenus", recommendedMenus);

		return "recommandView/recommandByMinigame";
	}
}
