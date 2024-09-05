package com.scit45.kiminomenyuwa.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

/**
 * 사용자의 활동 내역을 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "user_activity")
@NoArgsConstructor
public class UserActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "menu_id", nullable = false)
    private Integer menuId;

    @Column(name = "activity_time")
    private LocalDateTime activityTime;

    @Column(name = "activity_name")
    private String activityName;
}