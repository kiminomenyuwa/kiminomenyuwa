package com.scit45.kiminomenyuwa.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자의 식사 내역을 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "user_dining_history")
@NoArgsConstructor
@Data
public class UserDiningHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dining_id")
    private Integer diningId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "menu_id", nullable = false)
    private Integer menuId;

    @Column(name = "dining_date")
    private LocalDateTime diningDate;
}