package com.scit45.kiminomenyuwa.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.scit45.kiminomenyuwa.domain.dto.ReviewRequestDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewResponseDTO;
import com.scit45.kiminomenyuwa.domain.entity.ReviewEntity;
import com.scit45.kiminomenyuwa.domain.entity.ReviewPhotoEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.repository.ReviewPhotoRepository;
import com.scit45.kiminomenyuwa.domain.repository.ReviewRepository;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewPhotoRepository reviewPhotoRepository;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;

	// application.properties에서 파일 업로드 경로를 읽어옴
	@Value("${file.storage.location}")
	private String uploadDir;

	public ReviewResponseDTO saveReviewWithPhotos(ReviewRequestDTO reviewRequestDTO) throws IOException {
		// 1. 상점 및 사용자 엔티티 조회
		StoreEntity store = storeRepository.findById(reviewRequestDTO.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException("Store not found"));
		UserEntity user = userRepository.findById(reviewRequestDTO.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// 2. 리뷰 생성 및 저장
		ReviewEntity reviewEntity = new ReviewEntity(store, user, reviewRequestDTO.getRating(),
			reviewRequestDTO.getComment());
		ReviewEntity savedReview = reviewRepository.save(reviewEntity);

		// 3. 사진 파일 저장 및 리뷰와 연결
		List<ReviewPhotoEntity> photoEntities = new ArrayList<>();
		List<String> photoUrls = new ArrayList<>();
		for (MultipartFile file : reviewRequestDTO.getPhotos()) {
			String photoUrl = saveFile(file, savedReview.getReviewId()); // 파일 저장 후 URL 반환
			photoUrls.add("/files/" + photoUrl); // 저장된 사진 URL을 리스트에 추가

			// ReviewPhotoEntity의 생성자와 setter를 사용하여 설정
			ReviewPhotoEntity photoEntity = new ReviewPhotoEntity(savedReview, photoUrl);
			photoEntities.add(photoEntity);
		}

		// 4. 리뷰 사진 저장
		reviewPhotoRepository.saveAll(photoEntities);

		// 5. ReviewResponseDTO 생성 및 반환
		ReviewResponseDTO responseDTO = new ReviewResponseDTO();
		responseDTO.setReviewId(savedReview.getReviewId());
		responseDTO.setStoreId(savedReview.getStore().getStoreId());
		responseDTO.setUserId(savedReview.getUser().getUserId());
		responseDTO.setRating(savedReview.getRating());
		responseDTO.setComment(savedReview.getComment());
		responseDTO.setCreatedTime(savedReview.getCreatedTime().toString()); // 필요에 따라 형식 수정
		responseDTO.setPhotoUrls(photoUrls);

		return responseDTO;
	}

	/**
	 * 파일을 저장하고 저장된 파일의 경로를 반환하는 메서드
	 *
	 * @param file     저장할 파일
	 * @param reviewId 리뷰 ID
	 * @return 저장된 파일의 상대 경로 (예: "{reviewId}/{uniqueFileName}")
	 * @throws IOException 파일 저장 중 오류 발생 시
	 */
	private String saveFile(MultipartFile file, Integer reviewId) throws IOException {
		// 리뷰 ID로 폴더 경로 설정
		String reviewFolder = uploadDir + File.separator + reviewId;

		// 폴더가 존재하지 않으면 생성
		File directory = new File(reviewFolder);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// 파일 이름 생성 (파일 이름이 중복되지 않도록 처리)
		String originalFileName = file.getOriginalFilename();
		String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName; // 고유한 파일 이름 생성
		String filePath = reviewFolder + File.separator + uniqueFileName;

		// 파일 저장
		File destFile = new File(filePath);
		file.transferTo(destFile); // 파일 저장
		log.debug("filePath = {}", filePath);

		// 저장된 파일의 경로 반환 (예: "{reviewId}/{uniqueFileName}")
		return reviewId + "/" + uniqueFileName;
	}

	/**
	 * 가게 ID로 리뷰 목록을 조회하는 메서드
	 */
	public List<ReviewEntity> getReviewsByStoreId(int storeId) {
		// 가게 ID로 리뷰 목록 조회
		return reviewRepository.findByStoreStoreId(storeId);
	}
}
