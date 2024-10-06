package com.scit45.kiminomenyuwa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scit45.kiminomenyuwa.domain.dto.ProfilePhotoDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewRequestDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewResponseDTO;
import com.scit45.kiminomenyuwa.domain.dto.receipt.ReceiptDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.ProfilePhotoService;
import com.scit45.kiminomenyuwa.service.ReviewService;
import com.scit45.kiminomenyuwa.service.UserDiningHistoryService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;
	private final UserDiningHistoryService userDiningHistoryService;
	private final ProfilePhotoService profilePhotoService;

	/**
	 * 영수증 업로드 페이지 표시
	 */
	@GetMapping("/write")
	public String showUploadPage(@AuthenticationPrincipal AuthenticatedUser user, Model model) {
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
		return "reviewView/reviewForm"; // 템플릿 경로 변경
	}

	/**
	 * 리뷰 제출 처리
	 */
	@PostMapping("/write")
	@ResponseBody
	public ResponseEntity<Map<String, String>> submitReview(
		@ModelAttribute ReviewRequestDTO reviewDTO,
		@RequestParam("storeId") Integer storeId,
		@AuthenticationPrincipal AuthenticatedUser user,
		HttpSession session,  // 세션 객체 추가
		Model model,
		RedirectAttributes redirectAttributes) {

		String loggedInUserId = user.getId();  // 사용자 ID 또는 username

		Map<String, String> response = new HashMap<>();
		try {
			// 리뷰 저장
			reviewService.saveReviewWithPhotos(reviewDTO, loggedInUserId);

			// 세션에서 영수증 정보 가져오기
			ReceiptDTO receiptDTO = (ReceiptDTO)session.getAttribute("uploadedReceipt");
			if (receiptDTO != null) {
				receiptDTO.getItems().forEach(item -> {
					String description = item.getDescription();
					log.debug("Description: {}", description);

					// 먹은 내역 저장
					userDiningHistoryService.saveDiningHistory(loggedInUserId, description);
				});

			} else {
				log.warn("세션에 영수증 정보가 없습니다.");
			}

			response.put("redirect", "/stores/" + storeId);
			response.put("message", "리뷰가 저장되었습니다.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("리뷰 작성 중 오류 발생: {}", e.getMessage());
			model.addAttribute("errorMessage", "리뷰 작성 중 오류가 발생했습니다.");
			model.addAttribute("reviewRequestDTO", reviewDTO);
			response.put("redirect", "/stores/" + storeId);
			return ResponseEntity.badRequest().body(response);
		}
	}

	/**
	 * 사용자의 전체 리뷰내역 페이지
	 * @param user 현재 로그인 중인 id
	 * @param page 현재 페이지 번호 (기본값: 0)
	 * @param size 페이지당 항목 수 (기본값: 10)
	 * @return 개인 리뷰내역 페이지
	 */
	@GetMapping("/myReview")
	public String viewMyReviews(@AuthenticationPrincipal AuthenticatedUser user,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "5") int size,
		@RequestParam(required = false) Integer rating,
		@RequestParam(defaultValue = "createdTime") String sortBy,
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

		// 페이지네이션을 적용한 리뷰 목록 조회
		Page<ReviewResponseDTO> myReviewPage = reviewService.getMyReviewsPageable(user.getId(),
			PageRequest.of(page, size), rating, sortBy);

		model.addAttribute("myReviewLists", myReviewPage.getContent());
		model.addAttribute("currentPage", myReviewPage.getNumber());
		model.addAttribute("totalPages", myReviewPage.getTotalPages());
		model.addAttribute("totalItems", myReviewPage.getTotalElements());
		model.addAttribute("size", size);

		return "reviewView/myReviews";
	}
}