package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.FavoriteEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Integer> {
	// 특정 사용자가 특정 상점을 찜했는지 확인
	Optional<FavoriteEntity> findByUserAndStore(UserEntity user, StoreEntity store);

	// 특정 사용자가 찜한 모든 상점 조회
	List<FavoriteEntity> findByUser(UserEntity user);
}
