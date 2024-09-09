package com.scit45.kiminomenyuwa.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.service.MiniGameService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 홈 페이지와 관련된 요청을 처리하는 컨트롤러 클래스.
 * 사용자에게 메뉴 목록을 제공하고, 랜덤 메뉴를 선택하여 표시하는 기능을 담당합니다.
 */
@Controller
@Slf4j
@RequiredArgsConstructor // Lombok을 사용하여 final 필드에 대한 생성자를 자동 생성합니다.
public class HomeController {

	// MiniGameService를 주입받아 DB에서 메뉴 데이터를 조회합니다.
	private final MiniGameService miniGameService;

	/**
	 * 루트 경로("/")에 대한 GET 요청을 처리합니다.
	 * DB에서 메뉴의 개수를 조회하여 모델에 추가하고, 세션을 초기화한 뒤 홈 페이지로 이동합니다.
	 *
	 * @param model 스프링 MVC에서 뷰로 데이터를 전달하기 위해 사용하는 모델 객체
	 * @param session 사용자 세션을 관리하는 HttpSession 객체
	 * @return 홈 페이지 템플릿의 이름 (home.html)
	 */
	@GetMapping("/")
	public String home(Model model, HttpSession session) {
		// MiniGameService를 통해 DB에서 메뉴의 총 개수를 가져와 모델에 추가
		long menuCount = miniGameService.countAllMenus();
		model.addAttribute("menuCount", menuCount);

		// 새로운 게임이 시작되었으므로, 세션에 저장된 선택된 메뉴 번호 리스트를 초기화
		session.removeAttribute("selectedNumbers");

		// 홈 페이지 템플릿으로 이동
		return "home";
	}

	/**
	 * 랜덤으로 선택된 메뉴 번호를 세션에 저장하고, 해당 번호에 해당하는 메뉴 정보를 반환하는 메서드입니다.
	 *
	 * @param randomNumber 랜덤으로 선택된 메뉴 번호
	 * @param session 사용자 세션을 관리하는 HttpSession 객체
	 * @return 선택된 메뉴의 정보를 담은 MenuDTO 객체
	 */
	@PostMapping("/save-random-number")
	@ResponseBody
	public MenuDTO saveRandomNumber(@RequestParam("randomNumber")
	int randomNumber, HttpSession session) {
		// 세션에서 이미 선택된 숫자 리스트를 가져옴, 없으면 빈 리스트 초기화
		List<Integer> selectedNumbers = (List<Integer>)session.getAttribute("selectedNumbers");
		if (selectedNumbers == null) {
			selectedNumbers = new ArrayList<>();
		}

		// 새로운 랜덤 숫자를 리스트에 추가하고 세션에 저장
		selectedNumbers.add(randomNumber);
		session.setAttribute("selectedNumbers", selectedNumbers);

		// 선택된 메뉴의 정보를 MiniGameService를 통해 조회하고 반환
		return miniGameService.getMenuById(randomNumber);
	}

	/**
	 * 세션을 초기화하는 메서드입니다.
	 * 게임이 시작될 때마다 호출되어 기존에 저장된 선택된 메뉴 번호 리스트를 제거합니다.
	 *
	 * @param session 사용자 세션을 관리하는 HttpSession 객체
	 */
	@PostMapping("/reset-session")
	public void resetSession(HttpSession session) {
		// 세션 초기화: 선택된 메뉴 번호 리스트를 제거
		session.removeAttribute("selectedNumbers");
	}
}
