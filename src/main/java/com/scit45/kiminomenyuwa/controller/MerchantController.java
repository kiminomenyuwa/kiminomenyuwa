package com.scit45.kiminomenyuwa.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scit45.kiminomenyuwa.domain.dto.DiscountDTO;
import com.scit45.kiminomenyuwa.domain.dto.store.MenuRegistrationDTO;
import com.scit45.kiminomenyuwa.domain.dto.store.StoreDetailsDTO;
import com.scit45.kiminomenyuwa.domain.dto.store.StoreRegistrationDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.DiscountService;
import com.scit45.kiminomenyuwa.service.MerchantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("merchant")
public class MerchantController {

	private final MerchantService merchantService;
	private final DiscountService discountService; // DiscountService 주입

	/**
	 * 사장님이 관리하는 가게 목록 조회 페이지
	 *
	 * @param page     페이지 번호 (0부터 시작)
	 * @param size     페이지 크기
	 * @param sortBy   정렬 기준 필드
	 * @param sortDir  정렬 방향 (asc, desc)
	 * @param model    Model 객체
	 * @return 가게 목록 템플릿
	 */
	@GetMapping("/stores")
	@PreAuthorize("hasRole('ROLE_MERCHANT')")
	public String listMyStores(
		@AuthenticationPrincipal
		AuthenticatedUser user,
		@RequestParam(name = "page", defaultValue = "0")
		int page,
		@RequestParam(name = "size", defaultValue = "10")
		int size,
		@RequestParam(name = "sortBy", defaultValue = "name")
		String sortBy,
		@RequestParam(name = "sortDir", defaultValue = "asc")
		String sortDir,
		Model model) {

		// 정렬 및 페이징 설정
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(page, size, sort);

		// 사장님의 가게 목록 조회
		Page<StoreDetailsDTO> storePage = merchantService.getStoresForMerchant(user.getUsername(), pageable);

		int totalPages = storePage.getTotalPages();
		int currentPage = storePage.getNumber();

		// 페이지 번호를 10개씩 묶어서 표시
		int pageBlock = 10;
		int startPage = (currentPage / pageBlock) * pageBlock + 1;
		int endPage = Math.min(startPage + pageBlock - 1, totalPages);

		// 모델에 데이터 추가
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", storePage.getTotalElements());
		model.addAttribute("stores", storePage.getContent());
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("size", size);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "merchantView/merchantStoreList";
	}

	/**
	 * 각 가게 관리 대시보드 페이지로 이동
	 *
	 * @param storeId 가게 ID
	 * @param model   Model 객체
	 * @return 대시보드 템플릿
	 */
	@GetMapping("/stores/{storeId}")
	@PreAuthorize("hasRole('ROLE_MERCHANT')")
	public String merchantsPage(@PathVariable("storeId")
	Integer storeId, @AuthenticationPrincipal
	AuthenticatedUser user, Model model) {

		// 사장님용 가게 상세 정보 조회
		StoreDetailsDTO storeDetails = merchantService.getStoreDetails(storeId, user.getUsername());
		model.addAttribute("storeDetails", storeDetails);
		log.debug("storeDetails: {}", storeDetails);
		log.debug("menus: {}", storeDetails.getMenus());
		log.debug("reviews: {}", storeDetails.getReviews());
		return "merchantView/merchantStoreDetails"; // 템플릿 경로
	}

	/**
	 * 가게 등록 페이지로 이동
	 *
	 * @return
	 */
	@GetMapping("/stores/registration")
	@PreAuthorize("hasRole('ROLE_MERCHANT')")
	public String storeRegistration() {
		return "merchantView/storeRegistration";
	}

	/**
	 * 가게 등록정보를 Repository에 저장
	 *
	 * @param storeRegistrationDTO
	 */
	@ResponseBody
	@PostMapping("/stores/registration")
	@PreAuthorize("hasRole('ROLE_MERCHANT')")
	public ResponseEntity<String> registerStore(
		@ModelAttribute
		StoreRegistrationDTO storeRegistrationDTO, @AuthenticationPrincipal
		AuthenticatedUser user) {
		storeRegistrationDTO.setUserId(user.getId());
		log.debug("전달된 dto객체: {}", storeRegistrationDTO);
		try {
			Integer savedStoreId = merchantService.saveStore(storeRegistrationDTO);
			String redirectUrl = "/merchant/stores/" + savedStoreId;
			return ResponseEntity.ok(redirectUrl);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("가게 등록에 실패했습니다.");
		}
	}

