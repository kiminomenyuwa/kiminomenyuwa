package com.scit45.kiminomenyuwa.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
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
			// MenuEntity를 MenuDTO로 변환하여 리스트에 추가
			menuDTOs.add(MenuDTO.builder()
				.menuId(menuEntity.getMenuId())
				.storeId(menuEntity.getStoreId())
				.name(menuEntity.getName())
				.price(menuEntity.getPrice())
				.pictureUrl(menuEntity.getPictureUrl())
				.enabled(menuEntity.getEnabled())
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
				.storeId(menuEntity.getStoreId())
				.name(menuEntity.getName())
				.price(menuEntity.getPrice())
				.pictureUrl(menuEntity.getPictureUrl())
				.enabled(menuEntity.getEnabled())
				.categories(Arrays.asList(categories.split(", ")))  // 카테고리 추가
				.build();

			menuDTOs.add(menuDTO);
		}

		return menuDTOs;
	}

	public MenuDTO getMenuById(int menuId) {
		MenuEntity menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new EntityNotFoundException("Menu not found with id: " + menuId));
		return MenuDTO.builder().menuId(menu.getMenuId()).name(menu.getName()).price(menu.getPrice()).build();
	}
}
