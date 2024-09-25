package com.scit45.kiminomenyuwa.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.UserDTO;
import com.scit45.kiminomenyuwa.domain.entity.Role;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 유저(계정 관련) 서비스
 * 사용자 등록, 조회 및 ID 확인 등의 계정 관련 기능을 처리하는 서비스 클래스
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	// UserRepository: 회원 정보와 관련된 DB 작업을 처리하는 리포지토리
	private final UserRepository userRepository;

	// BCryptPasswordEncoder: 비밀번호 암호화를 담당하는 객체
	private final BCryptPasswordEncoder passwordEncoder;

	/**
	 * 회원가입 처리 메서드
	 *
	 * @param dto 회원가입 양식에서 전달된 정보를 담은 UserDTO 객체
	 */
	public void join(UserDTO dto) {
		// UserEntity 객체 생성, 회원 정보와 권한을 설정
		UserEntity entity = UserEntity.builder()
			.userId(dto.getUserId()) // 사용자 ID 설정
			.passwordHash(passwordEncoder.encode(dto.getPasswordHash())) // 비밀번호를 암호화하여 저장
			.name(dto.getName()) // 사용자 이름 설정
			.birthDate(dto.getBirthDate()) // 생년월일 설정
			.gender(dto.getGender()) // 성별 설정
			.email(dto.getEmail()) // 이메일 설정
			.roadNameAddress(dto.getRoadNameAddress()) // 도로명 주소 설정
			.detailAddress(dto.getDetailAddress()) // 상세 주소 설정
			.zipcode(dto.getZipcode()) // 우편번호 설정
			.phoneNumber(dto.getPhoneNumber()) // 전화번호 설정
			.profileImgUuid(dto.getProfileImgUuid()) // 프로필 이미지 UUID 설정
			.role(Role.ROLE_USER) // 기본 회원 등급으로 설정 (ROLE_USER)
			.enabled(true) // 계정 활성화 여부 (true로 설정하여 계정을 활성화)
			.build();

		// 생성한 UserEntity 객체를 DB에 저장
		userRepository.save(entity);

		// 회원가입 성공 시 로그 기록
		log.info("User {} joined successfully", dto.getUserId());
	}

	/**
	 * ID 존재 여부 확인 메서드
	 *
	 * @param searchId 확인할 사용자 ID
	 * @return 해당 ID가 존재하지 않으면 true, 존재하면 false
	 */
	public boolean checkIdNotExist(String searchId) {
		// existsById 메서드를 사용해 ID가 존재하는지 확인, 반대로 결과를 반환
		return !userRepository.existsById(searchId);
	}

	/**
	 * 사용자 ID로 사용자 정보 조회 메서드
	 *
	 * @param userId 조회할 사용자 ID
	 * @return 해당 사용자 ID로 조회된 UserEntity 객체(Optional로 감싸져 있음)
	 */
	public Optional<UserEntity> findByUserId(String userId) {
		// 사용자 ID를 기반으로 사용자 정보를 조회
		return userRepository.findByUserId(userId);
	}
}
