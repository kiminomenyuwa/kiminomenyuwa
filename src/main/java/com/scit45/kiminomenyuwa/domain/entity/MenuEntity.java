package com.scit45.kiminomenyuwa.domain.entity;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

/**
 * 상점에서 제공하는 메뉴 항목을 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "menu")
@NoArgsConstructor
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
}