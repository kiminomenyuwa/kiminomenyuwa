package com.scit45.kiminomenyuwa.domain.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

/**
 * 사용자가 소유한 상점 정보를 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "store")
@NoArgsConstructor
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "certification", nullable = false)
    private String certification;

    @Column(name = "road_name_address")
    private String roadNameAddress;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
}