package com.scit45.kiminomenyuwa.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.scit45.kiminomenyuwa.domain.dto.ProfilePhotoDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDTO;
import com.scit45.kiminomenyuwa.domain.entity.ProfilePhotoEntity;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.ProfilePhotoService;
import com.scit45.kiminomenyuwa.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원 관련 컨트롤러 (회원가입, 로그인)
 */
@Slf4j
@Controller
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final ProfilePhotoService profilePhotoService;

	//프로필 사진 저장 경로
	@Value("${profile.uploadPath}")
	String uploadPath;

	/**
	 * 로그인 페이지로 이동
	 * @return 로그인 폼으로 이동
	 */
	@GetMapping("loginForm")
	public String loginForm() {

		return "/userView/loginForm";
	}

	/**
	 * 회원가입 양식으로 이동
	 * @return 회원가입 폼으로 이동
	 */
	@GetMapping("joinForm")
	public String joinForm() {

		return "/userView/joinForm";
	}

	/**
	 * 회원가입 처리
	 * @param user joinForm.html에서 받은 회원 정보
	 * @return 홈페이지로 리다이렉트
	 */
	@PostMapping("join")
	public String join(@ModelAttribute UserDTO user) {
		log.debug("전달된 회원정보 : {}", user);

		userService.join(user);

		return "redirect:/";
	}

	/**
	 * ID 중복확인 폼
	 * @return idCheck.html
	 */
	@GetMapping("idCheck")
	public String idCheck() {

		return "/userView/idCheck";
	}

	/**
	 * ID 중복확인 처리메서드
	 * @param searchId idCheck.html에서 받은 사용자 아이디
	 * @param model
	 * @return
	 */
	@PostMapping("idCheck")
	public String idCheck2(@RequestParam("searchId") String searchId, Model model) {
		// ID 중복확인 폼에서 전달된 검색할 아이디를 받아서 log 출력
		log.debug("전달받은 ID : {}", searchId);
		// 서비스의 메서드로 검색할 아이디를 전달받아서 조회
		// 해당 아이디를 쓰는 회원이 있으면 false, 없으면 true 리턴받음
		boolean result = userService.checkIdNotExist(searchId);
		// 검색한 아이디와 조회결과를 모델에 저장
		model.addAttribute("searchId", searchId);
		model.addAttribute("result", result);
		// 검색한 페이지로 다시 이동
		return "/userView/idCheck";
	}

	/**
	 * 프로필 사진 저장
	 * @param profileImage 저장하려는 프로필 사진
	 * @param userId joinForm.html에서 입력중인 사용자ID
	 */
	@ResponseBody
	@PostMapping("uploadProfile")
	public ProfilePhotoDTO uploadProfile(
		@RequestParam("profileImage") MultipartFile profileImage
		, @RequestParam("userId") String userId) {
		log.debug("프사 저장하려는 id : {}", userId);

		//업로드 요청 받은 프로필사진 정보
		if (profileImage != null) {
			log.debug("파일 존재여부 : {}", profileImage.isEmpty());
			log.debug("파라미터 이름 : {}", profileImage.getName());
			log.debug("파일의 이름 : {}", profileImage.getOriginalFilename());
			log.debug("크기 : {}", profileImage.getSize());
			log.debug("파일 종류 : {}", profileImage.getContentType());
		}
		//프로필 사진 저장 처리
		profilePhotoService.saveProfileImage(profileImage, userId, uploadPath);

		//저장된 프로필 사진의 정보 불러오기 (변환된 UUID를 사용하기 위함)
		ProfilePhotoDTO profilePhotoDTO = profilePhotoService.getUserProfilePhotoInfo(userId);

		//joinForm.html에 프로필 사진 UUID를 리턴하기 위한 객체
		return profilePhotoDTO;
	}
}