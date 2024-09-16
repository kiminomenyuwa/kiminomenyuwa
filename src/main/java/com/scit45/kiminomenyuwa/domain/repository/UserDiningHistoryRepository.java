package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.UserDiningHistoryEntity;

@Repository
public interface UserDiningHistoryRepository extends JpaRepository<UserDiningHistoryEntity, Integer> {
	//현재 로그인 중인 userId의 먹은 음식 내역
	List<UserDiningHistoryEntity> findByUserId(String userId);

	//음식 내역중 중복으로 먹은 내역 제거
	@Query("SELECT DISTINCT udh.menuId FROM UserDiningHistoryEntity udh WHERE udh.userId = :userId")
	List<Long> findDistinctMenuIdsByUserId(@Param("userId") String userId);
}