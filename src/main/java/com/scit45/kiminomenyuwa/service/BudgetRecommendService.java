package com.scit45.kiminomenyuwa.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.entity.BudgetEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.BudgetRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserDiningHistoryRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BudgetRecommendService {

	private final MenuRepository menuRepository;
	private final UserRepository userRepository;
	private final BudgetRepository budgetRepository;
	private final UserDiningHistoryRepository userDiningHistoryRepository;

	/**
	 * 남은 예산과 남은 일수를 기반으로 추천 메뉴를 조회하는 메서드입니다.
	 *
	 * @param budget 남은 예산
	 * @param days   남은 일수
	 * @return 추천 메뉴 목록
	 */
	public List<MenuDTO> getRecommendedMenus(int budget, int days) {
		List<MenuEntity> affordableMenus = menuRepository.findByPriceLessThanEqual(budget);

		// 예산과 일수에 따라 추천 이유를 설정하는 로직을 추가합니다.
		return affordableMenus.stream().map(menu -> {
			return MenuDTO.builder()
				.menuId(menu.getMenuId())
				.name(menu.getName())
				.price(menu.getPrice())
				.build();
		}).collect(Collectors.toList());
	}

	/**
	 * 특정 사용자와 특정 달에 대한 남은 예산을 계산하는 메서드입니다.
	 *
	 * @param userId 사용자 ID
	 * @param year   연도
	 * @param month  월
	 * @return 남은 예산
	 */
	public int calculateRemainingBudget(String userId, int year, int month) {
		// 사용자 조회
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

		// 예산 조회
		Optional<BudgetEntity> budgetOpt = budgetRepository.findByUserAndYearAndMonth(user, year, month);
		if (!budgetOpt.isPresent()) {
			throw new RuntimeException("해당 연도와 월의 예산을 찾을 수 없습니다.");
		}
		BudgetEntity budget = budgetOpt.get();
		int initialBudget = budget.getBudget();

		// 해당 달의 시작과 끝 날짜 계산
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

		// 해당 달에 사용한 총 금액 계산
		List<UserDiningHistoryEntity> diningHistories = userDiningHistoryRepository.findByUserAndDiningDateBetween(user, start, end);
		int totalSpent = diningHistories.stream()
			.mapToInt(dh -> dh.getMenu().getPrice())
			.sum();

		// 남은 예산 계산
		int remainingBudget = initialBudget - totalSpent;
		log.debug("초기 예산: {}, 총 사용 금액: {}, 남은 예산: {}", initialBudget, totalSpent, remainingBudget);
		return remainingBudget;
	}
}
