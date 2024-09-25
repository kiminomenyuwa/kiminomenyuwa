package com.scit45.kiminomenyuwa.controller;// RecommendationController.java

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.recommendation.CollaborativeFilteringService;
import com.scit45.kiminomenyuwa.service.recommendation.ContentBasedFilteringService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("recommendations")
@Controller
public class RecommendationController {

	private final CollaborativeFilteringService collaborativeFilteringService;
	private final ContentBasedFilteringService contentBasedFilteringService;

	@GetMapping
	public String getRecommendations(Model model, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		String userId = authenticatedUser.getUsername(); // 스프링 시큐리티 사용 시 유저 ID 가져오는 방법
		int limit = 10;

		// 협업 필터링 기반 추천
		List<MenuEntity> collaborativeMenus = collaborativeFilteringService.recommendMenus(userId, limit);

		// 콘텐츠 기반 필터링 기반 추천
		List<MenuEntity> contentBasedMenus = contentBasedFilteringService.recommendMenus(userId, limit);

		// 두 추천 리스트를 합치고 중복 제거
		List<MenuEntity> combinedMenus = Stream.concat(collaborativeMenus.stream(), contentBasedMenus.stream())
			.distinct()
			.limit(limit)
			.collect(Collectors.toList());

		model.addAttribute("recommendedMenus", combinedMenus);

		return "recommendView/recommendations"; // Thymeleaf 템플릿 이름
	}
}
