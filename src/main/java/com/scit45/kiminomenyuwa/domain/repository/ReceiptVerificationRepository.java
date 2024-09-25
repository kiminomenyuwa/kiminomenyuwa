package com.scit45.kiminomenyuwa.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.entity.verification.ReceiptVerificationEntity;

@Repository
public interface ReceiptVerificationRepository extends JpaRepository<ReceiptVerificationEntity, Long> {
	Optional<ReceiptVerificationEntity> findByReviewerAndTargetStore(UserEntity reviewer, StoreEntity targetStore);
}
