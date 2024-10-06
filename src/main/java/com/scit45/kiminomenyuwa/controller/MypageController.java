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
import com.scit45.kiminomenyuwa.domain.dto.MiniGameMenuRatingDTO;
import com.scit45.kiminomenyuwa.domain.dto.ProfilePhotoDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.MenuService;
import com.scit45.kiminomenyuwa.service.MiniGameService;
import com.scit45.kiminomenyuwa.service.MyPageService;
import com.scit45.kiminomenyuwa.service.ProfilePhotoService;
import com.scit45.kiminomenyuwa.service.StoreSearchService;
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
	private final MyPageService myPageService;
	private final MiniGameService miniGameService;
	private final StoreSearchService storeSearchService;
	private final ProfilePhotoService profilePhotoService;

	/**
	 * 마이페이지 메인화면
	 * @return calendar.html
	 */
	@GetMapping
	public String calendar(Model model, @AuthenticationPrincipal AuthenticatedUser user) {
		// 현재 로그인한 사용자의 프로필 사진 URL 추가
		if (user != null) {
			String userId = user.getUsername(); // 로그인한 사용자의 ID 가져오기
			ProfilePhotoDTO profilePhotoDTO = profilePhotoService.getUserProfilePhotoInfo(userId);
			if (profilePhotoDTO != null) {
				// 프로필 사진의 URL을 모델에 추가
				String profilePhotoUrl = "/files/" + profilePhotoDTO.getSavedName();
				model.addAttribute("profilePhotoUrl", profilePhotoUrl);
			} else {
				// 프로필 사진이 없는 경우 기본 이미지를 사용하도록 설정
				model.addAttribute("profilePhotoUrl", "/images/default-profile.png");
			}
		}
		return "mypageView/calendar";
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

		// 현재 로그인한 사용자의 프로필 사진 URL 추가
		if (user != null) {
			String userId = user.getUsername(); // 로그인한 사용자의 ID 가져오기
			ProfilePhotoDTO profilePhotoDTO = profilePhotoService.getUserProfilePhotoInfo(userId);
			if (profilePhotoDTO != null) {
				// 프로필 사진의 URL을 모델에 추가
				String profilePhotoUrl = "/files/" + profilePhotoDTO.getSavedName();
				model.addAttribute("profilePhotoUrl", profilePhotoUrl);
			} else {
				// 프로필 사진이 없는 경우 기본 이미지를 사용하도록 설정
				model.addAttribute("profilePhotoUrl", "/images/default-profile.png");
			}
		}

		return "mypageView/diningHistory";
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
		return myPageService.getDiningHistoryByUserId(userId);
	}

	@ResponseBody
	@GetMapping("/api/menus/{menuId}")
	public ResponseEntity<MenuDTO> getMenuById(@PathVariable int menuId) {
		MenuDTO menu = myPageService.getMenuById(menuId);
		return ResponseEntity.ok(menu);
	}

	@GetMapping("minigameHistory")
	public String minigameHistory(Model model, @AuthenticationPrincipal AuthenticatedUser user) {
		// 사용자의 미니게임 내역

		// 현재 로그인한 사용자의 프로필 사진 URL 추가
		if (user != null) {
			String userId = user.getUsername(); // 로그인한 사용자의 ID 가져오기
			ProfilePhotoDTO profilePhotoDTO = profilePhotoService.getUserProfilePhotoInfo(userId);
			if (profilePhotoDTO != null) {
				// 프로필 사진의 URL을 모델에 추가
				String profilePhotoUrl = "/files/" + profilePhotoDTO.getSavedName();
				model.addAttribute("profilePhotoUrl", profilePhotoUrl);
			} else {
				// 프로필 사진이 없는 경우 기본 이미지를 사용하도록 설정
				model.addAttribute("profilePhotoUrl", "/images/default-profile.png");
			}
		}

		List<MiniGameMenuRatingDTO> miniGameRatingList = miniGameService.getUsersMiniGameRatingAll(user.getId());
		model.addAttribute("miniGameRatingList", miniGameRatingList);
		return "mypageView/minigameHistory";
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

			// 클라이언트가 전송한 year와 month를 사용 (덮어쓰지 않음)
			// budgetDTO.setYear(today.getYear());
			// budgetDTO.setMonth(today.getMonthValue());

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
	public ResponseEntity<String> initializeBudget(@RequestParam Integer year, @RequestParam Integer month,
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

	/**
	 * 사용자의 찜 목록을 조회하고 정렬된 결과를 반환합니다.
	 *
	 * @param sortBy    정렬 기준 ("distance", "newest", "oldest")
	 * @param model     모델 객체
	 * @param latitude  현재 위치의 위도 (거리 기준 정렬 시 필요)
	 * @param longitude 현재 위치의 경도 (거리 기준 정렬 시 필요)
	 * @param radius    반경 (미터 단위, 거리 기준 정렬 시 필요)
	 * @return 찜 목록 페이지 템플릿 이름
	 */
	@GetMapping("/favorites")
	public String getUserFavorites(
		@RequestParam(value = "sortBy", defaultValue = "distance") String sortBy,
		@RequestParam(value = "latitude", required = false) Double latitude,
		@RequestParam(value = "longitude", required = false) Double longitude,
		@RequestParam(value = "radius", defaultValue = "1000") double radius,
		@AuthenticationPrincipal AuthenticatedUser user,
		Model model) {

		// 현재 로그인한 사용자의 프로필 사진 URL 추가
		if (user != null) {
			String userId = user.getUsername(); // 로그인한 사용자의 ID 가져오기
			ProfilePhotoDTO profilePhotoDTO = profilePhotoService.getUserProfilePhotoInfo(userId);
			if (profilePhotoDTO != null) {
				// 프로필 사진의 URL을 모델에 추가
				String profilePhotoUrl = "/files/" + profilePhotoDTO.getSavedName();
				model.addAttribute("profilePhotoUrl", profilePhotoUrl);
			} else {
				// 프로필 사진이 없는 경우 기본 이미지를 사용하도록 설정
				model.addAttribute("profilePhotoUrl", "/images/default-profile.png");
			}
		}

		String userId = user.getUsername(); // 현재 사용자 ID 가져오기
		List<StoreResponseDTO> favorites = storeSearchService.getUserFavorites(userId, sortBy,
			latitude != null ? latitude : 37.572,
			longitude != null ? longitude : 126.985,
			radius);
		model.addAttribute("favorites", favorites);
		model.addAttribute("sortBy", sortBy);
		return "myPageView/favorites";
	}
}
