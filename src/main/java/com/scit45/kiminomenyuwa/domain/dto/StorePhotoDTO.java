package com.scit45.kiminomenyuwa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * StorePhotoEntity를 위한 Data Transfer Object (DTO) 클래스입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorePhotoDTO {
    Integer photoId;
    Integer storeId;
    String photoUrl;
    Boolean isMain;
}