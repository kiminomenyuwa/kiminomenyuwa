package com.scit45.kiminomenyuwa.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.BudgetEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Integer> {
	Optional<BudgetEntity> findByUserAndMonthAndYear(UserEntity user, Integer month, Integer year);

	Optional<BudgetEntity> findByUserAndYearAndMonth(UserEntity user, int year, int month);
}
