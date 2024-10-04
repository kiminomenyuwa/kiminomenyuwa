package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.StorePhotoEntity;

@Repository
public interface StorePhotoRepository extends JpaRepository<StorePhotoEntity, Integer> {
    /**
     * 특정 가게 ID(storeId)에 속한 모든 StorePhotoEntity를 조회합니다.
     *
     * @param storeId 조회할 가게의 ID
     * @return 해당 가게에 속한 모든 StorePhotoEntity 리스트
     */
    List<StorePhotoEntity> findAllByStoreStoreId(Integer storeId);

	List<StorePhotoEntity> findByStore(StoreEntity store);
}
