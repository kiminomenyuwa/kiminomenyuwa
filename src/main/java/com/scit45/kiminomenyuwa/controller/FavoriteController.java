package com.scit45.kiminomenyuwa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

	private final FavoriteService favoriteService;

	/**
	 * 가게를 찜하는 API
	 *
	 * @param storeId 찜할 가게의 ID
	 * @return 성공 메시지 또는 오류 메시지
	 */
	@PostMapping("/{storeId}")
	public ResponseEntity<String> addFavorite(@PathVariable Integer storeId) {
		try {
			String userId = getCurrentUserId();
			favoriteService.addFavorite(userId, storeId);
			return ResponseEntity.ok("찜 목록에 추가되었습니다.");
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
		}
	}

	/**
	 * 가게의 찜을 취소하는 API
	 *
	 * @param storeId 찜을 취소할 가게의 ID
	 * @return 성공 메시지 또는 오류 메시지
	 */
	@DeleteMapping("/{storeId}")
	public ResponseEntity<String> removeFavorite(@PathVariable Integer storeId) {
		try {
			String userId = getCurrentUserId();
			favoriteService.removeFavorite(userId, storeId);
			return ResponseEntity.ok("찜이 취소되었습니다.");
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
		}
	}

	/**
	 * 사용자의 찜 목록을 조회하는 API
	 *
	 * @return 찜한 가게 목록 또는 오류 메시지
	 */
	@GetMapping
	public ResponseEntity<?> getFavorites() {
		try {
			String userId = getCurrentUserId();
			List<StoreEntity> favorites = favoriteService.getFavorites(userId);
			return ResponseEntity.ok(favorites);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
		}
	}

	/**
	 * 현재 인증된 사용자의 ID를 반환하는 헬퍼 메소드
	 *
	 * @return 현재 사용자의 ID
	 * @throws IllegalStateException 인증되지 않은 사용자일 경우
	 */
	private String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() ||
			authentication.getPrincipal().equals("anonymousUser")) {
			throw new IllegalStateException("인증된 사용자가 아닙니다.");
		}
		return authentication.getName();
	}
}
