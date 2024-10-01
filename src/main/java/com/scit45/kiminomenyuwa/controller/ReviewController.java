package com.scit45.kiminomenyuwa.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scit45.kiminomenyuwa.domain.dto.ReviewRequestDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.MenuService;
import com.scit45.kiminomenyuwa.service.ReviewService;
import com.scit45.kiminomenyuwa.service.StoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final StoreService storeService;
	private final MenuService menuService;
	private final ReviewService reviewService;

	/**
	 * 리뷰 작성 페이지 표시
	 */
	@GetMapping("/write/{storeId}")
	public String showWriteReviewPage(@PathVariable("storeId") Integer storeId, Model model) {
		model.addAttribute("reviewDTO", new ReviewRequestDTO());
		return "reviewView/write_review";
	}

	/**
	 * 리뷰 제출 처리
	 */
	@PostMapping("/write")
	@ResponseBody
	public ResponseEntity<Map<String, String>> submitReview(
		@ModelAttribute ReviewRequestDTO reviewDTO
		, @RequestParam("storeId") Integer storeId
		, @AuthenticationPrincipal AuthenticatedUser user
		, Model model,
		RedirectAttributes redirectAttributes) {

		String loggedInUserId = user.getId();// 사용자 ID 또는 username

		Map<String, String> response = new HashMap<>();
		try {
			reviewService.saveReviewWithPhotos(reviewDTO, loggedInUserId);
			response.put("redirect", "/stores/" + storeId);
			response.put("message", "리뷰가 저장되었습니다.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("리뷰 작성 중 오류 발생: {}", e.getMessage());
			model.addAttribute("errorMessage", "리뷰 작성 중 오류가 발생했습니다.");
			// 다시 리뷰 작성 페이지로 이동하면서 기존 데이터 유지
			model.addAttribute("reviewRequestDTO", reviewDTO);
			response.put("redirect", "/stores/" + storeId);
			return ResponseEntity.badRequest().body(response);
		}
	}
}