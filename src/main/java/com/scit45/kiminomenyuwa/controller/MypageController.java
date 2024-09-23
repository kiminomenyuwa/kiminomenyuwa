package com.scit45.kiminomenyuwa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scit45.kiminomenyuwa.domain.dto.BudgetDTO;
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

	/**
	 * 캘린더에서 선택한 음식을 dining_history에 저장
	 * @param user
	 * @return
	 */
	@ResponseBody
	@GetMapping("/api/dining-history")
	public List<UserDiningHistoryDTO> getDiningHistory(@AuthenticationPrincipal AuthenticatedUser user) {
		String userId = user.getUsername();
		log.debug("유저 아이디 {}", userId);
		return myPageService.getDiningHistoryByUserId(userId);
	}

	@ResponseBody
	@GetMapping("/api/menus/{menuId}")
	public ResponseEntity<MenuDTO> getMenuById(@PathVariable int menuId) {
		MenuDTO menu = myPageService.getMenuById(menuId);
		return ResponseEntity.ok(menu);
	}

	/**
	 * 예산을 저장하는 API
	 * @param budgetDTO 예산 정보
	 * @return ResponseEntity
	 */
	@ResponseBody
	@PostMapping("/api/budget")
	public ResponseEntity<String> saveMonthlyBudget(@RequestBody BudgetDTO budgetDTO,
		@AuthenticationPrincipal AuthenticatedUser user) {
		try {
			// 예산 DTO에 사용자 ID 설정
			budgetDTO.setUserId(user.getUsername());

			// 현재 연도와 월 설정
			java.time.LocalDate today = java.time.LocalDate.now();
			budgetDTO.setYear(today.getYear());
			budgetDTO.setMonth(today.getMonthValue());

			// 예산 저장 서비스 호출
			myPageService.saveBudget(budgetDTO);
			return ResponseEntity.ok("예산이 저장되었습니다.");
		} catch (Exception e) {
			log.error("예산 저장 실패: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예산 저장에 실패했습니다.");
		}
	}

	/**
	 * 특정 연도와 월의 남은 예산을 가져오는 API
	 * @param year 연도
	 * @param month 월
	 * @param user 인증된 사용자
	 * @return 남은 예산
	 */
	@ResponseBody
	@GetMapping("/api/budget/remaining")
	public ResponseEntity<Map<String, Integer>> getRemainingBudget(@RequestParam Integer year,
		@RequestParam Integer month, @AuthenticationPrincipal AuthenticatedUser user) {
		String userId = user.getUsername();
		BudgetDTO budgetDTO = myPageService.getRemainingBudget(userId, year, month);

		Map<String, Integer> response = new HashMap<>();
		response.put("budget", budgetDTO.getBudget());

		return ResponseEntity.ok(response);
	}

	/**
	 * 특정 연도와 월의 예산을 초기화하는 API
	 * @param year 연도
	 * @param month 월
	 * @param user 인증된 사용자
	 * @return ResponseEntity
	 */
	@ResponseBody
	@PostMapping("/api/budget/initialize")
	public ResponseEntity<String> initializeBudget(
		@RequestParam Integer year,
		@RequestParam Integer month,
		@AuthenticationPrincipal AuthenticatedUser user) {
		try {
			String userId = user.getUsername();
			myPageService.initializeBudget(userId, year, month);
			return ResponseEntity.ok("예산이 초기화되었습니다.");
		} catch (Exception e) {
			log.error("예산 초기화 실패: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예산 초기화에 실패했습니다.");
		}
	}
}
