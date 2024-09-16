package com.scit45.kiminomenyuwa.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scit45.kiminomenyuwa.domain.dto.ReviewRequestDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewResponseDTO;
import com.scit45.kiminomenyuwa.domain.entity.ReviewEntity;
import com.scit45.kiminomenyuwa.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * 리뷰 작성 페이지를 보여주는 메서드
	 */
	@GetMapping("/new")
	public String showCreateReviewForm(@RequestParam("storeId") Integer storeId, Model model) {
		// 가게 ID를 DTO에 설정하여 뷰로 전달
		ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO();
		reviewRequestDTO.setStoreId(storeId);
		model.addAttribute("reviewRequestDTO", reviewRequestDTO);
		return "review/review-form"; // 리뷰 작성 폼을 보여줄 뷰 템플릿 이름
	}

	/**
	 * 리뷰와 사진을 함께 등록하는 메서드
	 */
	@PostMapping
	public String createReview(@ModelAttribute ReviewRequestDTO reviewRequestDTO
		, RedirectAttributes redirectAttributes) throws IOException {
		// 리뷰와 사진을 한 번에 저장
		ReviewResponseDTO savedReview = reviewService.saveReviewWithPhotos(reviewRequestDTO);
		redirectAttributes.addFlashAttribute("message", "리뷰가 저장되었습니다!");
		return "redirect:/reviews/" + savedReview.getReviewId();
	}

	// 리뷰 조회 메서드
	@GetMapping("/{reviewId}")
	public String getReview(@PathVariable Integer reviewId, Model model) {
		// 리뷰 ID를 기반으로 리뷰 조회
		ReviewResponseDTO review = reviewService.getReviewById(reviewId);

		// 조회된 리뷰 정보를 모델에 추가하여 뷰에 전달
		model.addAttribute("review", review);
		return "review/review-detail"; // 리뷰 상세 화면 뷰 템플릿 이름
	}

	/**
	 * 가게 ID로 리뷰 목록 조회
	 */
	@GetMapping("/store/{storeId}")
	public String getReviewsByStoreId(@PathVariable Integer storeId, Model model
		, @AuthenticationPrincipal UserDetails userDetails) {
		// 가게 ID로 리뷰 목록 조회
		List<ReviewEntity> reviews = reviewService.getReviewsByStoreId(storeId);
		model.addAttribute("reviews", reviews); // 조회된 리뷰 목록을 모델에 추가
		return "review/review-list"; // 리뷰 목록을 보여줄 뷰 템플릿 이름
	}
}