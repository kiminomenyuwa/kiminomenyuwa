package com.scit45.kiminomenyuwa.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 가게에 속한 사진 정보를 저장하는 엔티티 클래스입니다.
 */
@Data
@Entity
@Table(name = "store_photo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorePhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    // 대표 사진 여부를 나타내는 필드
    @Column(name = "is_main")
    private Boolean isMain = false;

    public StorePhotoEntity(StoreEntity store, String photoUrl) {
        this.store = store;
        this.photoUrl = photoUrl;
    }

}