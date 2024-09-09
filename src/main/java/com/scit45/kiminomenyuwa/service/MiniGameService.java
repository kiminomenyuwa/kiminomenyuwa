package com.scit45.kiminomenyuwa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 메뉴와 관련된 비즈니스 로직을 처리하는 서비스 클래스.
 * 메뉴 데이터를 조회하여 DTO로 변환한 후, 호출자에게 전달하는 역할을 수행합니다.
 */
@Service
@RequiredArgsConstructor
@Builder
@Slf4j
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

	/**
	 * DB에 저장된 모든 메뉴의 개수를 반환하는 메서드.
	 *
	 * @return 메뉴의 총 개수
	 */
	public long countAllMenus() {
		log.debug("DB에서 조회된 메뉴 개수: {}", menuRepository.count());
		return menuRepository.count();
	}

	/**
	 * 특정 ID에 해당하는 메뉴를 조회하여 DTO로 변환한 후 반환하는 메서드.
	 *
	 * @param menuId 조회할 메뉴의 ID
	 * @return 조회된 MenuDTO 객체
	 * @throws RuntimeException 메뉴가 존재하지 않는 경우 예외 발생
	 */
	public MenuDTO getMenuById(int menuId) {
		// ID에 해당하는 메뉴를 DB에서 조회
		Optional<MenuEntity> menuEntity = menuRepository.findById(menuId);
		if (menuEntity.isPresent()) {
			// 조회된 MenuEntity를 MenuDTO로 변환하여 반환
			MenuEntity menu = menuEntity.get();
			return MenuDTO.builder()
				.menuId(menu.getMenuId())
				.storeId(menu.getStoreId())
				.name(menu.getName())
				.price(menu.getPrice())
				.pictureUrl(menu.getPictureUrl())
				.enabled(menu.getEnabled())
				.build();
		} else {
			// 메뉴가 존재하지 않는 경우 예외 발생
			throw new RuntimeException("메뉴를 찾을 수 없습니다. menu_id: " + menuId);
		}
	}
}
