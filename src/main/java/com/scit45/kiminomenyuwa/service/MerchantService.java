package com.scit45.kiminomenyuwa.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewResponseDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.domain.dto.store.MenuRegistrationDTO;
import com.scit45.kiminomenyuwa.domain.dto.store.StoreDetailsDTO;
import com.scit45.kiminomenyuwa.domain.dto.store.StoreRegistrationDTO;
import com.scit45.kiminomenyuwa.domain.entity.FoodCategoryEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuCategoryMappingEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.StorePhotoEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.FoodCategoryRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuCategoryMappingRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.ReviewRepository;
import com.scit45.kiminomenyuwa.domain.repository.StorePhotoRepository;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MerchantService {

	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final StorePhotoRepository storePhotoRepository;
	private final MenuRepository menuRepository;
	private final MenuCategoryMappingRepository menuCategoryMappingRepository;
	private final FoodCategoryRepository foodCategoryRepository;
	private final ReviewRepository reviewRepository; // 리뷰 리포지토리 추가
	// application.properties에서 파일 업로드 경로를 읽어옴
	@Value("${file.storage.location}")
	private String uploadDir;

	/**
	 * 사장님이 관리하는 가게 목록을 조회
	 *
	 * @param pageable 페이지 정보
	 * @return StoreMerchantDTO 페이지
	 */
	public Page<StoreDetailsDTO> getStoresForMerchant(String merchantId, Pageable pageable) {
		Page<StoreEntity> storePage = storeRepository.findByUser_UserId(merchantId, pageable);

		return storePage.map(store -> {
			StoreDetailsDTO dto = new StoreDetailsDTO();
			dto.setStoreId(store.getStoreId());
			dto.setName(store.getName());
			dto.setRoadNameAddress(store.getRoadNameAddress());
			dto.setDetailAddress(store.getDetailAddress());
			dto.setZipcode(store.getZipcode());
			dto.setPhoneNumber(store.getPhoneNumber());
			dto.setCategory(store.getCategory());

			// 사진 URL 목록 조회
			dto.setPhotoUrls(storePhotoRepository.findAllByStoreStoreId(store.getStoreId()).stream()
				.map(StorePhotoEntity::getPhotoUrl)
				.collect(Collectors.toList()));

			if (store.getLocation() != null) {
				dto.setLongitude(store.getLocation().getCoordinate().getX());
				dto.setLatitude(store.getLocation().getCoordinate().getY());
			}
			dto.setCertification(store.getCertification());
			dto.setEnabled(store.getEnabled());
			// 추가적인 사장님용 필드 설정

			return dto;
		});
	}

	/**
	 * 특정 가게의 상세 정보를 조회하여 StoreDetailsDTO로 반환
	 *
	 * @param storeId 가게 ID
	 * @return StoreDetailsDTO 객체
	 */
	@Transactional
	public StoreDetailsDTO getStoreDetails(Integer storeId, String userId) {
		StoreEntity store = storeRepository.findById(storeId)
			.orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다. ID: " + storeId));

		// 현재 사장님이 해당 가게를 관리하는지 확인
		if (!store.getUser().getUserId().equals(userId)) {
			throw new IllegalArgumentException("해당 가게를 관리할 권한이 없습니다.");
		}

		// StoreDetailsDTO 생성
		StoreDetailsDTO dto = new StoreDetailsDTO();
		dto.setStoreId(store.getStoreId());
		dto.setName(store.getName());
		dto.setCertification(store.getCertification());
		dto.setRoadNameAddress(store.getRoadNameAddress());
		dto.setDetailAddress(store.getDetailAddress());
		dto.setZipcode(store.getZipcode());
		dto.setPhoneNumber(store.getPhoneNumber());
		dto.setCategory(store.getCategory());
		dto.setDescription(store.getDescription());
		dto.setLatitude(store.getLocation().getCoordinate().getY());
		dto.setLongitude(store.getLocation().getCoordinate().getX());
		dto.setEnabled(store.getEnabled());

		// 사진 URL 목록 조회
		dto.setPhotoUrls(storePhotoRepository.findAllByStoreStoreId(store.getStoreId()).stream()
			.map(StorePhotoEntity::getPhotoUrl)
			.collect(Collectors.toList()));

		// 메뉴 목록 조회
		dto.setMenus(getMenusForStore(dto.getStoreId()));

		// 리뷰 목록 조회
		List<ReviewResponseDTO> reviewDTOs = reviewRepository.findByStore(store).stream()
			.map(review -> {
				ReviewResponseDTO reviewDTO = new ReviewResponseDTO();
				reviewDTO.setReviewId(review.getReviewId());
				reviewDTO.setUserId(review.getUser().getUserId());
				reviewDTO.setComment(review.getComment());
				reviewDTO.setRating(review.getRating());
				reviewDTO.setCreatedTime(review.getCreatedTime());
				return reviewDTO;
			})
			.collect(Collectors.toList());
		dto.setReviews(reviewDTOs);

		return dto;
	}

	/**
	 * 특정 가게의 메뉴 목록을 조회
	 *
	 * @param storeId 가게 ID
	 * @return 메뉴 목록
	 */
	public List<MenuDTO> getMenusForStore(Integer storeId) {
		StoreEntity store = storeRepository.findById(storeId)
			.orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다. ID: " + storeId));

		// 현재 사장님이 해당 가게를 관리하는지 확인
		String merchantId = getCurrentMerchantId();
		if (!store.getUser().getUserId().equals(merchantId)) {
			throw new IllegalArgumentException("해당 가게를 관리할 권한이 없습니다.");
		}

		return menuRepository.findByStore(store).stream()
			.map(menu -> {
				MenuDTO menuDTO = new MenuDTO();
				menuDTO.setMenuId(menu.getMenuId());
				menuDTO.setName(menu.getName());
				menuDTO.setPrice(menu.getPrice());
				menuDTO.setPictureUrl(menu.getPictureUrl());
				menuDTO.setEnabled(menu.getEnabled());

				// 카테고리 목록 조회
				List<String> categories = menuCategoryMappingRepository.findByMenu(menu).stream()
					.map(mapping -> mapping.getFoodCategory().getCategoryName())
					.collect(Collectors.toList());
				menuDTO.setCategories(categories);

				return menuDTO;
			})
			.collect(Collectors.toList());
	}

	/**
	 * 등록 정보를 받아서 storeRepository에 저장 후 저장된 storeId 반환
	 *
	 * @param storeRegistrationDTO
	 * @return 저장된 storeId
	 */
	public Integer saveStore(StoreRegistrationDTO storeRegistrationDTO) throws IOException {
		UserEntity merchant = getCurrentUser(storeRegistrationDTO.getUserId());

		GeometryFactory geometryFactory = new GeometryFactory();

		Point location = geometryFactory.createPoint(
			new Coordinate(storeRegistrationDTO.getLongitude(), storeRegistrationDTO.getLatitude()));

		StoreEntity storeEntity = StoreEntity.builder()
			.user(merchant)
			.name(storeRegistrationDTO.getName())
			.certification(storeRegistrationDTO.getCertification())
			.roadNameAddress(storeRegistrationDTO.getRoadNameAddress())
			.detailAddress(storeRegistrationDTO.getDetailAddress())
			.zipcode(storeRegistrationDTO.getZipcode())
			.phoneNumber(storeRegistrationDTO.getPhoneNumber())
			.category(storeRegistrationDTO.getCategory())
			.description(storeRegistrationDTO.getDescription())
			.enabled(true)
			.location(location)
			.build();

		// 엔티티를 DB에 저장
		StoreEntity savedStore = storeRepository.save(storeEntity);

		List<StorePhotoEntity> photoEntities = new ArrayList<>();
		for (MultipartFile file : storeRegistrationDTO.getPhotos()) {
			String photoUrl = saveFile(file, savedStore.getStoreId()); // 파일 저장 후 URL 반환

			StorePhotoEntity photoEntity = new StorePhotoEntity(savedStore, photoUrl);
			photoEntities.add(photoEntity);
		}
		photoEntities.get(0).setIsMain(true);

		storePhotoRepository.saveAll(photoEntities);

		return savedStore.getStoreId();
	}

	/**
	 * 파일을 저장하고 저장된 파일의 경로를 반환하는 메서드
	 *
	 * @param file    저장할 파일
	 * @param storeId 가게 ID
	 * @return 저장된 파일의 상대 경로 (예: "store/{storeId}/{uniqueFileName}", menu/{menuId}/uniqueFileName)
	 * @throws IOException 파일 저장 중 오류 발생 시
	 */
	private String saveFile(MultipartFile file, Integer storeId) throws IOException {
		// store ID로 폴더 경로 설정
		String storePhotos = uploadDir + File.separator + "store" + File.separator + storeId;

		// 폴더가 존재하지 않으면 생성
		File directory = new File(storePhotos);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// 파일 이름 생성 (파일 이름이 중복되지 않도록 처리)
		String originalFileName = file.getOriginalFilename();
		String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName; // 고유한 파일 이름 생성
		String filePath = storePhotos + File.separator + uniqueFileName;

		// 파일 저장
		File destFile = new File(filePath);
		file.transferTo(destFile); // 파일 저장
		log.debug("filePath = {}", filePath);

		// 저장된 파일의 경로 반환 (예: "{storeId}/{uniqueFileName}")
		return "files/store/" + storeId + "/" + uniqueFileName;
	}

	/**
	 * 현재 인증된 사장님의 ID를 반환
	 *
	 * @return 사장님 ID
	 */
	private String getCurrentMerchantId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new IllegalArgumentException("사용자가 인증되지 않았습니다.");
		}
		return authentication.getName();
	}

	/**
	 * 파일을 저장하고 저장된 파일의 경로를 반환하는 메서드
	 *
	 * @param file    저장할 파일
	 * @param menuId 가게 ID
	 * @return 저장된 파일의 상대 경로 (예: "menu/{menuId}/uniqueFileName")
	 * @throws IOException 파일 저장 중 오류 발생 시
	 */
	private String saveMenuPhoto(MultipartFile file, Integer menuId) throws IOException {
		// store ID로 폴더 경로 설정
		String storePhotos = uploadDir + File.separator + "menu" + File.separator + menuId;

		// 폴더가 존재하지 않으면 생성
		File directory = new File(storePhotos);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// 파일 이름 생성 (파일 이름이 중복되지 않도록 처리)
		String originalFileName = file.getOriginalFilename();
		String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName; // 고유한 파일 이름 생성
		String filePath = storePhotos + File.separator + uniqueFileName;

		// 파일 저장
		File destFile = new File(filePath);
		file.transferTo(destFile); // 파일 저장
		log.debug("filePath = {}", filePath);

		// 저장된 파일의 경로 반환 (예: "{storeId}/{uniqueFileName}")
		return "files/menu/" + menuId + "/" + uniqueFileName;
	}

	public Integer saveMenu(MenuRegistrationDTO menuRegistrationDTO) throws IOException {
		// 1. MenuEntity를 생성하고 필드 값을 설정
		StoreEntity store = storeRepository.findById(menuRegistrationDTO.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException(
				"메뉴를 등록할 storeId: " + menuRegistrationDTO.getStoreId() + "가 존재하지 않습니다."));

		// 현재 사장님이 해당 가게를 관리하는지 확인
		String merchantId = getCurrentMerchantId();
		if (!store.getUser().getUserId().equals(merchantId)) {
			throw new IllegalArgumentException("해당 가게를 관리할 권한이 없습니다.");
		}

		MenuEntity menuEntity = MenuEntity.builder()
			.store(store)
			.name(menuRegistrationDTO.getName())
			.price(menuRegistrationDTO.getPrice())
			.enabled(menuRegistrationDTO.getEnabled())
			.build();

		// 2. 메뉴 엔티티를 저장하여 menuId 생성
		MenuEntity savedMenu = menuRepository.save(menuEntity);

		// 사진 파일이 있는 경우 사진 저장
		if (!menuRegistrationDTO.getPhoto().isEmpty()) {
			savedMenu.setPictureUrl(saveMenuPhoto(menuRegistrationDTO.getPhoto(), savedMenu.getMenuId()));
		}

		// 3. 카테고리 매핑 생성
		List<MenuCategoryMappingEntity> categoryMappings = menuRegistrationDTO.getCategories()
			.stream()
			.map(categoryName -> {
				// 카테고리 이름을 기반으로 FoodCategory 엔티티를 조회
				FoodCategoryEntity foodCategory = foodCategoryRepository.findByCategoryName(categoryName)
					.orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다: " + categoryName));

				// MenuCategoryMappingEntity 객체를 생성하여 메뉴와 카테고리 간 매핑을 저장
				return new MenuCategoryMappingEntity(savedMenu, foodCategory);
			})
			.collect(Collectors.toList());

		// 4. 카테고리 매핑 엔티티들을 저장
		menuCategoryMappingRepository.saveAll(categoryMappings);

		return savedMenu.getMenuId();
	}

	private StoreResponseDTO convertToDTO(StoreEntity store) {
		StoreResponseDTO dto = new StoreResponseDTO();
		dto.setStoreId(store.getStoreId());
		dto.setMerchantId(store.getUser().getUserId());
		dto.setName(store.getName());
		dto.setCertification(store.getCertification());
		dto.setRoadNameAddress(store.getRoadNameAddress());
		dto.setDetailAddress(store.getDetailAddress());
		dto.setZipcode(store.getZipcode());
		dto.setPhoneNumber(store.getPhoneNumber());
		dto.setCategory(store.getCategory());
		dto.setDescription(store.getDescription());
		dto.setEnabled(store.getEnabled());

		// 공간 정보(Geometry)에서 위도와 경도 추출
		if (store.getLocation() != null) {
			dto.setLongitude(store.getLocation().getCoordinate().getX());
			dto.setLatitude(store.getLocation().getCoordinate().getY());
		}

		// 사진 URL 목록 조회
		dto.setPhotoUrls(storePhotoRepository.findAllByStoreStoreId(store.getStoreId()).stream()
			.map(StorePhotoEntity::getPhotoUrl)
			.collect(Collectors.toList()));
		return dto;
	}

	/**
	 * 현재 인증된 사용자의 UserEntity를 반환
	 *
	 * @return 현재 사용자
	 */
	private UserEntity getCurrentUser(String userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
	}
}
