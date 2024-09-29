package com.scit45.kiminomenyuwa.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.dto.CategoryTypeDTO;
import com.scit45.kiminomenyuwa.domain.dto.FoodCategoryDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewResponseDTO;
import com.scit45.kiminomenyuwa.domain.dto.store.StoreInfoDTO;
import com.scit45.kiminomenyuwa.domain.dto.store.StoreRegistrationDTO;
import com.scit45.kiminomenyuwa.domain.entity.CategoryTypeEntity;
import com.scit45.kiminomenyuwa.domain.entity.FavoriteEntity;
import com.scit45.kiminomenyuwa.domain.entity.FoodCategoryEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.ProfilePhotoEntity;
import com.scit45.kiminomenyuwa.domain.entity.ReviewEntity;
import com.scit45.kiminomenyuwa.domain.entity.ReviewPhotoEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.StorePhotoEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.CategoryTypeRepository;
import com.scit45.kiminomenyuwa.domain.repository.FavoriteRepository;
import com.scit45.kiminomenyuwa.domain.repository.FoodCategoryRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.ProfilePhotoRepository;
import com.scit45.kiminomenyuwa.domain.repository.ReviewPhotoRepository;
import com.scit45.kiminomenyuwa.domain.repository.ReviewRepository;
import com.scit45.kiminomenyuwa.domain.repository.StorePhotoRepository;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class StoreService {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final MenuRepository menuRepository;
	private final FoodCategoryRepository foodCategoryRepository;
	private final CategoryTypeRepository categoryTypeRepository;
	private final StorePhotoRepository storePhotoRepository;
	private final FavoriteRepository favoriteRepository;
	private final ReviewRepository reviewRepository;
	private final ProfilePhotoRepository profilePhotoRepository;
	private final ReviewPhotoRepository reviewPhotoRepository;

	// // 모든 가게 리스트를 가져오는 메소드
	// public List<StoreRegistrationDTO> getAllStores() {
	// 	// StoreEntity를 StoreDTO로 변환하여 리스트로 반환
	//
	// 	List<StoreRegistrationDTO> storeList = storeRepository.findAll().stream()
	// 		.map(store -> {
	// 			log.debug(store.getName());
	// 			return StoreRegistrationDTO.builder()
	// 				.name(store.getName())
	// 				.phoneNumber(store.getPhoneNumber())
	// 				.zipcode(store.getZipcode())
	// 				.roadNameAddress(store.getRoadNameAddress())
	// 				.detailAddress(store.getDetailAddress())
	// 				.userId(store.getUser().getUserId())
	// 				.storeId(store.getStoreId())
	// 				.build();
	// 		})
	// 		.collect(Collectors.toList());
	//
	// 	// set photoList
	// 	storeList.forEach(store ->
	// 		store.setPhotosDTO(storePhotoRepository.findAllByStoreStoreId(store.getStoreId())
	// 			.stream().map(photo -> StorePhotoDTO.builder()
	// 				.photoId(photo.getPhotoId())
	// 				.photoUrl(photo.getPhotoUrl())
	// 				.isMain(photo.getIsMain())
	// 				.build()).toList()));
	//
	// 	return storeList;
	// }
	//
	// public Page<StoreUserDTO> getStoresForUser(String userId, Pageable pageable) {
	// 	if (userId == null || userId.isEmpty()) {
	// 		throw new IllegalArgumentException("유저 ID는 필수 입력 값입니다.");
	// 	}
	//
	// 	Page<StoreEntity> storePage = storeRepository.findByUser_UserId(userId, pageable);
	//
	// 	return storePage.map(store -> {
	// 		StoreUserDTO dto = new StoreUserDTO();
	// 		dto.setStoreId(store.getStoreId());
	// 		dto.setName(store.getName());
	// 		dto.setRoadNameAddress(store.getRoadNameAddress());
	// 		dto.setDetailAddress(store.getDetailAddress());
	// 		dto.setZipcode(store.getZipcode());
	// 		dto.setPhoneNumber(store.getPhoneNumber());
	// 		dto.setCategory(store.getCategory());
	// 		dto.setPhotoUrls(storePhotoRepository.findAllByStoreStoreId(store.getStoreId())
	// 			.stream()
	// 			.map(StorePhotoEntity::getPhotoUrl)
	// 			.collect(Collectors.toList()));
	// 		if (store.getLocation() != null) {
	// 			dto.setLongitude(store.getLocation().getCoordinate().getX());
	// 			dto.setLatitude(store.getLocation().getCoordinate().getY());
	// 		}
	// 		dto.setDescription(store.getDescription());
	//
	// 		// 찜 여부 및 찜 시간
	// 		Optional<FavoriteEntity> favoriteOpt = favoriteRepository.findByUser_UserIdAndStore_StoreId(userId,
	// 			store.getStoreId());
	// 		if (favoriteOpt.isPresent()) {
	// 			dto.setFavorited(true);
	// 			dto.setFavoritedTime(favoriteOpt.get().getCreatedAt());
	// 		} else {
	// 			dto.setFavorited(false);
	// 			dto.setFavoritedTime(null);
	// 		}
	//
	// 		return dto;
	// 	});
	// }

	/**
	 * dto를 entity로 변경해주는 메소드
	 *
	 * @param storeRegistrationDTO 작성한 가게 등록 정보
	 * @return 작성한 가게 정보를 entity로 변경된 정보를 리턴
	 */
	private StoreEntity convertToEntity(StoreRegistrationDTO storeRegistrationDTO) {

		UserEntity user = userRepository.findById(storeRegistrationDTO.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다: " + storeRegistrationDTO.getUserId()));
		return StoreEntity.builder()
			.user(user)
			.name(storeRegistrationDTO.getName())
			.certification(storeRegistrationDTO.getCertification())
			.roadNameAddress(storeRegistrationDTO.getRoadNameAddress())
			.detailAddress(storeRegistrationDTO.getDetailAddress())
			.zipcode(storeRegistrationDTO.getZipcode())
			.phoneNumber(storeRegistrationDTO.getPhoneNumber())
			.category(storeRegistrationDTO.getCategory())
			.description(storeRegistrationDTO.getDescription())
			.enabled(storeRegistrationDTO.getEnabled())
			.build();
	}

	/**
	 * 가게이름 받아오기
	 *
	 * @param storeId
	 * @return
	 */
	public String getStoreNameById(Integer storeId) {
		return storeRepository.findById(storeId)
			.map(store -> store.getName())
			.orElseThrow(() -> new IllegalArgumentException("Store not found"));
	}

	// 가게 ID로 메뉴 목록 조회
	public List<MenuDTO> getMenusByStoreId(Integer storeId) {
		List<MenuEntity> menus = menuRepository.findByStore_StoreId(storeId);
		// Entity를 DTO로 변환하여 반환
		return menus.stream()
			.map(menu -> new MenuDTO(menu.getMenuId(), menu.getStore().getStoreId(), menu.getName(), menu.getPrice(),
				menu.getPictureUrl(), menu.getEnabled()))
			.collect(Collectors.toList());
	}

	public List<CategoryTypeDTO> getAllCategoryTypes() {
		List<CategoryTypeEntity> entities = categoryTypeRepository.findAll();
		log.debug(entities.toString());
		return entities.stream()
			.map(entity -> CategoryTypeDTO.builder()
				.typeId(entity.getTypeId())
				.typeName(entity.getTypeName())
				.build())
			.collect(Collectors.toList());
	}

	public List<FoodCategoryDTO> getAllCategories(Integer typeId) {
		List<FoodCategoryEntity> categories = foodCategoryRepository.findByCategoryType_TypeId(typeId);
		log.debug(categories.toString());
		return categories.stream()
			.map(category -> new FoodCategoryDTO(category.getCategoryId(), category.getCategoryName(),
				category.getCategoryType().getTypeId()))
			.collect(Collectors.toList());
	}

	// 입력된 검색어를 바탕으로 카테고리 검색
	public List<FoodCategoryDTO> searchCategories(String query) {
		List<FoodCategoryEntity> categories = foodCategoryRepository.findByCategoryNameContaining(query);
		return categories.stream()
			.map(category -> new FoodCategoryDTO(category.getCategoryId(), category.getCategoryName(),
				category.getCategoryType().getTypeId()))
			.collect(Collectors.toList());
	}

	// /**
	//  * 사용자 ID로 가게 목록을 조회하여 StoreResponseDTO 페이지로 반환
	//  *
	//  * @param userId 사용자 고유 ID
	//  * @param pageable 페이지 및 정렬 정보
	//  * @return 가게 목록 페이지
	//  */
	// public Page<StoreResponseDTO> getStoresByUserId(String userId, Pageable pageable) {
	// 	if (userId == null || userId.isEmpty()) {
	// 		throw new IllegalArgumentException("유저 ID는 필수 입력 값입니다.");
	// 	}
	//
	// 	Page<StoreEntity> storePage = storeRepository.findByUser_UserId(userId, pageable);
	//
	// 	return storePage.map(store -> convertToDTO(store, userId));
	// }
	//
	// private StoreResponseDTO convertToDTO(StoreEntity store, String userId) {
	// 	StoreResponseDTO dto = new StoreResponseDTO();
	// 	dto.setStoreId(store.getStoreId());
	// 	dto.setMerchantId(store.getUser().getUserId());
	// 	dto.setName(store.getName());
	// 	dto.setCertification(store.getCertification());
	// 	dto.setRoadNameAddress(store.getRoadNameAddress());
	// 	dto.setDetailAddress(store.getDetailAddress());
	// 	dto.setZipcode(store.getZipcode());
	// 	dto.setPhoneNumber(store.getPhoneNumber());
	// 	dto.setCategory(store.getCategory());
	// 	dto.setDescription(store.getDescription());
	// 	dto.setEnabled(store.getEnabled());
	//
	// 	// 공간 정보(Geometry)에서 위도와 경도 추출
	// 	if (store.getLocation() != null) {
	// 		dto.setLongitude(store.getLocation().getCoordinate().getX());
	// 		dto.setLatitude(store.getLocation().getCoordinate().getY());
	// 	}
	//
	// 	// 사진 URL 목록 조회
	// 	dto.setPhotoUrls(storePhotoRepository.findAllByStoreStoreId(store.getStoreId()).stream()
	// 		.map(StorePhotoEntity::getPhotoUrl)
	// 		.collect(Collectors.toList()));
	//
	// 	// 찜 여부 및 찜 시간 조회
	// 	if (userId != null) {
	// 		Optional<FavoriteEntity> favoriteOpt = favoriteRepository.findByUser_UserIdAndStore_StoreId(userId,
	// 			store.getStoreId());
	// 		if (favoriteOpt.isPresent()) {
	// 			dto.setFavorited(true);
	// 			dto.setFavoritedTime(favoriteOpt.get().getCreatedAt());
	// 		} else {
	// 			dto.setFavorited(false);
	// 			dto.setFavoritedTime(null);
	// 		}
	// 	} else {
	// 		dto.setFavorited(false);
	// 		dto.setFavoritedTime(null);
	// 	}
	//
	// 	return dto;
	// }

	public StoreInfoDTO getStoreById(Integer storeId, String userId) throws EntityNotFoundException {
		StoreEntity store = storeRepository.findById(storeId)
			.orElseThrow(() -> new EntityNotFoundException("상점이 존재하지 않습니다: " + storeId));

		// 메뉴 리스트 조회 및 DTO 변환
		List<MenuEntity> menuEntities = menuRepository.findByStore(store);
		List<MenuDTO> menuDtos = menuEntities.stream()
			.map(menu -> MenuDTO.builder()
				.menuId(menu.getMenuId())
				.name(menu.getName())
				.price(menu.getPrice())
				.pictureUrl(menu.getPictureUrl())
				.enabled(menu.getEnabled())
				.build())
			.collect(Collectors.toList());

		// 가게 사진 리스트 조회
		List<StorePhotoEntity> photoEntities = storePhotoRepository.findByStore(store);
		List<String> photoUrls = photoEntities.stream()
			.map(StorePhotoEntity::getPhotoUrl)
			.collect(Collectors.toList());

		// 리뷰 리스트 조회 및 DTO 변환
		List<ReviewEntity> reviewEntities = reviewRepository.findByStore(store);
		List<ReviewResponseDTO> reviewDtos = reviewEntities.stream()
			.map(review ->
				{
					ProfilePhotoEntity profilePhoto = profilePhotoRepository.findByUserId(review.getUser().getUserId())
						.orElse(null);
					List<String> reviewPhotoUrls = reviewPhotoRepository.findByReview(review).stream()
						.map(ReviewPhotoEntity::getPhotoUrl).toList();

					ReviewResponseDTO dto = ReviewResponseDTO.builder()
						.reviewId(review.getReviewId())
						.userId(review.getUser().getName())
						.rating(review.getRating())
						.comment(review.getComment())
						.createdTime(review.getCreatedTime())
						.photoUrls(reviewPhotoUrls)
						.build();

					if (profilePhoto != null) {
						dto.setUserProfileUrl("/files/" + profilePhoto.getSavedName());
					} else
						dto.setUserProfileUrl("/images/default-profile.png");
					log.debug(dto.toString());
					return dto;
				}
			)
			.collect(Collectors.toList());

		// 위치 정보 변환
		Double latitude = null;
		Double longitude = null;
		if (store.getLocation() != null) {
			latitude = store.getLocation().getCoordinate().getY();
			longitude = store.getLocation().getCoordinate().getX();
		}

		// 찜 여부 및 찜 시간 조회
		Boolean favorited = false;
		LocalDateTime favoritedTime = null;
		if (userId != null) {
			UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다: " + userId));
			FavoriteEntity favorite = favoriteRepository.findByUserAndStore(user, store).orElse(null);
			if (favorite != null) {
				favorited = true;
				favoritedTime = favorite.getCreatedAt();
			}
		}

		// StoreUserDTO 생성
		StoreInfoDTO storeDto = StoreInfoDTO.builder()
			.storeId(store.getStoreId())
			.name(store.getName())
			.roadNameAddress(store.getRoadNameAddress())
			.detailAddress(store.getDetailAddress())
			.zipcode(store.getZipcode())
			.phoneNumber(store.getPhoneNumber())
			.category(store.getCategory())
			.photoUrls(photoUrls)
			.latitude(latitude)
			.longitude(longitude)
			.description(store.getDescription())
			.favorited(favorited)
			.favoritedTime(favoritedTime)
			.menus(menuDtos)
			.reviews(reviewDtos)
			.build();

		return storeDto;
	}
}
