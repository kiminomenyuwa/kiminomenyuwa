package com.scit45.kiminomenyuwa.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.ProfilePhotoDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.FriendshipService;
import com.scit45.kiminomenyuwa.service.MiniGameService;
import com.scit45.kiminomenyuwa.service.ProfilePhotoService;

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
	private final FriendshipService friendshipService;
	private final ProfilePhotoService profilePhotoService;

	/**
	 * 루트 경로("/")에 대한 GET 요청을 처리합니다.
	 * DB에서 메뉴의 개수를 조회하여 모델에 추가하고, 세션을 초기화한 뒤 홈 페이지로 이동합니다.
	 *
	 * @param model 스프링 MVC에서 뷰로 데이터를 전달하기 위해 사용하는 모델 객체
	 * @param session 사용자 세션을 관리하는 HttpSession 객체
	 * @return 홈 페이지 템플릿의 이름 (home.html)
	 */
	@GetMapping("/")
	public String home(Model model, @AuthenticationPrincipal
	AuthenticatedUser user, HttpSession session) {
		// 현재 로그인한 사용자의 ID 가져오기
		String loggedInUserId = getLoggedInUserId();

		// MiniGameService를 통해 DB에서 메뉴의 총 개수를 가져와 모델에 추가
		long menuCount = miniGameService.countAllMenus();
		model.addAttribute("menuCount", menuCount);

		// 친구 수 모델에 추가
		int acceptedFriendCount = friendshipService.getFriendCount(loggedInUserId);
		model.addAttribute("friendCount", acceptedFriendCount);

		// 새로운 게임이 시작되었으므로 세션 초기화
		session.removeAttribute("selectedNumbers");

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

		// 홈 페이지 템플릿으로 이동
		return "homepage";
	}

	// 로그인한 사용자의 ID를 가져오는 메서드
	private String getLoggedInUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails)authentication.getPrincipal();
			return userDetails.getUsername();
		}
		return null;
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
		List<Integer> selectedNumbers = (List<Integer>)session.getAttribute("selectedNumbers");
		if (selectedNumbers == null) {
			selectedNumbers = new ArrayList<>();
		}

		selectedNumbers.add(randomNumber);
		session.setAttribute("selectedNumbers", selectedNumbers);

		return miniGameService.getMenuById(randomNumber); // MenuDTO 반환
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

	/**
	 * 사용자가 메뉴 추천을 요청했을 때, DB에서 랜덤으로 메뉴를 가져와 반환하는 메서드입니다.
	 * 위치 기반 추천을 위해 latitude, longitude, radius가 제공될 수 있습니다.
	 *
	 * @param latitude 사용자의 위도 (선택적)
	 * @param longitude 사용자의 경도 (선택적)
	 * @param radius 반경 (선택적)
	 * @param session 사용자 세션을 관리하는 HttpSession 객체
	 * @return 랜덤으로 선택된 메뉴의 정보를 담은 MenuDTO 객체
	 */
	@PostMapping("/get-random-menu")
	@ResponseBody
	public MenuDTO getRandomMenu(
		@RequestParam(value = "latitude", required = false)
		Double latitude,
		@RequestParam(value = "longitude", required = false)
		Double longitude,
		@RequestParam(value = "radius", required = false)
		Double radius,
		HttpSession session) {

		List<Integer> selectedNumbers = (List<Integer>)session.getAttribute("selectedNumbers");
		if (selectedNumbers == null) {
			selectedNumbers = new ArrayList<>();
		}

		MenuDTO randomMenu;

		// 위치 정보가 있으면 위치 기반 메뉴 추천, 그렇지 않으면 일반 랜덤 추천
		if (latitude != null && longitude != null && radius != null) {
			randomMenu = miniGameService.getRandomMenuByLocation(latitude, longitude, radius);
		} else {
			randomMenu = miniGameService.getRandomMenu();
		}

		// 이미 선택된 메뉴는 다시 선택되지 않도록 처리
		selectedNumbers.add(randomMenu.getMenuId());
		session.setAttribute("selectedNumbers", selectedNumbers);

		return randomMenu; // MenuDTO 반환
	}

	/**
	 * 사용자가 메뉴에 대한 평가를 제출했을 때, 평가를 저장하고, 평가되지 않은 다음 메뉴를 추천하는 메서드입니다.
	 *
	 * @param menuId 평가된 메뉴의 ID
	 * @param rating 사용자가 부여한 평가 점수
	 * @param authentication 현재 로그인한 사용자의 인증 정보
	 * @return 평가되지 않은 다음 메뉴의 정보를 담은 MenuDTO 객체
	 */
	@PostMapping("/rate-menu")
	@ResponseBody
	public MenuDTO rateMenu(@RequestParam("menuId")
	String menuId, @RequestParam("rating")
	float rating, Authentication authentication) {
		String userId = authentication.getName(); // 로그인한 사용자 ID를 가져옴
		log.info("rateMenu 요청 - userId: {}, menuId: {}, rating: {}", userId, menuId, rating);

		try {
			// 평가를 저장
			if (!menuId.isEmpty()) {
				miniGameService.rateMenu(userId, Integer.parseInt(menuId), rating);
				log.info("평가 저장 성공");
			}
			// 점수가 매겨지지 않은 다음 메뉴를 추천
			return miniGameService.getNextUnratedMenu(userId);
		} catch (Exception e) {
			log.error("평가 저장 중 오류 발생", e);
			throw e;
		}
	}

	@PostMapping("/get-random-menu-by-location")
	public ResponseEntity<MenuDTO> getRandomMenuByLocation(
		@RequestParam("latitude")
		double latitude,
		@RequestParam("longitude")
		double longitude,
		@RequestParam("radius")
		double radius) {

		log.info("Received request with latitude: {}, longitude: {}, radius: {}", latitude, longitude, radius);

		MenuDTO menu = miniGameService.getRandomMenuByLocation(latitude, longitude, radius);
		if (menu != null) {
			return ResponseEntity.ok(menu);
		} else {
			return ResponseEntity.noContent().build(); // 반경 내에 메뉴가 없는 경우
		}
	}

	@PostMapping("/get-random-menu2")
	public ResponseEntity<MenuDTO> getRandomMenu() {
		MenuDTO randomMenu = miniGameService.getRandomMenu();
		if (randomMenu != null) {
			return ResponseEntity.ok(randomMenu);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

}
