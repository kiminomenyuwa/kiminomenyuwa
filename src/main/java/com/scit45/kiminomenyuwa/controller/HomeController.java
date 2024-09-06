package com.scit45.kiminomenyuwa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.service.MiniGameService;

import lombok.RequiredArgsConstructor;

/**
 * 홈 페이지와 관련된 요청을 처리하는 컨트롤러 클래스.
 * 사용자에게 메뉴 목록을 제공하고, 랜덤 메뉴를 선택하여 표시하는 기능을 담당합니다.
 */
@Controller
@RequiredArgsConstructor // Lombok을 사용하여 final 필드에 대한 생성자를 자동 생성합니다.
public class HomeController {

	// MiniGameService를 주입받아 DB에서 메뉴 데이터를 조회합니다.
	private final MiniGameService miniGameService;

	/**
	 * 루트 경로("/")에 대한 GET 요청을 처리합니다.
	 * DB에서 모든 메뉴를 조회하고, 모델에 추가하여 홈 페이지로 전달합니다.
	 *
	 * @param model 스프링 MVC에서 뷰로 데이터를 전달하기 위해 사용하는 모델 객체
	 * @return 홈 페이지 템플릿의 이름 (home.html)
	 */
	@GetMapping("/")
	public String home(Model model) {
		// MiniGameService를 통해 DB에서 모든 메뉴를 조회하여 리스트로 가져옵니다.
		List<MenuDTO> allMenus = miniGameService.getAllMenus();

		// 조회한 메뉴 리스트를 모델에 추가하여 뷰로 전달합니다.
		model.addAttribute("menus", allMenus);

		// home.html 템플릿을 반환하여, 사용자에게 홈 페이지를 렌더링합니다.
		return "home";
	}
}
