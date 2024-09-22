package com.scit45.kiminomenyuwa.domain.repository;

import com.scit45.kiminomenyuwa.domain.entity.StorePhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorePhotoRepository extends JpaRepository<StorePhotoEntity, Integer> {
    /**
     * 특정 가게 ID(storeId)에 속한 모든 StorePhotoEntity를 조회합니다.
     *
     * @param storeId 조회할 가게의 ID
     * @return 해당 가게에 속한 모든 StorePhotoEntity 리스트
     */
    List<StorePhotoEntity> findAllByStoreStoreId(Integer storeId);
}
