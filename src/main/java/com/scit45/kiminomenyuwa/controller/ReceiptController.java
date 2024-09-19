package com.scit45.kiminomenyuwa.controller;

import com.scit45.kiminomenyuwa.domain.dto.ReceiptDTO;
import com.scit45.kiminomenyuwa.service.verification.ReceiptRecognitionService;
import com.scit45.kiminomenyuwa.service.verification.ReceiptVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptRecognitionService receiptRecognitionService;
    private final ReceiptVerificationService receiptVerificationService;

    /**
     * 영수증 업로드 페이지 표시
     */
    @GetMapping("/upload")
    public String showUploadPage() {
        return "receipt/upload"; // 템플릿 경로 변경
    }

    /**
     * 영수증 업로드 처리
     */
    @PostMapping("/upload")
    public String handleUpload(MultipartFile receiptFile, RedirectAttributes redirectAttributes, Model model) {
        if (receiptFile.isEmpty()) {
            model.addAttribute("errorMessage", "파일을 선택하지 않았습니다.");
            return "receipt/upload"; // 템플릿 경로 변경
        }

        try {
            // 영수증 분석
            String jsonResult = receiptRecognitionService.analyzeReceipt(receiptFile);
            ReceiptDTO receiptDTO = receiptRecognitionService.parseReceiptJson(jsonResult);

            // 인증 페이지로 데이터 전달
            model.addAttribute("receiptDTO", receiptDTO);
            return "receipt/confirm"; // 템플릿 경로 변경
        } catch (IOException e) {
            log.error("영수증 분석 중 오류 발생: {}", e.getMessage());
            model.addAttribute("errorMessage", "영수증 분석 중 오류가 발생했습니다.");
            return "receipt/upload"; // 템플릿 경로 변경
        } catch (Exception e) {
            log.error("영수증 처리 중 오류 발생: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "receipt/upload"; // 템플릿 경로 변경
        }
    }

    /**
     * 영수증 정보 확인 및 인증 완료 처리
     */
    @PostMapping("/confirm")
    public String confirmReceipt(ReceiptDTO receiptDTO, Model model, RedirectAttributes redirectAttributes) {
        // Spring Security를 통해 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName(); // 사용자 ID 또는 username

        try {
            boolean isVerified = receiptVerificationService.verifyReceipt(receiptDTO, loggedInUserId);
            if (isVerified) {
                redirectAttributes.addFlashAttribute("successMessage", "영수증 인증이 완료되었습니다.");
                return "redirect:/reviews/write";
            } else {
                model.addAttribute("errorMessage", "영수증 인증에 실패했습니다.");
                return "receipt/confirm"; // 템플릿 경로 변경
            }
        } catch (IllegalArgumentException e) {
            log.error("영수증 인증 오류: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "receipt/confirm"; // 템플릿 경로 변경
        }
    }
}
