package com.scit45.kiminomenyuwa.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.verification.PurchasedMenuEntity;

@Repository
public interface PurchasedMenuRepository extends JpaRepository<PurchasedMenuEntity, Long> {
}
