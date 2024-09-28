package com.scit45.kiminomenyuwa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scit45.kiminomenyuwa.domain.dto.CategoryTypeDTO;
import com.scit45.kiminomenyuwa.domain.dto.FoodCategoryDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.store.StoreRegistrationDTO;
import com.scit45.kiminomenyuwa.service.StoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	/**
	 * 사장님 페이지로 이동
	 *
	 * @return
	 */
	@GetMapping("storePage")
	public String storePage() {
		return "store/storePage";
	}

	// // 가게 리스트 페이지를 반환하는 메소드
	// @ResponseBody
	// @GetMapping("/api/stores")
	// public List<StoreRegistrationDTO> showStoreList(Authentication user) {
	//     // 서비스에서 가게 리스트를 가져와서 모델에 추가
	//     List<StoreRegistrationDTO> storeList = storeService.getAllStores();
	//
	//     return storeList;
	// }

	/**
	 * 가게 이름 불러오기
	 *
	 * @param storeId
	 * @return
	 */
	@ResponseBody
	@GetMapping("/api/store/{storeId}")
	public StoreRegistrationDTO getStoreName(@PathVariable("storeId")
	Integer storeId) {
		String storeName = storeService.getStoreNameById(storeId);
		log.debug(storeName);
		return StoreRegistrationDTO.builder()
			.name(storeName)
			.build();
	}

	/**
	 * @param storeId
	 * @return 지정 가게에 등록된 메뉴 리스트 가져옴
	 */
	@ResponseBody
	@GetMapping("/api/store/{storeId}/menus")
	public ResponseEntity<List<MenuDTO>> getMenusByStoreId(@PathVariable("storeId")
	Integer storeId) {
		List<MenuDTO> menus = storeService.getMenusByStoreId(storeId);
		log.debug(menus.toString());
		return ResponseEntity.ok(menus);
	}

	/**
	 * @return 카테고리 타입을 불러옴
	 */
	@ResponseBody
	@GetMapping("/api/category-types")
	public List<CategoryTypeDTO> getCategoryTypes() {
		return storeService.getAllCategoryTypes();
	}

	// 카테고리 검색 API
	@ResponseBody
	@GetMapping("/api/search-categories")
	public List<FoodCategoryDTO> searchCategories(@RequestParam("query") String query) {
		return storeService.searchCategories(query);
	}

	@ResponseBody
	@GetMapping("/api/categories")
	public List<FoodCategoryDTO> getCategories(@RequestParam("typeId") Integer typeId) {
		return storeService.getAllCategories(typeId);
	}
}
