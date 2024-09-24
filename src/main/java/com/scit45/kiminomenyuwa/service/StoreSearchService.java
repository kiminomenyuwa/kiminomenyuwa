package com.scit45.kiminomenyuwa.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.domain.entity.FavoriteEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.FavoriteRepository;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class StoreSearchService {
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final FavoriteRepository favoriteRepository;

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

		// 현재 인증된 사용자 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = null;
		Set<Integer> favoritedStoreIds = new HashSet<>();

		if (authentication != null && authentication.isAuthenticated() &&
			!authentication.getPrincipal().equals("anonymousUser")) {
			userId = authentication.getName();
			// 사용자의 찜한 상점 목록 조회
			UserEntity user = userRepository.findById(userId)
				.orElse(null);
			if (user != null) {
				List<FavoriteEntity> favorites = favoriteRepository.findByUser(user);
				favoritedStoreIds = favorites.stream()
					.map(fav -> fav.getStore().getStoreId())
					.collect(Collectors.toSet());
			}
		}

		// StoreEntity를 StoreResponseDTO로 매핑하면서 favorited 필드 설정
		final Set<Integer> finalFavoritedStoreIds = favoritedStoreIds;
		return storeEntities.stream().map(store -> {
			StoreResponseDTO dto = mapToDTO(store);
			dto.setFavorited(finalFavoritedStoreIds.contains(store.getStoreId()));
			return dto;
		}).collect(Collectors.toList());
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
