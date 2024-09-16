package com.scit45.kiminomenyuwa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserDiningHistoryRepository;

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

	/**
	 * 현재 로그인 중인 사용자의 음식 먹은 내역을 List로 가져오는 메서드
	 * @param userId 현재 로그인 중인 유져의 userId
	 * @return 음식내역 List
	 */
	public List<UserDiningHistoryDTO> getDiningHistory(String userId) {
		//먹은 음식 내역 태이블에서 현재 로그인 중인 id로 select
		List<UserDiningHistoryEntity> diningHistoryEntities = userDiningHistoryRepository.findByUserId(userId);

		//불러온 내역 EntityList를 dtoList로 변환
		List<UserDiningHistoryDTO> diningHistoryDTOs = new ArrayList<>();
		for (UserDiningHistoryEntity dining : diningHistoryEntities) {
			diningHistoryDTOs.add(UserDiningHistoryDTO.builder()
				.diningId(dining.getDiningId())
				.userId(dining.getUserId())
				.menuId(dining.getMenuId())
				.diningDate(dining.getDiningDate())
				.build());
		}
		//현재 로그인한 사용자의 먹은 음식내역 DTOList를 리턴
		return diningHistoryDTOs;
	}

	/**
	 * 현재 로그인 중인 사용자의 먹은 내역 중 중복이 제거된 menuId만 출력
	 * @param userId 로그인 중인 사용자의 id
	 * @return 중복이 제거된 음식 내역의 menuId들
	 */
	public List<Long> getDistinctDiningHistory(String userId) {
		List<Long> distinctDiningHistory = userDiningHistoryRepository.findDistinctMenuIdsByUserId(userId);

		return distinctDiningHistory;
	}

	/**
	 * 사용자가 먹지 않은 메뉴 리스트를 가져오는 메서드
	 * @param userId 로그인 중인 사용자의 id
	 * @return 사용자가 먹지 않은 메뉴 리스트
	 */
	public List<MenuDTO> getMenusNotTried(String userId) {
		// 사용자가 먹은 메뉴 ID 리스트를 가져오기
		List<Long> eatenMenuIds = userDiningHistoryRepository.findDistinctMenuIdsByUserId(userId);

		// 사용자가 먹지 않은 메뉴들을 MenuRepository에서 가져오기
		List<MenuEntity> availableMenus = menuRepository.findMenusNotInMenuIds(eatenMenuIds);

		// MenuEntity 리스트를 MenuDTO 리스트로 변환하여 반환
		List<MenuDTO> menuDTOs = new ArrayList<>();
		for (MenuEntity menuEntity : availableMenus) {
			menuDTOs.add(MenuDTO.builder()
				.menuId(menuEntity.getMenuId())
				.storeId(menuEntity.getStoreId())
				.name(menuEntity.getName())
				.price(menuEntity.getPrice())
				.pictureUrl(menuEntity.getPictureUrl())
				.enabled(menuEntity.getEnabled())
				.build());
		}

		return menuDTOs;
	}
}
