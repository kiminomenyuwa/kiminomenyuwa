package com.scit45.kiminomenyuwa.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.MenuService;
import com.scit45.kiminomenyuwa.service.UserDiningHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("mypage")
@RequiredArgsConstructor
public class MypageController {
	private final MenuService menuService;
	private final UserDiningHistoryService userDiningHistoryService;

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
}
