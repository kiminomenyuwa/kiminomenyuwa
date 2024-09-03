package com.scit45.kiminomenyuwa.domain.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

/**
 * 카테고리 타입을 정의하는 엔티티 클래스입니다.
 * 예를 들어, '재료', '나라', '조리 방법' 등의 카테고리 타입을 포함합니다.
 */
@Entity
@Table(name = "category_type")
@NoArgsConstructor
public class CategoryTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_name", nullable = false)
    private String typeName;
}