package com.scit45.kiminomenyuwa.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.MenuService;
import com.scit45.kiminomenyuwa.service.UserDiningHistoryService;
import com.scit45.kiminomenyuwa.service.MyPageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("mypage")
@RequiredArgsConstructor
public class MypageController {
	private final MenuService menuService;
	private final UserDiningHistoryService userDiningHistoryService;
	private final MyPageService myPageService;

	/**
	 * 마이페이지 메인화면
	 * @return mypageMain.html
	 */
	@GetMapping("mypageMain")
	public String mypageMain() {
		return "mypageView/mypageMain";
	}

	/**
	 * 먹은 음식 내역
	 * @return diningHistory.html
	 */
	@GetMapping("diningHistory")
	public String diningHistory(Model model, @AuthenticationPrincipal AuthenticatedUser user) {

		//현재 로그인 중인 사용자의 먹은 음식 내역
		List<UserDiningHistoryDTO> diningHistoryDTOList = userDiningHistoryService.getDiningHistory(user.getId());
		model.addAttribute("diningHistoryList", diningHistoryDTOList);

		return "mypageView/diningHistory";
	}

	@GetMapping("calender")
	public String calendar() {
		return "mypageView/calender";
	}

	/**
	 *
	 * @param storeName 가게 이름
	 * @return 입력한 가게에 등록된 메뉴
	 */
	@ResponseBody
	@GetMapping("/api/menus/store")
	public List<MenuDTO> getMenusByStoreName(@RequestParam String storeName) {
		log.debug("storeName: {}", storeName);
		return myPageService.getMenusByStoreName(storeName);
	}

	/**
	 * 선택한 메뉴를 db에 저장
	 * @param userDiningHistoryDTO
	 */
	@ResponseBody
	@PostMapping("/api/dining-history")
	public void saveDiningHistory(@RequestBody UserDiningHistoryDTO userDiningHistoryDTO,
		@AuthenticationPrincipal AuthenticatedUser user) {
		log.debug("saveDiningHistory: {}", userDiningHistoryDTO);
		log.debug("로그인한 유저: {}", user.getId());
		myPageService.saveDiningHistory(userDiningHistoryDTO);
	}

	@ResponseBody
	@GetMapping("/api/dining-history")
	public List<UserDiningHistoryDTO> getDiningHistory(@AuthenticationPrincipal AuthenticatedUser user) {
		String userId = user.getUsername();
		return myPageService.getDiningHistoryByUserId(userId);
	}
}
