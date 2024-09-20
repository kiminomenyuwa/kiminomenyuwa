package com.scit45.kiminomenyuwa.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.CategoryCountDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.MiniGameMenuRatingDTO;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.MiniGameMenuRatingEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuCategoryMappingRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.MiniGameMenuRatingRepository;

import jakarta.transaction.Transactional;
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
@Transactional
public class MiniGameService {

	// 메뉴 데이터베이스 접근을 위한 Repository 인스턴스.
	private final MenuRepository menuRepository;
	private final MiniGameMenuRatingRepository ratingRepository;
	private final MenuCategoryMappingRepository menuCategoryMappingRepository;
	private final MenuService menuService;

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

	/**
	 * DB에 저장된 모든 메뉴 중에서 랜덤으로 하나를 선택하여 반환합니다.
	 *
	 * @return 랜덤으로 선택된 메뉴의 DTO 객체
	 */
	public MenuDTO getRandomMenu() {
		// DB에서 모든 메뉴를 조회
		List<MenuEntity> menus = menuRepository.findAll();
		if (menus.isEmpty()) {
			log.warn("메뉴가 없습니다.");
			throw new RuntimeException("메뉴가 없습니다.");
		}

		// 랜덤으로 하나의 메뉴를 선택
		MenuEntity selectedMenu = menus.get(new Random().nextInt(menus.size()));
		log.info("선택된 메뉴: {}", selectedMenu.getName());

		// 선택한 메뉴를 DTO로 변환하여 반환
		return MenuDTO.builder()
			.menuId(selectedMenu.getMenuId())
			.name(selectedMenu.getName())
			.build();
	}

	/**
	 * 특정 메뉴에 대해 평가(점수)를 저장하는 메서드.
	 *
	 * @param userId  사용자 ID
	 * @param menuId  메뉴 ID
	 * @param rating  부여할 점수 (0 ~ 5)
	 */
	public void rateMenu(String userId, int menuId, float rating) {
		log.info("평가 저장 시작 - userId: {}, menuId: {}, rating: {}", userId, menuId, rating);

		// 평가 정보를 담은 엔티티 객체 생성
		MiniGameMenuRatingEntity ratingEntity = new MiniGameMenuRatingEntity();
		ratingEntity.setUserId(userId);
		ratingEntity.setMenuId(menuId);
		ratingEntity.setRating(rating);
		ratingEntity.setRatingDate(LocalDateTime.now());

		log.info("평가 엔티티 생성 완료 - {}", ratingEntity);

		// DB에 저장
		ratingRepository.save(ratingEntity);
		log.info("평가 저장 완료 - userId: {}, menuId: {}, rating: {}", userId, menuId, rating);
	}

	/**
	 * 사용자가 아직 평가하지 않은 메뉴 중에서 랜덤으로 하나를 선택하여 반환합니다.
	 *
	 * @param userId 사용자 ID
	 * @return 평가되지 않은 메뉴의 DTO 객체
	 */
	public MenuDTO getNextUnratedMenu(String userId) {
		// 평가되지 않은 메뉴 리스트를 DB에서 조회
		List<MenuEntity> unratedMenus = menuRepository.findUnratedMenusByUserId(userId);

		// 모든 메뉴가 평가된 경우 로그 메시지를 출력하고 null 반환
		if (unratedMenus.isEmpty()) {
			log.info("모든 메뉴가 평가되었습니다.");
			return null;
		}

		// 랜덤으로 메뉴 선택
		MenuEntity selectedMenu = unratedMenus.get(new Random().nextInt(unratedMenus.size()));

		log.info("선택된 메뉴: {}", selectedMenu.getName());

		// 선택된 메뉴를 DTO로 변환하여 반환
		return MenuDTO.builder()
			.menuId(selectedMenu.getMenuId())
			.name(selectedMenu.getName())
			.build();
	}

