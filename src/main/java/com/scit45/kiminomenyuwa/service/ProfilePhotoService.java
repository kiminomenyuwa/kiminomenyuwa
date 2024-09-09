package com.scit45.kiminomenyuwa.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scit45.kiminomenyuwa.domain.dto.ProfilePhotoDTO;
import com.scit45.kiminomenyuwa.domain.entity.ProfilePhotoEntity;
import com.scit45.kiminomenyuwa.domain.repository.ProfilePhotoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프로필 사진 관련 서비스
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfilePhotoService {
	//프로필 사진 DB처리
	private final ProfilePhotoRepository profilePhotoRepository;

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

	/**
	 * 프사 엔티티를 DTO로 바꿔주는 메서드
	 * @param entity 프사 엔티티
	 * @return 프사 디티오
	 */
	private ProfilePhotoDTO convertEntityToDTO(ProfilePhotoEntity entity) {
		return ProfilePhotoDTO.builder()
                .userId(entity.getUserId())
                .originalName(entity.getOriginalName())
                .savedName(entity.getSavedName())
				.uploadDate(entity.getUploadDate())
                .build();
	}

	/**
	 * 프로필 사진 불러오기 메서드
	 * @param userId 요청한 사용자의 ID
	 * @return 사용자 프로필 사진의 DTO
	 */
	public ProfilePhotoDTO getUserProfilePhotoInfo(String userId) {
		//userId로 저장된 프로필 사진 불러옴
		ProfilePhotoEntity profilePhotoEntity = profilePhotoRepository.searchByUserId(userId);
		//불러온 엔티티를 DTO로 변환
		ProfilePhotoDTO profilePhotoDTO = convertEntityToDTO(profilePhotoEntity);

		return profilePhotoDTO;
	}
}
