package com.scit45.kiminomenyuwa.domain.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자의 식사 내역을 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "user_dining_history")
@NoArgsConstructor
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