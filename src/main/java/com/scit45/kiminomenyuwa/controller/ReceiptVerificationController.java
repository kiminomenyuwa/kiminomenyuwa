package com.scit45.kiminomenyuwa.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scit45.kiminomenyuwa.domain.dto.ReceiptDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.verification.ReceiptRecognitionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("receipt")
@Controller
public class ReceiptVerificationController {
	private final ReceiptRecognitionService receiptRecognitionService;

	// 영수증 인증 페이지 로드
	@GetMapping("uploadForm")
	public String showReceiptVerificationPage(@RequestParam("storeId") Long storeId
		, @AuthenticationPrincipal AuthenticatedUser user
		, Model model
		, RedirectAttributes redirectAttributes) {

		// 로그인되지 않은 경우, 홈으로 리다이렉트
		if (user == null) {
			redirectAttributes.addFlashAttribute("error", "로그인 후 이용 가능합니다.");
			return "redirect:/";  // 홈으로 리다이렉트
		}

		// 영수증 인증 페이지에 필요한 정보 설정
		model.addAttribute("storeId", storeId);
		model.addAttribute("username", user.getUsername());

		return "verificationView/receipt-upload-form";
	}

	@PostMapping("recognize")
	public String recognizeReceipt(@RequestParam("receiptImage") MultipartFile file
		, Model model) {
		try {
			// 1. API로 결과를 받아오는 메소드 호출
			String analyzeResult = receiptRecognitionService.analyzeReceipt(file);
			// 2. 결과를 파싱하여 필요한 정보를 추출하는 메소드 호출
			ReceiptDTO receipt = receiptRecognitionService.parseReceiptJson(analyzeResult);
			model.addAttribute("receipt", receipt);
			return "verificationView/receipt-recognition-result";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "파일 업로드 중 오류가 발생했습니다.");
			return "error";
		}
	}
}
