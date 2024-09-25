package com.scit45.kiminomenyuwa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class StoreSearchService {
	private final StoreRepository storeRepository;

	/**
	 * 특정 위치와 반경을 기준으로 주변 상점을 검색하고 DTO로 매핑합니다.
	 *
	 * @param latitude  중심점의 위도
	 * @param longitude 중심점의 경도
	 * @param radius    반경 (미터 단위)
	 * @return 반경 내의 상점 목록 DTO
	 */
	@Transactional(readOnly = true)
	public List<StoreResponseDTO> getStoresNearby(double latitude, double longitude, double radius) {
		// Point 객체 생성 (경도, 위도 순)
		GeometryFactory geometryFactory = new GeometryFactory();
		Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
		point.setSRID(4326); // WGS 84 좌표계

		// Point 객체를 WKT로 변환
		String pointWKT = point.toText(); // 예: "POINT(126.9855771 37.5728571)"

		// StoreEntity 리스트 조회
		List<StoreEntity> storeEntities = storeRepository.findStoresWithinRadius(pointWKT, radius);

		// StoreEntity를 StoreResponseDTO로 매핑
		return storeEntities.stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	/**
	 * StoreEntity를 StoreResponseDTO로 매핑합니다.
	 *
	 * @param store StoreEntity 객체
	 * @return StoreResponseDTO 객체
	 */
	private StoreResponseDTO mapToDTO(StoreEntity store) {
		StoreResponseDTO dto = new StoreResponseDTO();
		dto.setStoreId(store.getStoreId());
		dto.setMerchantId(store.getUser().getUserId()); // UserEntity에서 userId 가져오기
		dto.setName(store.getName());
		dto.setCertification(store.getCertification());
		dto.setRoadNameAddress(store.getRoadNameAddress());
		dto.setDetailAddress(store.getDetailAddress());
		dto.setZipcode(store.getZipcode());
		dto.setPhoneNumber(store.getPhoneNumber());
		dto.setCategory(store.getCategory());
		dto.setDescription(store.getDescription());
		dto.setEnabled(store.getEnabled());

		// 위치 정보 추출
		if (store.getLocation() != null) {
			dto.setLongitude(store.getLocation().getCoordinate().getX());
			dto.setLatitude(store.getLocation().getCoordinate().getY());
		}

		return dto;
	}
}
