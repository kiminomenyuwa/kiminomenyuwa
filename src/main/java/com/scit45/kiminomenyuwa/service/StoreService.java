package com.scit45.kiminomenyuwa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreRegistrationDTO;
import com.scit45.kiminomenyuwa.domain.entity.MenuCategoryMappingEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.FoodCategoryRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuCategoryMappingRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class StoreService {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final MenuRepository menuRepository;
	private final MenuCategoryMappingRepository menuCategoryMappingRepository;
	private final FoodCategoryRepository foodCategoryRepository;

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

	public void saveMenu(MenuDTO menuDTO) {
		// 1. MenuEntity를 생성하고 필드 값을 설정
		MenuEntity menuEntity = MenuEntity.builder()
			.storeId(menuDTO.getStoreId())
			.name(menuDTO.getName())
			.price(menuDTO.getPrice())
			.pictureUrl(menuDTO.getPictureUrl())
			.enabled(menuDTO.getEnabled())
			.build();

		// 2. 메뉴 엔티티를 저장하여 menuId 생성
		MenuEntity savedMenu = menuRepository.save(menuEntity);

		// 3. 카테고리 매핑 생성
		List<MenuCategoryMappingEntity> categoryMappings = menuDTO.getCategories().stream()
			.map(categoryName -> {
				// 카테고리 이름을 기반으로 FoodCategory 엔티티를 조회
				var foodCategory = foodCategoryRepository.findByCategoryName(categoryName)
					.orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다: " + categoryName));

				// MenuCategoryMappingEntity 객체를 생성하여 메뉴와 카테고리 간 매핑을 저장
				MenuCategoryMappingEntity mapping = new MenuCategoryMappingEntity();
				mapping.setMenu(savedMenu);
				mapping.setFoodCategory(foodCategory);
				return mapping;
			})
			.collect(Collectors.toList());

		// 4. 카테고리 매핑 엔티티들을 저장
		menuCategoryMappingRepository.saveAll(categoryMappings);
	}
}
