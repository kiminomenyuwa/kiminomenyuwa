package com.scit45.kiminomenyuwa.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.MenuService;
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
		List<Object[]> categoryTop10 = userDiningHistoryService.getTopCategoriesByUserId(user.getId());
		model.addAttribute("categoryTop10List", categoryTop10);

		return "recommandView/recTest";
	}

	@GetMapping("recommendUntriedFood")
	public String recommendUntriedFood(Model model, @AuthenticationPrincipal AuthenticatedUser user) {
		// 전체 먹지 않은 메뉴 리스트 출력
		List<MenuDTO> notTriedMenuList = userDiningHistoryService.getMenusNotTried(user.getId());
		log.debug("notTriedMenuList: {}", notTriedMenuList);

		// 사용자가 먹은 음식 내역 중 카테고리 TOP 10
		List<Object[]> categoryTop10 = userDiningHistoryService.getTopCategoriesByUserId(user.getId());
		log.debug("categoryTop10: {}", categoryTop10);

		// 추천할 메뉴 리스트 (최종 결과)
		List<MenuDTO> recommendedMenus = new ArrayList<>();

		// 카테고리 TOP 10을 기준으로 아직 먹지 않은 메뉴 추천
		for (Object[] category : categoryTop10) {
			String categoryName = (String) category[0];  // 카테고리 이름

			// 해당 카테고리에 속한 메뉴 필터링
			for (MenuDTO menu : notTriedMenuList) {
				// 메뉴의 categories 필드가 null이 아닌지 확인한 후 contains 호출
				if (menu.getCategories() != null && menu.getCategories().contains(categoryName)) {
					recommendedMenus.add(menu);
				}
			}
		}

		// 추천된 메뉴 리스트를 모델에 추가
		model.addAttribute("recommendedMenus", recommendedMenus);

		return "recommandView/untriedMenu"; // 추천 결과를 보여줄 페이지로 이동
	}


}
