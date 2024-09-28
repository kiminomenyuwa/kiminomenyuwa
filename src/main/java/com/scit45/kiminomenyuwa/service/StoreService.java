package com.scit45.kiminomenyuwa.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scit45.kiminomenyuwa.domain.dto.CategoryTypeDTO;
import com.scit45.kiminomenyuwa.domain.dto.FoodCategoryDTO;
import com.scit45.kiminomenyuwa.domain.dto.MenuDTO;
import com.scit45.kiminomenyuwa.domain.dto.StorePhotoDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreRegistrationDTO;
import com.scit45.kiminomenyuwa.domain.dto.StoreResponseDTO;
import com.scit45.kiminomenyuwa.domain.entity.CategoryTypeEntity;
import com.scit45.kiminomenyuwa.domain.entity.FavoriteEntity;
import com.scit45.kiminomenyuwa.domain.entity.FoodCategoryEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuCategoryMappingEntity;
import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.StorePhotoEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.CategoryTypeRepository;
import com.scit45.kiminomenyuwa.domain.repository.FavoriteRepository;
import com.scit45.kiminomenyuwa.domain.repository.FoodCategoryRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuCategoryMappingRepository;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.StorePhotoRepository;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

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
	private final MenuCategoryMappingRepository menuCategoryMappingRepository;
	private final FoodCategoryRepository foodCategoryRepository;
	private final CategoryTypeRepository categoryTypeRepository;
	private final StorePhotoRepository storePhotoRepository;
	private final FavoriteRepository favoriteRepository;

	// application.properties에서 파일 업로드 경로를 읽어옴
	@Value("${file.storage.location}")
	private String uploadDir;

	// 모든 가게 리스트를 가져오는 메소드
	public List<StoreRegistrationDTO> getAllStores() {
		// StoreEntity를 StoreDTO로 변환하여 리스트로 반환

		List<StoreRegistrationDTO> storeList = storeRepository.findAll().stream()
			.map(store -> {
				log.debug(store.getName());
				return StoreRegistrationDTO.builder()
					.name(store.getName())
					.phoneNumber(store.getPhoneNumber())
					.zipcode(store.getZipcode())
					.roadNameAddress(store.getRoadNameAddress())
					.detailAddress(store.getDetailAddress())
					.userId(store.getUser().getUserId())
					.storeId(store.getStoreId())
					.build();
			})
			.collect(Collectors.toList());

		// set photoList
		storeList.forEach(store ->
			store.setPhotosDTO(storePhotoRepository.findAllByStoreStoreId(store.getStoreId())
				.stream().map(photo -> StorePhotoDTO.builder()
					.photoId(photo.getPhotoId())
					.photoUrl(photo.getPhotoUrl())
					.isMain(photo.getIsMain())
					.build()).toList()));

		return storeList;
	}

	/**
	 * 등록 정보를 받아서 storeRepository에 저장한다
	 *
	 * @param storeRegistrationDTO
	 */
	public void saveStore(StoreRegistrationDTO storeRegistrationDTO) throws IOException {
		StoreEntity storeEntity = convertToEntity(storeRegistrationDTO);
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
	}

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

	public void saveMenu(MenuDTO menuDTO) {
		// 1. MenuEntity를 생성하고 필드 값을 설정
		StoreEntity store = storeRepository.findById(menuDTO.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException("storeId: " + menuDTO.getStoreId() + "가 존재하지 않습니다."));

		MenuEntity menuEntity = MenuEntity.builder()
			.store(store)
			.name(menuDTO.getName())
			.price(menuDTO.getPrice())
			.pictureUrl(menuDTO.getPictureUrl())
			.enabled(menuDTO.getEnabled())
			.build();

		// 2. 메뉴 엔티티를 저장하여 menuId 생성
		MenuEntity savedMenu = menuRepository.save(menuEntity);

		// 3. 카테고리 매핑 생성
		List<MenuCategoryMappingEntity> categoryMappings = menuDTO.getCategories().stream()
			.map(categoryName -> {
				// 카테고리 이름을 기반으로 FoodCategory 엔티티를 조회
				FoodCategoryEntity foodCategory = foodCategoryRepository.findByCategoryName(categoryName)
					.orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다: " + categoryName));

				// MenuCategoryMappingEntity 객체를 생성하여 메뉴와 카테고리 간 매핑을 저장
				MenuCategoryMappingEntity mapping = new MenuCategoryMappingEntity();
				mapping.setMenu(savedMenu);
				mapping.setFoodCategory(foodCategory);
				return mapping;
			})
			.collect(Collectors.toList());

		// 4. 카테고리 매핑 엔티티들을 저장
		menuCategoryMappingRepository.saveAll(categoryMappings);
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
		return entities.stream()
			.map(entity -> CategoryTypeDTO.builder()
				.typeId(entity.getTypeId())
				.typeName(entity.getTypeName())
				.build())
			.collect(Collectors.toList());
	}

	public List<FoodCategoryDTO> getAllCategories(Integer typeId) {
		List<FoodCategoryEntity> categories = foodCategoryRepository.findByCategoryType_TypeId(typeId);
		return categories.stream()
			.map(category -> new FoodCategoryDTO(category.getCategoryName(), category.getCategoryType().getTypeId()))
			.collect(Collectors.toList());
	}

	/**
	 * 파일을 저장하고 저장된 파일의 경로를 반환하는 메서드
	 *
	 * @param file    저장할 파일
	 * @param storeId 가게 ID
	 * @return 저장된 파일의 상대 경로 (예: "{storeId}/{uniqueFileName}")
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

	private StoreResponseDTO convertToDTO(StoreEntity store, String userId) {
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

		// 찜 여부 및 찜 시간 조회
		if (userId != null) {
			Optional<FavoriteEntity> favoriteOpt = favoriteRepository.findByUser_UserIdAndStore_StoreId(userId,
				store.getStoreId());
			if (favoriteOpt.isPresent()) {
				dto.setFavorited(true);
				dto.setFavoritedTime(favoriteOpt.get().getCreatedAt());
			} else {
				dto.setFavorited(false);
				dto.setFavoritedTime(null);
			}
		} else {
			dto.setFavorited(false);
			dto.setFavoritedTime(null);
		}

		return dto;
	}
}
