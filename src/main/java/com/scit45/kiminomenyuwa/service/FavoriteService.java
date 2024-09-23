package com.scit45.kiminomenyuwa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scit45.kiminomenyuwa.domain.entity.FavoriteEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.FavoriteRepository;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;

	/**
	 * 사용자가 상점을 찜하는 기능
	 *
	 * @param userId  찜할 사용자의 ID
	 * @param storeId 찜할 상점의 ID
	 * @throws IllegalArgumentException 사용자가 존재하지 않거나 상점이 존재하지 않을 경우
	 * @throws IllegalStateException    이미 찜한 상점일 경우
	 */
	@Transactional
	public void addFavorite(String userId, Integer storeId) {
		// 사용자와 상점 엔티티를 데이터베이스에서 조회
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
		StoreEntity store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("해당 상점이 존재하지 않습니다."));

		// 이미 찜한 상점인지 확인
		if (favoriteRepository.findByUserAndStore(user, store).isPresent()) {
			throw new IllegalStateException("이미 찜한 상점입니다.");
		}

		// 찜 엔티티 생성 및 저장
		FavoriteEntity favorite = new FavoriteEntity(user, store);

		favoriteRepository.save(favorite);
	}

	/**
	 * 사용자가 상점의 찜을 취소하는 기능
	 *
	 * @param userId  찜을 취소할 사용자의 ID
	 * @param storeId 찜을 취소할 상점의 ID
	 * @throws IllegalArgumentException 사용자가 존재하지 않거나 상점이 존재하지 않을 경우
	 * @throws IllegalStateException    찜하지 않은 상점일 경우
	 */
	@Transactional
	public void removeFavorite(String userId, Integer storeId) {
		// 사용자와 상점 엔티티를 데이터베이스에서 조회
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
		StoreEntity store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("해당 상점이 존재하지 않습니다."));

		// 찜 엔티티 조회
		FavoriteEntity favorite = favoriteRepository.findByUserAndStore(user, store)
			.orElseThrow(() -> new IllegalStateException("찜하지 않은 상점입니다."));

		// 찜 엔티티 삭제
		favoriteRepository.delete(favorite);
	}

	/**
	 * 사용자가 찜한 상점 목록을 조회하는 기능
	 *
	 * @param userId 찜 목록을 조회할 사용자의 ID
	 * @return 사용자가 찜한 상점 목록
	 * @throws IllegalArgumentException 사용자가 존재하지 않을 경우
	 */
	@Transactional(readOnly = true)
	public List<StoreEntity> getFavorites(String userId) {
		// 사용자 엔티티를 데이터베이스에서 조회
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

		// 찜 목록 조회
		List<FavoriteEntity> favorites = favoriteRepository.findByUser(user);

		// 찜한 상점 목록 반환
		return favorites.stream()
			.map(FavoriteEntity::getStore)
			.collect(Collectors.toList());
	}

}
