package com.scit45.kiminomenyuwa.controller;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("recommand")
@RequiredArgsConstructor
public class RecommandController {

	private final MenuService menuService;

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
		List<UserDiningHistoryDTO> diningHistoryDTOList = menuService.getDiningHistory(user.getId());
		model.addAttribute("diningHistoryList", diningHistoryDTOList);


		return "recommandView/recTest";
	}


}
