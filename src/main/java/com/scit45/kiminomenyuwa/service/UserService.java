package com.scit45.kiminomenyuwa.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scit45.kiminomenyuwa.domain.dto.UserDTO;
import com.scit45.kiminomenyuwa.domain.entity.ProfilePhotoEntity;
import com.scit45.kiminomenyuwa.domain.entity.Role;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.ProfilePhotoRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 유저
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	//회원정보 DB처리
	private final UserRepository userRepository;
	//프로필 사진 DB처리
	private final ProfilePhotoRepository profilePhotoRepository;

	//TODO 비밀번호 암호화(security)
	//private final BCryptPasswordEncoder passwordEncoder;

	/**
	 * 회원가입 처리 메서드
	 * @param dto joinForm.html에서 받은 회원가입 정보
	 */
	public void join(UserDTO dto) {
		UserEntity entity = UserEntity.builder()
			.userId(dto.getUserId())
			.passwordHash(dto.getPasswordHash())
			//TODO .passwordHash(passwordEncoder.encode(dto.getPasswordHash()))
			.name(dto.getName())
			.birthDate(dto.getBirthDate())
			.gender(dto.getGender())
			.email(dto.getEmail())
			.roadNameAddress(dto.getRoadNameAddress())
			.detailAddress(dto.getDetailAddress())
			.zipcode(dto.getZipcode())
			.phoneNumber(dto.getPhoneNumber())
			//TODO 프로필사진 .profilePhotoUrl(dto.getProfilePhotoUrl())
			.role(Role.ROLE_USER) //기본회원 등급 ROLE_USER
			.enabled(true)
			.build();

		//리포지토리로 회원정보 DB에 저장
		userRepository.save(entity);

		//회원가입성공 로그
		log.info("User {} joined successfully", dto.getUserId());
	}

	/**
	 * ID 존재여부 확인
	 * @param searchId : idCheckForm.html - PostMapping("idCheck") - 여기
	 * @return ID존재시 false, 없으면 true return
	 */
	public boolean checkIdNotExist(String searchId) {

		return !userRepository.existsById(searchId);
	}

	/**
	 * 프로필 사진 저장 메서드 : 파일명 UUID 변환 및 PhotoEntity에 저장
	 * @param profileImage joinForm.html에서 받은 이미지 파일
	 * @param userId joinForm.html에서 입력하고 있는 사용자 ID
	 * @param uploadPath application.properties에서 설정한 파일 저장 경로
	 */
	public void saveProfileImage(MultipartFile profileImage, String userId, String uploadPath) {
		ProfilePhotoEntity profilePhotoEntity = new ProfilePhotoEntity();

		//프사 원래 파일명을 UUID로 변환
		String originalName = profileImage.getOriginalFilename();
		String extension = originalName.substring(originalName.lastIndexOf("."));
		String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String uuidString = UUID.randomUUID().toString();
		String transformedFileName = dateString + "_" + uuidString + extension;

		// 파일 저장 처리
		try {
			File file = new File(uploadPath, transformedFileName);
			profileImage.transferTo(file);

			// 엔티티에 원래파일명, 프로필 사진을 저장하려는 userId, UUID로 변환된 파일명 저장
			profilePhotoEntity.setOriginalName(originalName);
			profilePhotoEntity.setUserId(userId);
			profilePhotoEntity.setSavedName(transformedFileName);

			profilePhotoRepository.save(profilePhotoEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}