package com.scit45.kiminomenyuwa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.FoodCategoryDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreDTO;
import com.scit45.kiminomenyuwa.domain.entity.FoodCategoryEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserDiningHistoryRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 메뉴 관련 서비스
 * 전체 메뉴 목록, 사용자가 먹은 이력
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
	private final MenuRepository menuRepository;
	private final UserDiningHistoryRepository userDiningHistoryRepository;

	/**
	 * 모든 메뉴를 조회하고, DTO 리스트로 변환하여 반환하는 메서드.
	 *
	 * @return DB에서 조회한 모든 MenuDTO 객체의 리스트
	 */
	public List<MenuDTO> getAllMenus() {
		// DB에서 모든 MenuEntity 객체를 조회
		List<MenuEntity> menuEntities = menuRepository.findAll();

		// 조회된 엔티티 리스트를 DTO 리스트로 변환
		List<MenuDTO> menuDTOs = new ArrayList<>();
		for (MenuEntity menuEntity : menuEntities) {
			// 각 메뉴의 카테고리 리스트 가져오기
			List<FoodCategoryDTO> categories = menuEntity.getCategoryMappings().stream()
				.map(mapping -> FoodCategoryDTO
					.builder()
					.categoryId(mapping.getFoodCategory().getCategoryId())
					.categoryName(mapping.getFoodCategory().getCategoryName())
					.typeId(mapping.getFoodCategory().getCategoryType().getTypeId())
					.build())
				.collect(Collectors.toList());

			// MenuEntity를 MenuDTO로 변환하여 리스트에 추가
			menuDTOs.add(MenuDTO.builder()
				.menuId(menuEntity.getMenuId())
				.storeId(menuEntity.getStore().getStoreId())
				.name(menuEntity.getName())
				.price(menuEntity.getPrice())
				.pictureUrl(menuEntity.getPictureUrl())
				.enabled(menuEntity.getEnabled())
				.categories(categories) // 카테고리 리스트 추가
				.build());
		}

		// 변환된 DTO 리스트를 호출자에게 반환
		return menuDTOs;
	}

	public List<MenuDTO> getMenusNotTried(String userId) {
		List<Long> eatenMenuIds = userDiningHistoryRepository.findDistinctMenuIdsByUserId(userId);

		// 메뉴와 카테고리 정보를 함께 가져오기
		List<Object[]> menuWithCategories = menuRepository.findMenusWithCategoriesNotInMenuIds(eatenMenuIds);

		List<MenuDTO> menuDTOs = new ArrayList<>();
		for (Object[] row : menuWithCategories) {
			MenuEntity menuEntity = (MenuEntity)row[0];
			String categories = (String)row[1];

			// MenuEntity를 MenuDTO로 변환하고 카테고리 리스트 추가
			MenuDTO menuDTO = MenuDTO.builder()
				.menuId(menuEntity.getMenuId())
				.storeId(menuEntity.getStore().getStoreId())
				.name(menuEntity.getName())
				.price(menuEntity.getPrice())
				.pictureUrl(menuEntity.getPictureUrl())
				.enabled(menuEntity.getEnabled())
//				.categories(Arrays.asList(categories.split(", "))) // 카테고리 추가
				.build();

			menuDTOs.add(menuDTO);
		}
		return menuDTOs;
	}

	/**
	 * 특정 가게 ID에 속한 모든 메뉴를 조회하여 MenuDTO 리스트로 반환
	 *
	 * @param storeId 가게 ID
	 * @return 메뉴 DTO 리스트
	 */
	public List<MenuDTO> findMenusByStoreId(Integer storeId) {
		List<MenuEntity> menuEntities = menuRepository.findByStore_StoreId(storeId);

		return menuEntities.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	public MenuDTO findById(Integer menuId) {
		return convertToDTO(menuRepository.findById(menuId)
			.orElseThrow(() -> new EntityNotFoundException("menuId : {}" + menuId + "가 존재하지 않습니다.")));
	}

	/**
	 * MenuEntity를 MenuDTO로 변환하는 메서드
	 *
	 * @param menuEntity 메뉴 엔티티
	 * @return 메뉴 DTO
	 */
	private MenuDTO convertToDTO(MenuEntity menuEntity) {
		List<FoodCategoryDTO> foodCategories = menuEntity.getCategoryMappings().stream()
			.map(mapping -> {
				FoodCategoryEntity category = mapping.getFoodCategory();
				return FoodCategoryDTO.builder()
					.categoryId(category.getCategoryId())
					.categoryName(category.getCategoryName())
					.typeId(category.getCategoryType().getTypeId())
					.build();
			})
			.collect(Collectors.toList());

		return MenuDTO.builder()
			.menuId(menuEntity.getMenuId())
			.storeId(menuEntity.getStore().getStoreId())
			.name(menuEntity.getName())
			.price(menuEntity.getPrice())
			.pictureUrl(menuEntity.getPictureUrl())
			.enabled(menuEntity.getEnabled())
			.categories(foodCategories)
			.build();
	}

	// StoreEntity를 StoreDTO로 변환하는 메서드
	public StoreDTO convertToStoreDTO(StoreEntity storeEntity) {
		// Geometry에서 좌표 정보 추출 (JTS 라이브러리 사용)
		double latitude = storeEntity.getLocation().getCoordinate().getY();
		double longitude = storeEntity.getLocation().getCoordinate().getX();

		return StoreDTO.builder()
			.storeId(storeEntity.getStoreId())
			.name(storeEntity.getName())
			.roadNameAddress(storeEntity.getRoadNameAddress())
			.detailAddress(storeEntity.getDetailAddress())
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}

	// MenuEntity를 MenuDTO로 변환하는 메서드
	public MenuDTO convertToMenuDTO(MenuEntity menuEntity) {
		StoreDTO storeDTO = convertToStoreDTO(menuEntity.getStore());

		return MenuDTO.builder()
			.menuId(menuEntity.getMenuId())
			.name(menuEntity.getName())
			.price(menuEntity.getPrice())
			.pictureUrl(menuEntity.getPictureUrl())
			.enabled(menuEntity.getEnabled())
			.store(storeDTO) // MenuDTO에 StoreDTO 추가
			.build();
	}

}
