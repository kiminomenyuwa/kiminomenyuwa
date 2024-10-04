package com.scit45.kiminomenyuwa.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.scit45.kiminomenyuwa.domain.dto.receipt.ReceiptDTO;
import com.scit45.kiminomenyuwa.domain.dto.receipt.ReceiptUploadResponseDTO;
import com.scit45.kiminomenyuwa.service.MyPageService;
import com.scit45.kiminomenyuwa.service.UserDiningHistoryService;
import com.scit45.kiminomenyuwa.service.verification.ReceiptRecognitionService;
import com.scit45.kiminomenyuwa.service.verification.ReceiptVerificationService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

	private final ReceiptRecognitionService receiptRecognitionService;
	private final ReceiptVerificationService receiptVerificationService;
	private final UserDiningHistoryService userDiningHistoryService;

	/**
	 * 영수증 업로드 페이지 표시
	 */
	@GetMapping("/upload")
	public String showUploadPage() {
		return "receipt/upload"; // 템플릿 경로 변경
	}

	/**
	 * 영수증 업로드 처리 (AJAX)
	 */
	@PostMapping("/uploadAjax")
	@ResponseBody
	public ResponseEntity<?> handleUploadAjax(
		@RequestParam("receiptFile") MultipartFile receiptFile,
		HttpSession session) { // 세션 객체를 추가합니다.
		if (receiptFile.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("파일을 선택하지 않았습니다.");
		}

		try {
			// 영수증 분석
			String jsonResult = receiptRecognitionService.analyzeReceipt(receiptFile);
			ReceiptDTO receiptDTO = receiptRecognitionService.parseReceiptJson(jsonResult);
			log.debug(receiptDTO.toString());

			// 현재 로그인된 사용자 정보 가져오기
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String loggedInUserId = authentication.getName();

			// 세션에 영수증 정보 저장
			session.setAttribute("uploadedReceipt", receiptDTO);  // 영수증 정보를 세션에 저장

			// 영수증 인증
			boolean isVerified = receiptVerificationService.verifyReceipt(receiptDTO, loggedInUserId);

			if (isVerified) {
				return ResponseEntity.ok(new ReceiptUploadResponseDTO(true, "영수증 인증이 완료되었습니다.", receiptDTO));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ReceiptUploadResponseDTO(false, "영수증 인증에 실패했습니다.", receiptDTO));
			}

		} catch (IOException e) {
			log.error("영수증 분석 중 오류 발생: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("영수증 분석 중 오류가 발생했습니다.");
		} catch (IllegalArgumentException e) {
			log.error("영수증 인증 오류: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(e.getMessage());
		} catch (Exception e) {
			log.error("영수증 처리 중 오류 발생: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("영수증 처리 중 오류가 발생했습니다.");
		}
	}
}
