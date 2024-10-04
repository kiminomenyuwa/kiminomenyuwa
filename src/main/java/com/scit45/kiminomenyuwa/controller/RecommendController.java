package com.scit45.kiminomenyuwa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.MiniGameMenuRatingDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.MenuService;
import com.scit45.kiminomenyuwa.service.MiniGameService;
import com.scit45.kiminomenyuwa.service.RecommendService;
import com.scit45.kiminomenyuwa.service.UserDiningHistoryService;
import com.scit45.kiminomenyuwa.service.UserService;
import com.scit45.kiminomenyuwa.utils.AgeUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("recommend")
@RequiredArgsConstructor
public class RecommendController {

	private final MenuService menuService;
	private final UserDiningHistoryService userDiningHistoryService;
	private final MiniGameService miniGameService;
	private final RecommendService recommendService;
	private final UserService userService;

	// /**
	//  * 추천기능 테스트용 페이지
	//  * @return recTest.html
	//  */
	// @GetMapping("test")
	// public String recommendTest(Model model, @AuthenticationPrincipal AuthenticatedUser user) {
	//
	// 	//전체 메뉴 리스트 출력
	// 	List<MenuDTO> menuDTOList = menuService.getAllMenus();
	// 	model.addAttribute("menuList", menuDTOList);
	//
	// 	//현재 로그인 중인 사용자의 먹은 음식 내역
	// 	List<UserDiningHistoryDTO> diningHistoryDTOList = userDiningHistoryService.getDiningHistory(user.getId());
	// 	model.addAttribute("diningHistoryList", diningHistoryDTOList);
	//
	// 	//현재 로그인 중인 사용자의 중복 제거 먹은 음식 내역
	// 	List<Long> distinctDiningHistory = userDiningHistoryService.getDistinctDiningHistory(user.getId());
	// 	model.addAttribute("distinctDiningHistoryList", distinctDiningHistory);
	//
	// 	// 사용자가 먹지 않은 메뉴 리스트
	// 	List<MenuDTO> notTriedMenuList = userDiningHistoryService.getMenusNotTried(user.getId());
	// 	model.addAttribute("notTriedMenuList", notTriedMenuList);
	//
	// 	// 사용자가 먹은 음식 내역 중 카테고리 TOP 10
	// 	List<CategoryCountDTO> categoryTop10 = userDiningHistoryService.getTopCategoriesByUserId(user.getId());
	// 	model.addAttribute("categoryTop10List", categoryTop10);
	//
	// 	// 사용자의 미니게임 내역
	// 	List<MiniGameMenuRatingDTO> miniGameRatingList = miniGameService.getUsersMiniGameRatingAll(user.getId());
	// 	model.addAttribute("miniGameRatingList", miniGameRatingList);
	//
	// 	// 사용자 ID를 가져와서 카테고리 점수를 계산
	// 	List<CategoryCountDTO> categoryScores = miniGameService.getCategoryScoresByUserId(user.getId());
	// 	model.addAttribute("categoryScores", categoryScores);
	//
	// 	return "recommendView/recTest";
	// }

	// /**
	//  * 먹지 않은 메뉴 추천
	//  * 전체 메뉴에서 여태 먹은 메뉴를 제외한 후
	//  * 여태 먹은 메뉴들에서 중복되는 카테고리를 count 하여 카테고리 TOP 10을 구합니다
	//  * 추려진 카테고리를 사용하여 해당 카테고리에 맞는 메뉴를 추천합니다
	//  * @param model 모델에 담아 html에 출력
	//  * @param user 현재 로그인 중인 userId
	//  * @return untriedMenu.html
	//  */
	// @GetMapping("recommendUntriedFood")
	// public String recommendUntriedFood(Model model, @AuthenticationPrincipal AuthenticatedUser user) {
	// 	// 추천할 메뉴 리스트 (최종 결과)
	// 	List<MenuDTO> recommendedMenus = recommendService.recommendMenusByUntriedCategory(user.getId());
	// 	model.addAttribute("recommendedMenus", recommendedMenus);
	//
	// 	return "recommendView/untriedMenu"; // 추천 결과를 보여줄 페이지로 이동
	// }

	/**
	 * 미니게임 점수 기반 추천
	 * @param model 모델에 담아 출력
	 * @param user 현재 로그인 중인 userId
	 * @return recommendByMinigame.html
	 */
	@GetMapping("recommendByMinigame")
	public String recommendByMinigameScore(Model model, @AuthenticationPrincipal AuthenticatedUser user) {
		// 사용자의 미니게임 기반 카테고리 점수
		List<CategoryCountDTO> minigameCategoryScoreList = miniGameService.getCategoryScoresByUserId(user.getId());
		model.addAttribute("minigameCategoryScoreList", minigameCategoryScoreList);

		// 메뉴의 총 점수를 서비스에서 가져옴
		Map<MenuDTO, Integer> menuScoreMap = miniGameService.getMenuScoreMap(user.getId());
		// menuScoreMap을 정렬된 리스트로 변환 (Map 구조라서 아래의 별도 작업이 필요했음ㄷㄷ)
		List<Map.Entry<MenuDTO, Integer>> sortedMenuList = new ArrayList<>(menuScoreMap.entrySet());
		sortedMenuList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // 내림차순 정렬
		model.addAttribute("sortedMenuList", sortedMenuList); // 정렬된 리스트 추가

		// 사용자 ID를 기반으로 추천 메뉴 리스트 가져오기
		// List<MenuDTO> recommendedMenus = recommendService.recommendMenusByCategoryScores(user.getId(), );
		// model.addAttribute("recommendedMenus", recommendedMenus);

		return "recommendView/recommendByMinigame";
	}

	// /**
	//  * 연령대별 7일간 인기 메뉴 추천
	//  * @param user 현재 로그인 중인 ID
	//  * @param model 모델에 담아 출력
	//  * @return List<MenuDTO>의 인기 메뉴 리스트
	//  */
	// @GetMapping("recommendByAge")
	// public String recommendAgePopularMenus(@AuthenticationPrincipal AuthenticatedUser user, Model model) {
	// 	// userId를 통해 UserDTO 조회
	// 	UserDTO userDTO = userService.getUserByUserId(user.getId());
	//
	// 	// UserDTO에서 birthDate를 가져와서 연령대 계산
	// 	String ageGroup = AgeUtils.getAgeGroup(userDTO.getBirthDate());
	//
	// 	// 연령대에 해당하는 인기 메뉴 추천 리스트 가져오기
	// 	List<MenuDTO> agePopularMenus = recommendService.recommendPopularMenusByAgeGroup(ageGroup);
	//
	// 	model.addAttribute("agePopularMenus", agePopularMenus); // 연령대별 인기 메뉴 리스트를 모델에 추가
	// 	model.addAttribute("ageGroup", ageGroup); // 연령대 정보를 모델에 추가
	// 	return "recommendView/recommendByAge";
	// }
}