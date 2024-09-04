package com.scit45.kiminomenyuwa.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scit45.kiminomenyuwa.domain.dto.UserDTO;
import com.scit45.kiminomenyuwa.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원 관련 컨트롤러 (회원가입, 로그인)
 */
@Slf4j
@Controller
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	/**
	 * 로그인 페이지로 이동
	 * @return 로그인 폼으로 이동
	 */
	@GetMapping("loginForm")
	public String loginForm() {

		return "/userView/loginForm";
	}

	/**
	 * 회원가입 양식으로 이동
	 * @return 회원가입 폼으로 이동
	 */
	@GetMapping("joinForm")
	public String joinForm() {

		return "/userView/joinForm";
	}

	/**
	 * 회원가입 처리
	 * @param user joinForm.html에서 받은 회원 정보
	 * @return 홈페이지로 리다이렉트
	 */
	@PostMapping("join")
	public String join(@ModelAttribute UserDTO user) {
		log.debug("전달된 회원정보 : {}", user);

		userService.join(user);

		return "redirect:/";
	}

	/**
	 * ID 중복확인 폼
	 * @return idCheck.html
	 */
	@GetMapping("idCheck")
	public String idCheck() {

		return "/userView/idCheck";
	}

	/**
	 * ID 중복확인 처리메서드
	 * @param searchId idCheck.html에서 받은 사용자 아이디
	 * @param model
	 * @return
	 */
	@PostMapping("idCheck")
	public String idCheck2(@RequestParam("searchId") String searchId, Model model) {
		// ID 중복확인 폼에서 전달된 검색할 아이디를 받아서 log 출력
		log.debug("전달받은 ID : {}", searchId);
		// 서비스의 메서드로 검색할 아이디를 전달받아서 조회
		// 해당 아이디를 쓰는 회원이 있으면 false, 없으면 true 리턴받음
		boolean result = userService.findId(searchId);
		// 검색한 아이디와 조회결과를 모델에 저장
		model.addAttribute("searchId", searchId);
		model.addAttribute("result", result);
		// 검색한 페이지로 다시 이동
		return "/userView/idCheck";
	}

}
