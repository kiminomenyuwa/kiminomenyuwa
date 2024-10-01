// DiscountRepository.java
package com.scit45.kiminomenyuwa.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.DiscountEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Integer> {
	DiscountEntity findByMenu(MenuEntity menu);

	void deleteByMenu_MenuId(Integer menuId);
}
