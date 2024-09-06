package com.scit45.kiminomenyuwa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

	@GetMapping("storeRegistration")
	public String storeRegistration() {

		return "store/storeRegistration";
	}

	@ResponseBody
	@PostMapping("storeRegistration")
	public void registerStore(
		StoreRegistrationDTO storeRegistrationDTO) {
		log.debug("전달된 dto객체: {}", storeRegistrationDTO);
		storeService.saveStore(storeRegistrationDTO);
	}

}
