package com.scit45.kiminomenyuwa.domain.dto.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreRegistrationDTO {
    Integer storeId;
    String userId; // UserEntity가 아닌 userId로 설정
    String name;
    String certification;
    String roadNameAddress;
    String detailAddress;
    String zipcode;
    String phoneNumber;
    String category;
    String description;
    Boolean enabled;
    Double longitude;
    Double latitude;
    List<MultipartFile> photos = new ArrayList<>();
    List<StorePhotoDTO> photosDTO = new ArrayList<>();
}