	/**
	 * 메뉴 등록 페이지로 이동 (가게별)
	 *
	 * @param storeId 가게 ID
	 * @param model   Model 객체
	 * @return 메뉴 등록 템플릿
	 */
	@GetMapping("/stores/{storeId}/menus/registration")
	@PreAuthorize("hasRole('ROLE_MERCHANT')")
	public String menuRegistration(@PathVariable("storeId")
	Integer storeId, @AuthenticationPrincipal
	AuthenticatedUser user, Model model) {
		// 가게 상세 정보 조회 (옵션: 메뉴 등록 페이지에서 가게 정보를 표시하고 싶을 경우)
		StoreDetailsDTO storeDetails = merchantService.getStoreDetails(storeId, user.getUsername());
		model.addAttribute("storeDetails", storeDetails);

		// 메뉴 등록 DTO 초기화 및 가게 ID 설정
		MenuRegistrationDTO menuRegistrationDTO = new MenuRegistrationDTO();
		menuRegistrationDTO.setStoreId(storeId);
		model.addAttribute("menuRegistrationDTO", menuRegistrationDTO);

		return "merchantView/menuRegistration";
	}

	/**
	 * 새로운 메뉴를 등록하는 엔드포인트 (가게별)
	 *
	 * @param storeId              가게 ID
	 * @param menuRegistrationDTO  클라이언트에서 전달된 메뉴 등록 정보
	 * @return 성공 여부에 대한 응답
	 */
	@ResponseBody
	@PostMapping("/stores/{storeId}/menus/registration")
	@PreAuthorize("hasRole('ROLE_MERCHANT')")
	public ResponseEntity<String> registerMenu(
		@PathVariable("storeId")
		Integer storeId,
		@ModelAttribute
		MenuRegistrationDTO menuRegistrationDTO) {
		log.debug("전달된 메뉴 DTO 객체: {}", menuRegistrationDTO);

		// storeId를 DTO에 설정 (보안 강화: URL의 storeId와 DTO의 storeId가 일치하는지 확인)
		if (!storeId.equals(menuRegistrationDTO.getStoreId())) {
			return ResponseEntity.status(400).body("가게 ID 불일치");
		}

		try {
			merchantService.saveMenu(menuRegistrationDTO);
			String redirectUrl = "/merchant/stores/" + menuRegistrationDTO.getStoreId();
			return ResponseEntity.ok(redirectUrl);
		} catch (Exception e) {
			log.error("메뉴 등록 중 오류 발생: ", e);
			return ResponseEntity.status(500).body("메뉴 등록에 실패했습니다.");
		}
	}

	/**
	 * 할인 정보 저장
	 */
	@ResponseBody
	@PostMapping("/discounts")
	@PreAuthorize("hasRole('ROLE_MERCHANT')")
	public ResponseEntity<String> saveDiscount(@RequestBody
	DiscountDTO discountDTO, @AuthenticationPrincipal
	AuthenticatedUser user) {
		// 메뉴 소유권 확인
		if (!merchantService.isMenuOwnedByMerchant(discountDTO.getMenuId(), user.getUsername())) {
			return ResponseEntity.status(403).body("해당 메뉴에 대한 권한이 없습니다.");
		}

		discountService.saveDiscount(discountDTO);
		return ResponseEntity.ok("할인 정보가 저장되었습니다.");
	}

	/**
	 * 특정 메뉴의 할인 정보 조회
	 */
	@ResponseBody
	@GetMapping("/discounts/{menuId}")
	@PreAuthorize("hasRole('ROLE_MERCHANT')")
	public ResponseEntity<DiscountDTO> getDiscountByMenuId(@PathVariable
	Integer menuId, @AuthenticationPrincipal
	AuthenticatedUser user) {
		// 메뉴 소유권 확인
		if (!merchantService.isMenuOwnedByMerchant(menuId, user.getUsername())) {
			return ResponseEntity.status(403).build();
		}

		DiscountDTO discountDTO = discountService.getDiscountByMenuId(menuId);
		if (discountDTO != null) {
			return ResponseEntity.ok(discountDTO);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	/**
	 * 할인 정보 삭제
	 */
	@ResponseBody
	@DeleteMapping("/discounts/{menuId}")
	@PreAuthorize("hasRole('ROLE_MERCHANT')")
	public ResponseEntity<String> deleteDiscountByMenuId(
		@PathVariable("menuId")
		Integer menuId,
		@AuthenticationPrincipal
		AuthenticatedUser user) {
		// 메뉴 소유권 확인
		if (!merchantService.isMenuOwnedByMerchant(menuId, user.getUsername())) {
			return ResponseEntity.status(403).body("해당 메뉴에 대한 권한이 없습니다.");
		}

		discountService.deleteDiscountByMenuId(menuId);
		return ResponseEntity.ok("할인 정보가 삭제되었습니다.");
	}
}
