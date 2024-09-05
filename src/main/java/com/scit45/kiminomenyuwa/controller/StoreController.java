package com.scit45.kiminomenyuwa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
