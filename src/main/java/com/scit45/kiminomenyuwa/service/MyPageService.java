package com.scit45.kiminomenyuwa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserDiningHistoryRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MyPageService {

	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;
	private final UserDiningHistoryRepository userDiningHistoryRepository;
	private final UserRepository userRepository;

	public List<MenuDTO> getMenusByStoreName(String storeName) {
		// 상점 이름으로 상점 찾기
		StoreEntity store = storeRepository.findByName(storeName);
		if (store == null) {
			throw new RuntimeException("Store not found");
		}
		// 상점 ID로 메뉴 찾기
		List<MenuEntity> menus = menuRepository.findByStoreId(store.getStoreId());
		log.debug("메뉴: {}", menus.toString());

		// MenuEntity를 MenuDTO로 변환
		return menus.stream()
			.map(menu -> new MenuDTO(menu.getMenuId(), menu.getName(), menu.getPrice(), menu.getPictureUrl()))
			.collect(Collectors.toList());
	}

	public void saveDiningHistory(UserDiningHistoryDTO userDiningHistoryDTO) {
		// userId와 menuId로 각각의 엔티티를 조회
		UserEntity user = userRepository.findById(userDiningHistoryDTO.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
		MenuEntity menu = menuRepository.findById(userDiningHistoryDTO.getMenuId())
			.orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));

		// 빌더 패턴을 사용하여 엔티티 생성
		UserDiningHistoryEntity diningHistoryEntity = UserDiningHistoryEntity.builder()
			.user(user)
			.menu(menu)
			.diningDate(userDiningHistoryDTO.getDiningDate())
			.build();
		log.debug("UserDiningHistoryEntity: {}", diningHistoryEntity);

		// 엔티티 저장
		userDiningHistoryRepository.save(diningHistoryEntity);
	}

	public MenuDTO getMenuById(int menuId) {
		MenuEntity menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new EntityNotFoundException("Menu not found with id: " + menuId));
		return MenuDTO.builder().menuId(menu.getMenuId()).name(menu.getName()).price(menu.getPrice()).build();
	}

	public List<UserDiningHistoryDTO> getDiningHistoryByUserId(String userId) {
		return userDiningHistoryRepository.findByUser_UserId(userId)
			.stream()
			.map(history -> UserDiningHistoryDTO.builder()
				.diningId(history.getDiningId())
				.userId(history.getUser().getUserId())
				.menuId(history.getMenu().getMenuId())
				.diningDate(history.getDiningDate())
				.build())
			.collect(Collectors.toList());
	}
}
