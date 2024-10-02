// DiscountRepository.java
package com.scit45.kiminomenyuwa.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.DiscountEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Integer> {
	// 할인 정보를 메뉴 ID로 조회하는 메서드 정의
	DiscountEntity findByMenu_MenuId(Integer menuId);

	// 메뉴 ID로 할인 정보 삭제
	DiscountEntity findByMenu(MenuEntity menu);

	void deleteByMenu_MenuId(Integer menuId);
}
