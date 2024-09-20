package com.scit45.kiminomenyuwa.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
		return "recommandView/recTest";
	}

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
}