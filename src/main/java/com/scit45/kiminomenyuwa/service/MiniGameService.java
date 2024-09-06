package com.scit45.kiminomenyuwa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * 메뉴와 관련된 비즈니스 로직을 처리하는 서비스 클래스.
 * 메뉴 데이터를 조회하여 DTO로 변환한 후, 호출자에게 전달하는 역할을 수행합니다.
 */
@Service
@RequiredArgsConstructor
@Builder
public class MiniGameService {

	// 메뉴 데이터베이스 접근을 위한 Repository 인스턴스.
	private final MenuRepository menuRepository;

	/**
	 * 모든 메뉴를 조회하고, DTO 리스트로 변환하여 반환하는 메서드.
	 *
	 * @return DB에서 조회한 모든 MenuDTO 객체의 리스트
	 */
	public List<MenuDTO> getAllMenus() {
		// DB에서 모든 MenuEntity 객체를 조회합니다.
		List<MenuEntity> menuEntities = menuRepository.findAll();

		// 조회된 엔티티 리스트를 DTO 리스트로 변환합니다.
		List<MenuDTO> menuDTOs = new ArrayList<>();
		for (MenuEntity menuEntity : menuEntities) {
			// MenuEntity를 MenuDTO로 변환하여 리스트에 추가합니다.
			menuDTOs.add(MenuDTO.builder()
				.menuId(menuEntity.getMenuId())
				.storeId(menuEntity.getStoreId())
				.name(menuEntity.getName())
				.price(menuEntity.getPrice())
				.pictureUrl(menuEntity.getPictureUrl())
				.enabled(menuEntity.getEnabled())
				.build());
		}

		// 변환된 DTO 리스트를 호출자에게 반환합니다.
		return menuDTOs;
	}
}
