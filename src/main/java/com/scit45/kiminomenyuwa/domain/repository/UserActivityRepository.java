package com.scit45.kiminomenyuwa.domain.repository;

import com.scit45.kiminomenyuwa.domain.entity.UserActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivityEntity, Integer> {
}