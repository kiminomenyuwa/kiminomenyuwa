package com.scit45.kiminomenyuwa.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.service.StoreSearchService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("stores")
@Controller
public class StoreSearchController {

	private final StoreSearchService storeSearchService;

	/**
	 * 검색 페이지를 렌더링합니다.
	 *
	 * @param model 모델 객체
	 * @return 검색 페이지 템플릿 이름
	 */
	@GetMapping("/search")
	public String showSearchForm(Model model) {
		model.addAttribute("searchRequest", new SearchRequest());
		return "store/search/store-search";
	}

	@GetMapping("/keywordsearch")
	public String keywordSearch() {
		return "store/search/store-keywordsearch";
	}

	/**
	 * 검색 요청을 처리하고 결과를 렌더링합니다.
	 *
	 * @param searchRequest 검색 요청 데이터
	 * @param result        바인딩 결과
	 * @param model         모델 객체
	 * @return 검색 결과 페이지 템플릿 이름
	 */
	@PostMapping("/search")
	public String searchStores(@ModelAttribute("searchRequest") SearchRequest searchRequest, BindingResult result,
		Model model) {
		if (result.hasErrors()) {
			return "store/search/store-search";
		}

		List<StoreResponseDTO> stores = storeSearchService.getStoresNearby(searchRequest.getLatitude(),
			searchRequest.getLongitude(), searchRequest.getRadius());

		model.addAttribute("stores", stores);
		return "store/search/store-results";
	}

	/**
	 * 검색 요청 데이터 클래스
	 */
	@Data
	public static class SearchRequest {
		Double latitude;
		Double longitude;
		Double radius; // 미터 단위
	}

	/**
	 * 키워드로 상점을 검색합니다.
	 *
	 * @param keyword 검색 키워드
	 * @return 상점 목록 DTO
	 */
	@GetMapping("api/stores/keywordsearch")
	public ResponseEntity<Page<StoreResponseDTO>> searchStoresByKeyword(@RequestParam("keyword") String keyword,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<StoreResponseDTO> stores = storeSearchService.getStoresByName(keyword, pageable);
		return ResponseEntity.ok(stores);
	}
}
