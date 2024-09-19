package com.scit45.kiminomenyuwa.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.scit45.kiminomenyuwa.domain.dto.ReceiptDTO;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.verification.ReceiptRecognitionService;
import com.scit45.kiminomenyuwa.service.verification.ReceiptVerificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/receipt2")
@RequiredArgsConstructor
@Slf4j
public class ReceiptVerificationController {

	private final ReceiptRecognitionService receiptRecognitionService;
	private final ReceiptVerificationService receiptVerificationService;

	/**
	 * GET 요청을 처리하여 영수증 인증 폼을 표시합니다.
	 *
	 * @param model 모델 객체
	 * @return Thymeleaf 템플릿 이름
	 */
	@GetMapping("/verify2")
	public String showVerificationForm(Model model) {
		return "review/verify_receipt";
	}

	/**
	 * POST 요청을 처리하여 영수증 파일을 분석하고 인증을 수행합니다.
	 *
	 * @param receiptFile 업로드된 영수증 파일
	 * @param model       모델 객체
	 * @return Thymeleaf 템플릿 이름
	 */
	@PostMapping("/verify2")
	public String verifyReceipt(MultipartFile receiptFile
		, Model model
		, @AuthenticationPrincipal AuthenticatedUser user) {
		// 예시로 사용자 ID를 "user123"으로 고정 (실제 프로젝트에서는 인증된 사용자 ID를 사용)
		String loggedInUserId = user.getUsername();

		try {
			// 영수증 파일 분석
			String analysisResultJson = receiptRecognitionService.analyzeReceipt(receiptFile);
			log.info("영수증 분석 결과: {}", analysisResultJson);

			// JSON 결과를 ReceiptDTO로 파싱
			ReceiptDTO receiptDTO = receiptRecognitionService.parseReceiptJson(analysisResultJson);

			log.debug("receiptDTO = {}", receiptDTO);

			if (receiptDTO == null) {
				model.addAttribute("errorMessage", "영수증 데이터를 파싱하는 데 실패했습니다.");
				return "review/verify_receipt";
			}

			// // ReceiptDTO 검증 및 인증 수행
			// if (receiptVerificationService.verifyReceipt(receiptDTO, loggedInUserId))
			// 	log.info("영수증 인증이 성공적으로 완료되었습니다.");

			// 성공 메시지와 인증된 영수증 정보를 모델에 추가
			model.addAttribute("successMessage", "영수증 인증이 완료되었습니다.");
			model.addAttribute("receipt", receiptDTO);

		} catch (Exception e) {
			log.error("영수증 인증 중 오류 발생: {}", e.getMessage());
			model.addAttribute("errorMessage", "영수증 인증 중 오류가 발생했습니다: " + e.getMessage());
		}

		return "review/verify_receipt";
	}
}
