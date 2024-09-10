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
 * 미니게임에서 메뉴에 대한 별점을 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "mini_game_menu_rating")
@NoArgsConstructor
public class MiniGameMenuRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Integer ratingId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "mini_game_id", nullable = false)
    private Integer miniGameId;

    @Column(name = "menu_id", nullable = false)
    private Integer menuId;

    @Column(name = "rating", nullable = false)
    private Float rating;

    @Column(name = "rating_date")
    private LocalDateTime ratingDate;
}