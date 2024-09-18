package com.scit45.kiminomenyuwa.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.ProfilePhotoEntity;

@Repository
public interface ProfilePhotoRepository extends JpaRepository<ProfilePhotoEntity, Integer> {
	ProfilePhotoEntity searchByUserId(String userId);
}