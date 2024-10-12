package com.scit45.kiminomenyuwa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO;
import com.scit45.kiminomenyuwa.domain.dto.FoodCategoryDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuCategoryMappingRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserDiningHistoryRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserDiningHistoryService {
	private final MenuRepository menuRepository;
	private final UserDiningHistoryRepository userDiningHistoryRepository;
	private final MenuCategoryMappingRepository menuCategoryMappingRepository;
	private final UserRepository userRepository;

	/**
	 * 현재 로그인 중인 사용자의 음식 먹은 내역을 List로 가져오는 메서드
	 * @param userId 현재 로그인 중인 유져의 userId
	 * @return 음식내역 List
	 */
	public List<UserDiningHistoryDTO> getDiningHistory(String userId) {
		//먹은 음식 내역 태이블에서 현재 로그인 중인 id로 select
		List<UserDiningHistoryEntity> diningHistoryEntities = userDiningHistoryRepository.findByUser_UserId(userId);

		//불러온 내역 EntityList를 dtoList로 변환
		List<UserDiningHistoryDTO> diningHistoryDTOs = new ArrayList<>();
		for (UserDiningHistoryEntity dining : diningHistoryEntities) {
			diningHistoryDTOs.add(UserDiningHistoryDTO.builder()
				.diningId(dining.getDiningId())
				.userId(dining.getUser().getUserId())
				.menuId(dining.getMenu().getMenuId())
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
	public List<Integer> getDistinctDiningHistory(String userId) {
		return userDiningHistoryRepository.findDistinctMenuIdsByUserId(userId);
	}

	/**
	 * 사용자가 먹지 않은 메뉴 리스트를 가져오는 메서드
	 * @param userId 로그인 중인 사용자의 id
	 * @return 사용자가 먹지 않은 메뉴 리스트
	 */
	public List<MenuDTO> getMenusNotTried(String userId, List<MenuDTO> menus) {
		// 사용자가 먹은 메뉴 ID 리스트를 가져오기
		List<Integer> eatenMenuIds = userDiningHistoryRepository.findDistinctMenuIdsByUserId(userId);

		// 사용자가 먹지 않은 메뉴들을 MenuRepository에서 가져오기
		// -> 인근 가게들의 메뉴 목록에서 필터링 하도록 수정
		// List<MenuEntity> availableMenus = menuRepository.findMenusNotInMenuIds(eatenMenuIds);
		List<MenuDTO> availableMenus = menus.stream()
			.filter(e -> !eatenMenuIds.contains(e.getMenuId()))
			.toList();

		// MenuEntity 리스트를 MenuDTO 리스트로 변환하여 반환
		List<MenuDTO> menuDTOs = new ArrayList<>();
		for (MenuDTO menu : availableMenus) {
			// 각 메뉴에 해당하는 카테고리 리스트 가져오기
			List<FoodCategoryDTO> categories = menuCategoryMappingRepository.findCategoriesWithTypeByMenuId(
					menu.getMenuId())
				.stream().map(e -> FoodCategoryDTO.builder()
					.categoryId(e.getFoodCategory().getCategoryId())
					.typeId(e.getFoodCategory().getCategoryType().getTypeId())
					.categoryName(e.getFoodCategory().getCategoryName())
					.build()).toList();

			// MenuDTO에 카테고리 리스트를 추가하여 생성
			menuDTOs.add(MenuDTO.builder()
				.menuId(menu.getMenuId())
				.storeId(menu.getStoreId())
				.name(menu.getName())
				.price(menu.getPrice())
				.pictureUrl(menu.getPictureUrl())
				.enabled(menu.getEnabled())
				.categories(categories) // 카테고리 리스트 추가
				.build());
		}

		return menuDTOs;
	}

	/**
	 * 사용자가 먹은 음식들의 카테고리 중 중복되는 TOP10 키워드
	 * @param userId 현재 로그인 중인 ID
	 * @return 사용자가 먹은 음식들의 카테고리 중 중복되는 TOP10 키워드 리스트
	 */
	public List<CategoryCountDTO> getTopCategoriesByUserId(String userId) {
		return userDiningHistoryRepository.findTopCategoriesByUserId(userId)
			.stream()
			.limit(10)
			.collect(Collectors.toList());
	}

	/**
	 * 사용자가 먹은 음식 내역을 저장하는 메서드
	 * @param loggedInUserId 현재 로그인중인 UserId
	 * @param foodName 음식의 이름
	 */
	public void saveDiningHistory(String loggedInUserId, String foodName) {
		MenuEntity menu = menuRepository.findByName(foodName);
		UserEntity user = userRepository.findById(loggedInUserId)
			.orElseThrow(() -> new EntityNotFoundException("해당 Id의 유져가 없습니다 : " + loggedInUserId));

		UserDiningHistoryEntity userDiningHistory = UserDiningHistoryEntity.builder()
			.user(user)
			.menu(menu)
			.diningDate(java.time.LocalDateTime.now())
			.build();
		userDiningHistoryRepository.save(userDiningHistory);
	}
}
