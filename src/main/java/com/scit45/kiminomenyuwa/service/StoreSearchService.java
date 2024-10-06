package com.scit45.kiminomenyuwa.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.domain.dto.store.StoreWithMenusDTO;
import com.scit45.kiminomenyuwa.domain.entity.FavoriteEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.StorePhotoEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.FavoriteRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.StorePhotoRepository;
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
	private final StorePhotoRepository storePhotoRepository;
	private final MenuRepository menuRepository;

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

		if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal()
			.equals("anonymousUser")) {
			userId = authentication.getName();
			// 사용자의 찜한 상점 목록 조회
			UserEntity user = userRepository.findById(userId).orElse(null);
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

		// 상점의 사진 URLs 추가
		List<StorePhotoEntity> photoEntities = storePhotoRepository.findByStore(store);
		List<String> photoUrls = photoEntities.stream()
			.map(StorePhotoEntity::getPhotoUrl)
			.collect(Collectors.toList());
		dto.setPhotoUrls(photoUrls);

		// 위치 정보 추출
		if (store.getLocation() != null) {
			dto.setLongitude(store.getLocation().getCoordinate().getX());
			dto.setLatitude(store.getLocation().getCoordinate().getY());
		}

		return dto;
	}

	/**
	 * 사용자가 찜한 상점 목록을 조회하고 DTO로 매핑하며, 정렬 옵션을 적용합니다.
	 *
	 * @param userId 로그인한 사용자의 ID
	 * @param sortBy 정렬 기준 ("distance", "newest", "oldest")
	 * @param latitude 현재 위치의 위도 (거리 기준 정렬 시 필요)
	 * @param longitude 현재 위치의 경도 (거리 기준 정렬 시 필요)
	 * @param radius 반경 (미터 단위, 거리 기준 정렬 시 필요)
	 * @return 사용자가 찜한 상점 목록 DTO
	 */
	@Transactional(readOnly = true)
	public List<StoreResponseDTO> getUserFavorites(String userId, String sortBy, double latitude, double longitude,
		double radius) {
		// 사용자 엔티티 조회
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

		// 찜한 상점 목록 조회
		List<FavoriteEntity> favorites = favoriteRepository.findByUser(user);
		List<StoreResponseDTO> favoritedStores = favorites.stream().map(f -> {
			StoreResponseDTO dto = mapToDTO(f.getStore());
			dto.setFavorited(true);
			dto.setFavoritedTime(f.getCreatedAt());

			dto.setPhotoUrls(storePhotoRepository.findAllByStoreStoreId(f.getStore().getStoreId())
				.stream()
				.map(StorePhotoEntity::getPhotoUrl)
				.toList());

			return dto;
		}).collect(Collectors.toList());

		// 정렬 적용
		switch (sortBy.toLowerCase()) {
			case "distance":
				favoritedStores = favoritedStores.stream()
					.sorted(Comparator.comparingDouble(
						store -> calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude())))
					.collect(Collectors.toList());
				break;
			case "newest":
				favoritedStores = favoritedStores.stream()
					.sorted(Comparator.comparing(StoreResponseDTO::getFavoritedTime).reversed())
					.collect(Collectors.toList());
				break;
			case "oldest":
				favoritedStores = favoritedStores.stream()
					.sorted(Comparator.comparing(StoreResponseDTO::getFavoritedTime))
					.collect(Collectors.toList());
				break;
			default:
				// 기본 정렬 (예: 이름 순)
				favoritedStores = favoritedStores.stream()
					.sorted(Comparator.comparing(StoreResponseDTO::getName))
					.collect(Collectors.toList());
				break;
		}

		// DTO 매핑
		return favoritedStores;
	}

	/**
	 * 두 지점 간의 거리를 계산합니다. (Haversine formula)
	 *
	 * @param lat1 첫 번째 지점의 위도
	 * @param lon1 첫 번째 지점의 경도
	 * @param lat2 두 번째 지점의 위도
	 * @param lon2 두 번째 지점의 경도
	 * @return 거리 (미터 단위)
	 */
	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int EARTH_RADIUS = 6371000; // 미터 단위

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(
			Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c;
	}

	/**
	 * 이름으로 상점을 검색하고 DTO로 매핑합니다.
	 *
	 * @return 상점 목록 DTO
	 */
	@Transactional(readOnly = true)
	public Page<StoreResponseDTO> getStoresByName(String keyword, Pageable pageable) {
		// StoreEntity 리스트 조회
		Page<StoreEntity> storeEntities = storeRepository.findByNameContaining(keyword, pageable);

		// 현재 인증된 사용자 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = null;
		Set<Integer> favoritedStoreIds = new HashSet<>();

		if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal()
			.equals("anonymousUser")) {
			userId = authentication.getName();
			// 사용자의 찜한 상점 목록 조회
			UserEntity user = userRepository.findById(userId).orElse(null);
			if (user != null) {
				List<FavoriteEntity> favorites = favoriteRepository.findByUser(user);
				favoritedStoreIds = favorites.stream()
					.map(fav -> fav.getStore().getStoreId())
					.collect(Collectors.toSet());
			}
		}

		// StoreEntity를 StoreResponseDTO로 매핑하면서 favorited 필드 설정
		final Set<Integer> finalFavoritedStoreIds = favoritedStoreIds;
		List<StoreResponseDTO> dtoList = storeEntities.stream().map(store -> {
			StoreResponseDTO dto = mapToDTO(store);
			dto.setFavorited(finalFavoritedStoreIds.contains(store.getStoreId()));
			return dto;
		}).collect(Collectors.toList());

		return new PageImpl<>(dtoList, pageable, storeEntities.getTotalElements());
	}

	@Transactional(readOnly = true)
	public List<StoreWithMenusDTO> getNearByStoresWithMenus(double latitude, double longitude, double radius) {
		// Point 객체 생성 (경도, 위도 순)
		GeometryFactory geometryFactory = new GeometryFactory();
		Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
		point.setSRID(4326); // WGS 84 좌표계

		// Point 객체를 WKT로 변환
		String pointWKT = point.toText(); // 예: "POINT(126.9855771 37.5728571)"

		// 반경 내의 StoreEntity 리스트 조회
		List<StoreEntity> storeEntities = storeRepository.findStoresWithinRadius(pointWKT, radius);

		// 현재 인증된 사용자 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = null;
		Map<Integer, LocalDateTime> favoritedStoresMap = new HashMap<>();

		if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal()
			.equals("anonymousUser")) {
			userId = authentication.getName();
			// 사용자의 찜한 상점 목록 조회
			UserEntity user = userRepository.findById(userId).orElse(null);
			if (user != null) {
				List<FavoriteEntity> favorites = favoriteRepository.findByUser(user);
				favoritedStoresMap = favorites.stream()
					.collect(Collectors.toMap(fav -> fav.getStore().getStoreId(), FavoriteEntity::getCreatedAt,
						(existing, replacement) -> existing));
			}
		}

		// StoreEntity를 StoreWithMenusDTO로 매핑하면서 favorited 필드 및 메뉴 리스트 설정
		Map<Integer, LocalDateTime> finalFavoritedStoresMap = favoritedStoresMap;
		return storeEntities.stream().map(store -> {
			StoreWithMenusDTO dto = StoreWithMenusDTO.builder()
				.storeId(store.getStoreId())
				.name(store.getName())
				.roadNameAddress(store.getRoadNameAddress())
				.detailAddress(store.getDetailAddress())
				.zipcode(store.getZipcode())
				.phoneNumber(store.getPhoneNumber())
				.category(store.getCategory())
				.description(store.getDescription())
				.latitude(store.getLocation().getCoordinate().getY()) // WKT의 Y는 위도
				.longitude(store.getLocation().getCoordinate().getX()) // WKT의 X는 경도
				.favorited(finalFavoritedStoresMap.containsKey(store.getStoreId()))
				.favoritedTime(finalFavoritedStoresMap.get(store.getStoreId()))
				.build();

			// 상점의 사진 URLs 추가
			List<StorePhotoEntity> photoEntities = storePhotoRepository.findByStore(store);
			List<String> photoUrls = photoEntities.stream()
				.map(StorePhotoEntity::getPhotoUrl)
				.collect(Collectors.toList());
			dto.setPhotoUrls(photoUrls);

			// 상점의 메뉴 리스트 조회 및 DTO로 매핑
			List<MenuDTO> menuDTOs = menuRepository.findByStore(store)
				.stream()
				.map(menu -> MenuDTO.builder()
					.menuId(menu.getMenuId())
					.name(menu.getName())
					.price(menu.getPrice())
					.pictureUrl(menu.getPictureUrl())
					.enabled(menu.getEnabled())
					.build())
				.collect(Collectors.toList());
			dto.setMenus(menuDTOs);

			return dto;
		}).collect(Collectors.toList());
	}

}
