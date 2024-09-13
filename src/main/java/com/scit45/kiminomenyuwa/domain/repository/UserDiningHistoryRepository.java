package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;

@Repository
public interface UserDiningHistoryRepository extends JpaRepository<UserDiningHistoryEntity, Integer> {
	List<UserDiningHistoryEntity> findByUserId(String userId);
}