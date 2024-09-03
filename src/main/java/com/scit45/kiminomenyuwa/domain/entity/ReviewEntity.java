package com.scit45.kiminomenyuwa.domain.entity;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 리뷰를 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "review")
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "menu_id", nullable = false)
    private Integer menuId;

    @Column(name = "rating", nullable = false)
    private Byte rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_time")
    private LocalDateTime createdTime;
}