	/**
	 * 사용자의 미니게임 전체 점수 내역을 가져오는 메서드
	 * @param userId 현재 로그인 중인 userId
	 * @return 미니게임 점수 List
	 */
	public List<MiniGameMenuRatingDTO> getUsersMiniGameRatingAll(String userId) {
		// 별점 기준 내림차순으로 정렬
		Sort sort = Sort.by(Sort.Direction.DESC, "rating");

		List<MiniGameMenuRatingEntity> ratingEntities = ratingRepository.findByUserId(userId,sort);
        List<MiniGameMenuRatingDTO> miniGameRatingList = new ArrayList<>();
        for (MiniGameMenuRatingEntity entity : ratingEntities) {
			miniGameRatingList.add(MiniGameMenuRatingDTO.builder()
				.ratingId(entity.getRatingId())
                .userId(entity.getUserId())
                .menuId(entity.getMenuId())
                .rating(entity.getRating())
                .ratingDate(entity.getRatingDate())
                .build());
        }
        return miniGameRatingList;
	}

	/**
	 * 사용자의 미니게임 내역에서 카테고리 점수(횟수)를 도출
	 * @param userId 현재 로그인 중인 userId
	 * @return 미니게임 점수 배율에 따른 카테고리별 점수(횟수)List
	 */
	public List<CategoryCountDTO> getCategoryScoresByUserId(String userId) {
		// 1. 미니게임 내역을 불러옴
		Sort sort = Sort.by(Sort.Direction.DESC, "rating");
		List<MiniGameMenuRatingEntity> ratingEntities = ratingRepository.findByUserId(userId, sort);

		// 2. 카테고리별 점수 저장을 위한 Map 생성
		Map<String, Long> categoryScores = new HashMap<>();

		// 3. 각 미니게임 내역을 돌면서 카테고리 점수 계산
		for (MiniGameMenuRatingEntity entity : ratingEntities) {
			int rating = Math.round(entity.getRating()); // 평가 점수
			Integer menuId = entity.getMenuId();

			// 메뉴 ID로 해당 메뉴의 카테고리를 조회
			List<String> categories = menuCategoryMappingRepository.findCategoriesByMenuId(menuId);

			// 카테고리 점수를 평가 점수만큼 증가시킴
			for (String category : categories) {
				categoryScores.put(category, categoryScores.getOrDefault(category, 0L) + rating);
			}
		}

		// 4. Map을 List<CategoryCountDTO>로 변환
		List<CategoryCountDTO> categoryScoreList = new ArrayList<>();
		for (Map.Entry<String, Long> entry : categoryScores.entrySet()) {
			categoryScoreList.add(CategoryCountDTO.builder()
				.categoryName(entry.getKey())
				.categoryCount(entry.getValue())
				.build());
		}

		// 5. 내림차순으로 정렬 (점수가 높은 순서대로)
		categoryScoreList.sort((a, b) -> Long.compare(b.getCategoryCount(), a.getCategoryCount()));

		return categoryScoreList;
	}

	public List<MenuDTO> recommendMenusByCategoryScores(String userId) {
		// 1. 사용자 카테고리 점수 불러오기
		List<CategoryCountDTO> categoryScores = getCategoryScoresByUserId(userId);

		// 2. 모든 메뉴 불러오기
		List<MenuDTO> allMenus = menuService.getAllMenus();

		// 3. 카테고리 점수에 따른 메뉴 추천 리스트 생성
		List<MenuDTO> recommendedMenus = new ArrayList<>();

		// 4. 각 카테고리 점수별로 메뉴 필터링
		for (CategoryCountDTO categoryScore : categoryScores) {
			String categoryName = categoryScore.getCategoryName();

			// 해당 카테고리에 속하는 메뉴들만 필터링하여 추천 리스트에 추가
			for (MenuDTO menu : allMenus) {
				if (menu.getCategories().contains(categoryName)) {
					recommendedMenus.add(menu);
				}
			}
		}

		// 5. 중복 메뉴 제거 (카테고리가 여러 개 겹치는 메뉴를 한번만 추천)
		Set<Integer> uniqueMenuIds = new HashSet<>();
		List<MenuDTO> uniqueRecommendedMenus = new ArrayList<>();
		for (MenuDTO menu : recommendedMenus) {
			if (!uniqueMenuIds.contains(menu.getMenuId())) {
				uniqueMenuIds.add(menu.getMenuId());
				uniqueRecommendedMenus.add(menu);
			}
		}

		return uniqueRecommendedMenus;
	}

}
