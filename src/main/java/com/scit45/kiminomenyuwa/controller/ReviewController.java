package com.scit45.kiminomenyuwa.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewRequestDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreRegistrationDTO;
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
    @GetMapping("/write")
    public String showWriteReviewPage(Model model) {
        List<StoreRegistrationDTO> stores = storeService.getAllStores();
        List<MenuDTO> menus = menuService.getAllMenus();
        model.addAttribute("stores", stores);
        model.addAttribute("menus", menus);
        model.addAttribute("reviewDTO", new ReviewRequestDTO());
        return "review/write_review";
    }

    /**
     * 리뷰 제출 처리
     */
    @PostMapping("/write")
    public String submitReview(@ModelAttribute ReviewRequestDTO reviewDTO, Model model,
        RedirectAttributes redirectAttributes) {
        // Spring Security를 통해 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName(); // 사용자 ID 또는 username

        try {
            reviewService.saveReviewWithPhotos(reviewDTO, loggedInUserId);
            redirectAttributes.addFlashAttribute("successMessage", "리뷰가 성공적으로 작성되었습니다.");
            return "redirect:/reviews/write";
        } catch (Exception e) {
            log.error("리뷰 작성 중 오류 발생: {}", e.getMessage());
            model.addAttribute("errorMessage", "리뷰 작성 중 오류가 발생했습니다.");
            // 다시 리뷰 작성 페이지로 이동하면서 기존 데이터 유지
            List<StoreRegistrationDTO> stores = storeService.getAllStores();
            List<MenuDTO> menus = menuService.getAllMenus();
            model.addAttribute("stores", stores);
            model.addAttribute("menus", menus);
            return "review/write_review";
        }
    }
}