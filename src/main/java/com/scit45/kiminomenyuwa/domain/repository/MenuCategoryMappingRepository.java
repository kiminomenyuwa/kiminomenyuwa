package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.MenuCategoryMappingEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;

@Repository
public interface MenuCategoryMappingRepository extends JpaRepository<MenuCategoryMappingEntity, Integer> {
	// 메뉴 ID에 해당하는 카테고리 리스트를 가져오는 메서드
	@Query("SELECT mcm.foodCategory.categoryName FROM MenuCategoryMappingEntity mcm WHERE mcm.menu.menuId = :menuId")
	List<String> findCategoriesByMenuId(@Param("menuId") Integer menuId);

	List<MenuCategoryMappingEntity> findByMenu(MenuEntity menu);

	List<MenuCategoryMappingEntity> findByMenu_MenuId(Integer menu_menuId);
}