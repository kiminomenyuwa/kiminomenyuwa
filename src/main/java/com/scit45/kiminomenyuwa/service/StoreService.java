package com.scit45.kiminomenyuwa.service;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.StoreRegistrationDTO;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreService {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;

	/**
	 * 등록 정보를 받아서 storeRepository에 저장한다
	 * @param storeRegistrationDTO
	 */
	public void saveStore(StoreRegistrationDTO storeRegistrationDTO) {

		StoreEntity storeEntity = convertToEntity(storeRegistrationDTO);

		// 엔티티를 DB에 저장
		storeRepository.save(storeEntity);
	}

	/**
	 * dto를 entity로 변경해주는 메소드
	 * @param storeRegistrationDTO 작성한 가게 등록 정보
	 * @return 작성한 가게 정보를 entity로 변경된 정보를 리턴
	 */
	private StoreEntity convertToEntity(StoreRegistrationDTO storeRegistrationDTO) {

		UserEntity user = userRepository.findById(storeRegistrationDTO.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다: " + storeRegistrationDTO.getUserId()));
		return StoreEntity.builder()
			.user(user)
			.name(storeRegistrationDTO.getName())
			.certification(storeRegistrationDTO.getCertification())
			.roadNameAddress(storeRegistrationDTO.getRoadNameAddress())
			.detailAddress(storeRegistrationDTO.getDetailAddress())
			.zipcode(storeRegistrationDTO.getZipcode())
			.phoneNumber(storeRegistrationDTO.getPhoneNumber())
			.category(storeRegistrationDTO.getCategory())
			.description(storeRegistrationDTO.getDescription())
			.enabled(storeRegistrationDTO.getEnabled())
			.build();
	}

}
