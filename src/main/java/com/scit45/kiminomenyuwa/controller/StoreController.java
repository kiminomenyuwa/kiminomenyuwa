package com.scit45.kiminomenyuwa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreRegistrationDTO;
import com.scit45.kiminomenyuwa.service.StoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	/**
	 * 가게 등록 페이지로 이동
	 * @return
	 */
	@GetMapping("storeRegistration")
	public String storeRegistration() {

		return "store/storeRegistration";
	}

	/**
	 * 가게 등록정보를 Repository에 저장
	 * @param storeRegistrationDTO
	 */
	@ResponseBody
	@PostMapping("storeRegistration")
	public ResponseEntity<StoreRegistrationDTO> registerStore(
		@RequestBody
		StoreRegistrationDTO storeRegistrationDTO) {
		log.debug("전달된 dto객체: {}", storeRegistrationDTO);
		storeService.saveStore(storeRegistrationDTO);
		return ResponseEntity.ok().body(storeRegistrationDTO);
	}

	// 가게 등록 후 페이지로 이동
	@GetMapping("/merchantsPage")
	public String merchantsPage() {
		return "store/merchantsPage"; // templates/store/merchantsPage.html 파일로 렌더링
	}

	@GetMapping("menuRegistration")
	public String menuRegistration() {
		return "store/menuRegistration";
	}

	/**
	 * 새로운 메뉴를 등록하는 엔드포인트
	 * 
	 * @param menuDTO 클라이언트에서 전달된 메뉴 등록 정보
	 * @return 성공 여부에 대한 응답
	 */
	@ResponseBody
	@PostMapping("menuRegistration")
	public ResponseEntity<MenuDTO> registerMenu(@RequestBody
	MenuDTO menuDTO) {
		// 전달된 DTO 객체 로그 출력
		log.debug("전달된 메뉴 DTO 객체: {}", menuDTO);

		// 서비스 계층을 통해 메뉴 저장 로직을 실행
		storeService.saveMenu(menuDTO);

		// 성공적으로 저장된 DTO를 응답으로 반환
		return ResponseEntity.ok().body(menuDTO);
	}

}
