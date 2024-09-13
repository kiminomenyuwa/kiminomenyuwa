package com.scit45.kiminomenyuwa.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
 * 유저(계정 관련) 서비스
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	//회원정보 DB처리
	private final UserRepository userRepository;

	//비밀번호 암호화(security)
	private final BCryptPasswordEncoder passwordEncoder;

	/**
	 * 회원가입 처리 메서드
	 * @param dto joinForm.html에서 받은 회원가입 정보
	 */
	public void join(UserDTO dto) {
		UserEntity entity = UserEntity.builder()
			.userId(dto.getUserId())
			.passwordHash(passwordEncoder.encode(dto.getPasswordHash()))
			.name(dto.getName())
			.birthDate(dto.getBirthDate())
			.gender(dto.getGender())
			.email(dto.getEmail())
			.roadNameAddress(dto.getRoadNameAddress())
			.detailAddress(dto.getDetailAddress())
			.zipcode(dto.getZipcode())
			.phoneNumber(dto.getPhoneNumber())
			.profileImgUuid(dto.getProfileImgUuid())
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

}