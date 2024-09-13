package com.scit45.kiminomenyuwa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("mypage")
@RequiredArgsConstructor
public class MypageController {

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
	public String diningHistory() {
		return "mypageView/diningHistory";
	}
}
