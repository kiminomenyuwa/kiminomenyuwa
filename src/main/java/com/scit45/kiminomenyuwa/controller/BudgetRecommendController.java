package com.scit45.kiminomenyuwa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.BudgetRecommendService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor

public class BudgetRecommendController {

	private final BudgetRecommendService budgetRecommendService;

	@GetMapping("/recommend/recommendByBudget")
	public String recommendByBudget() {
		return "recommendView/recommendByBudget"; // ��플�� 경로 변경
	}

	/**
	 * 남은 예산과 남은 일수를 기반으로 추천 메뉴를 가져오는 API
	 *
	 * @param budget 남은 예산
	 * @param days   남은 일수
	 * @return 추천 메뉴 목록
	 */
	@ResponseBody
	@GetMapping("/api/menus/budgetRecommended")
	public ResponseEntity<List<MenuDTO>> getRecommendedMenus(
		@RequestParam Integer budget,
		@RequestParam Integer days) {
		try {
			log.debug("getRecommendedMenus 호출 - 예산: {}, 남은 일수: {}", budget, days);
			List<MenuDTO> recommendedMenus = budgetRecommendService.getRecommendedMenus(budget, days);
			return ResponseEntity.ok(recommendedMenus);
		} catch (Exception e) {
			log.error("추천 메뉴 조회 실패: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * 특정 사용자와 특정 달에 대한 남은 예산을 계산하여 반환하는 API
	 *
	 * @param year  연도
	 * @param month 월
	 * @param user  인증된 사용자
	 * @return 남은 예산
	 */
	@ResponseBody
	@GetMapping("/api/budget/remaining")
	public ResponseEntity<Map<String, Integer>> getRemainingBudget(
		@RequestParam Integer year,
		@RequestParam Integer month,
		@AuthenticationPrincipal AuthenticatedUser user) {
		try {
			String userId = user.getUsername(); // 인증된 사용자 ID 가져오기
			log.debug("getRemainingBudget 호출 - 사용자 ID: {}, 연도: {}, 월: {}", userId, year, month);
			int remainingBudget = budgetRecommendService.calculateRemainingBudget(userId, year, month);

			Map<String, Integer> response = new HashMap<>();
			response.put("budget", remainingBudget);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("남은 예산 계산 실패: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
