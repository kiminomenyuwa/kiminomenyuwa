package com.scit45.kiminomenyuwa.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.BudgetDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.UserDiningHistoryDTO;
import com.scit45.kiminomenyuwa.domain.entity.BudgetEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.BudgetRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserDiningHistoryRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;

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
	private final BudgetRepository budgetRepository;

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
			.map(menu -> new MenuDTO(menu.getMenuId(), menu.getName(), menu.getPrice()))
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

	/**
	 * 예산을 저장하는 메서드입니다.
	 *
	 * @param budgetDTO 저장할 예산 정보
	 */
	public void saveBudget(BudgetDTO budgetDTO) {
		// 사용자 정보 조회
		UserEntity user = userRepository.findById(budgetDTO.getUserId())
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

		// 예산 조회
		Optional<BudgetEntity> existingBudgetOpt = budgetRepository.findByUserAndMonthAndYear(user,
			budgetDTO.getMonth(), budgetDTO.getYear());

		BudgetEntity budgetEntity;
		if (existingBudgetOpt.isPresent()) {
			// 기존 예산 업데이트
			budgetEntity = existingBudgetOpt.get();
			budgetEntity.setBudget(budgetDTO.getBudget());
		} else {
			// 새로운 예산 생성
			budgetEntity = new BudgetEntity();
			budgetEntity.setUser(user);
			budgetEntity.setMonth(budgetDTO.getMonth());
			budgetEntity.setYear(budgetDTO.getYear());
			budgetEntity.setBudget(budgetDTO.getBudget());
		}

		// 예산 저장
		budgetRepository.save(budgetEntity);
	}

	/**
	 * 특정 연도와 월의 예산을 조회하거나, 존재하지 않으면 이전 달의 남은 예산으로 초기화
	 *
	 * @param userId 사용자 ID
	 * @param year 연도
	 * @param month 월
	 * @return 예산 정보
	 */
	public BudgetDTO getRemainingBudget(String userId, int year, int month) {
		// 사용자 정보 조회
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

		// 해당 연도와 월의 예산 조회
		Optional<BudgetEntity> budgetOpt = budgetRepository.findByUserAndMonthAndYear(userEntity, month, year);
		if (budgetOpt.isPresent()) {
			BudgetEntity budgetEntity = budgetOpt.get();
			return BudgetDTO.builder()
				.budgetId(budgetEntity.getBudgetId())
				.userId(userId)
				.month(budgetEntity.getMonth())
				.year(budgetEntity.getYear())
				.budget(budgetEntity.getBudget())
				.build();
		} else {
			// 새로운 월의 예산을 설정 (고정 값)
			int defaultBudget = 200000; // 원하는 기본 예산 금액

			BudgetEntity newBudget = new BudgetEntity();
			newBudget.setUser(userEntity);
			newBudget.setMonth(month);
			newBudget.setYear(year);
			newBudget.setBudget(defaultBudget); // 기본 예산 설정

			budgetRepository.save(newBudget);

			return BudgetDTO.builder()
				.budgetId(newBudget.getBudgetId())
				.userId(userId)
				.month(newBudget.getMonth())
				.year(newBudget.getYear())
				.budget(newBudget.getBudget())
				.build();
		}
	}
}
