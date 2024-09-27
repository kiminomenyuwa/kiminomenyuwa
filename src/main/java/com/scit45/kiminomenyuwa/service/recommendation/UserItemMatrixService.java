package com.scit45.kiminomenyuwa.service.recommendation;// UserItemMatrixService.java

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.entity.MiniGameMenuRatingEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;
import com.scit45.kiminomenyuwa.domain.repository.MiniGameMenuRatingRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserDiningHistoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class UserItemMatrixService {

	private final UserDiningHistoryRepository userDiningHistoryRepository;
	private final MiniGameMenuRatingRepository miniGameMenuRatingRepository;

	/**
	 * 사용자-아이템 행렬을 생성합니다.
	 *
	 * @return 사용자-아이템 평점 맵
	 */
	public Map<String, Map<Integer, Integer>> createUserItemMatrix() {
		Map<String, Map<Integer, Integer>> userItemMatrix = new HashMap<>();

		// 모든 유저의 식사 내역을 가져옵니다.
		List<UserDiningHistoryEntity> allDiningHistories = userDiningHistoryRepository.findAll();

		for (UserDiningHistoryEntity history : allDiningHistories) {
			String userId = history.getUser().getUserId();
			Integer menuId = history.getMenu().getMenuId();

			userItemMatrix.putIfAbsent(userId, new HashMap<>());
			// 식사 내역을 통해 메뉴를 먹었다는 의미로 1 점수 부여
			userItemMatrix.get(userId).put(menuId, userItemMatrix.get(userId).getOrDefault(menuId, 0) + 1);
		}

		// 모든 유저의 메뉴 평점을 가져옵니다.
		List<MiniGameMenuRatingEntity> allRatings = miniGameMenuRatingRepository.findAll();

		for (MiniGameMenuRatingEntity rating : allRatings) {
			String userId = rating.getUser().getUserId();
			Integer menuId = rating.getMenu().getMenuId();
			int score = Math.round(rating.getRating());

			userItemMatrix.putIfAbsent(userId, new HashMap<>());
			// 평점을 점수로 반영
			userItemMatrix.get(userId).put(menuId, userItemMatrix.get(userId).getOrDefault(menuId, 0) + score);
		}

		return userItemMatrix;
	}
}
