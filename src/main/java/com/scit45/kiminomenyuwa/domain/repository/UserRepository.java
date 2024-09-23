package com.scit45.kiminomenyuwa.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	// 사용자 ID가 존재하는지 확인하는 메서드
	boolean existsByUserId(String userId);

	// 유저 정보 조회 메서드
	Optional<UserEntity> findByUserId(String userId);
}