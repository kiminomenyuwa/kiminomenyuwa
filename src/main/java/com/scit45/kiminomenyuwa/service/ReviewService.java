package com.scit45.kiminomenyuwa.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.scit45.kiminomenyuwa.domain.dto.ReviewRequestDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReviewResponseDTO;
import com.scit45.kiminomenyuwa.domain.entity.ProfilePhotoEntity;
import com.scit45.kiminomenyuwa.domain.entity.ReviewEntity;
import com.scit45.kiminomenyuwa.domain.entity.ReviewPhotoEntity;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.entity.verification.ReceiptVerificationEntity;
import com.scit45.kiminomenyuwa.domain.repository.ProfilePhotoRepository;
import com.scit45.kiminomenyuwa.domain.repository.ReceiptVerificationRepository;
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
	private final ProfilePhotoRepository profilePhotoRepository;
	private final ReceiptVerificationRepository receiptVerificationRepository;

	// application.properties에서 파일 업로드 경로를 읽어옴
	@Value("${file.storage.location}")
	private String uploadDir;

	public ReviewResponseDTO saveReviewWithPhotos(ReviewRequestDTO reviewRequestDTO, String loggedUserId) throws
		IOException {
		// 1. 상점 및 사용자 엔티티 조회
		StoreEntity store = storeRepository.findById(reviewRequestDTO.getStoreId())
			.orElseThrow(() -> new IllegalArgumentException("Store not found"));
		UserEntity reviewer = userRepository.findById(loggedUserId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// TODO : 여기 주석 풀기 (테스트를 위해서 주석처리 해두었음)
		// 2. 영수증 인증 내역 조회
		// 인증 내역이 존재하지 않으면 리뷰 작성 불가
		// ReceiptVerificationEntity receiptVerification = receiptVerificationRepository.findByReviewerAndTargetStore(
		//                 reviewer, store)
		//         .orElseThrow(() -> new IllegalArgumentException("인증 내역이 존재하지 않습니다."));

		// // 3. 이미 리뷰가 작성된 인증 내역인지 확인
		// if (receiptVerification.getReview() != null) {
		//     throw new IllegalArgumentException("A review has already been submitted for this receipt verification.");
		// }

		// 4. 리뷰 생성 및 저장
		ReviewEntity reviewEntity = new ReviewEntity(store, reviewer, reviewRequestDTO.getRating(),
			reviewRequestDTO.getComment());
		ReviewEntity savedReview = reviewRepository.save(reviewEntity);

		// 5. 사진 파일 저장 및 리뷰와 연결
		List<ReviewPhotoEntity> photoEntities = new ArrayList<>();
		List<String> photoUrls = new ArrayList<>();
		for (MultipartFile file : reviewRequestDTO.getPhotos()) {
			String photoUrl = saveFile(file, savedReview.getReviewId()); // 파일 저장 후 URL 반환
			photoUrls.add("/files/" + photoUrl); // 저장된 사진 URL을 리스트에 추가

			// ReviewPhotoEntity의 생성자와 setter를 사용하여 설정
			ReviewPhotoEntity photoEntity = new ReviewPhotoEntity(savedReview, photoUrl);
			photoEntities.add(photoEntity);
		}

		// 6. 리뷰 사진 저장
		reviewPhotoRepository.saveAll(photoEntities);

		// 7. ReviewResponseDTO 생성 및 반환
		ReviewResponseDTO responseDTO = new ReviewResponseDTO();
		responseDTO.setReviewId(savedReview.getReviewId());
		responseDTO.setStoreId(savedReview.getStore().getStoreId());
		responseDTO.setUserId(savedReview.getUser().getUserId());
		responseDTO.setRating(savedReview.getRating());
		responseDTO.setComment(savedReview.getComment());
		responseDTO.setCreatedTime(savedReview.getCreatedTime());
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
		String reviewFolder = uploadDir + File.separator + "review" + File.separator + reviewId;

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
		return "/files/review/" + reviewId + "/" + uniqueFileName;
	}

	/**
	 * 리뷰 ID로 리뷰를 조회하는 메서드
	 *
	 * @param reviewId 조회할 리뷰의 ID
	 * @return ReviewResponseDTO 리뷰 응답 DTO
	 */
	@Transactional(readOnly = true)
	public ReviewResponseDTO getReviewById(Integer reviewId) {
		// 리뷰 엔티티를 조회하고 없으면 예외 발생
		ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다: " + reviewId));

		// ReviewEntity를 ReviewResponseDTO로 변환하여 반환
		return convertToDto(reviewEntity);
	}

	/**
	 * ReviewEntity를 ReviewResponseDTO로 변환하는 메서드
	 *
	 * @param reviewEntity 변환할 리뷰 엔티티
	 * @return ReviewResponseDTO 변환된 리뷰 응답 DTO
	 */
	private ReviewResponseDTO convertToDto(ReviewEntity reviewEntity) {
		// DTO로 변환하면서 필요한 필드를 설정
		ReviewResponseDTO responseDTO = new ReviewResponseDTO();
		responseDTO.setReviewId(reviewEntity.getReviewId());
		responseDTO.setStoreId(reviewEntity.getStore().getStoreId());
		responseDTO.setUserId(reviewEntity.getUser().getUserId());
		responseDTO.setRating(reviewEntity.getRating());
		responseDTO.setComment(reviewEntity.getComment());
		responseDTO.setCreatedTime(reviewEntity.getCreatedTime());

		// 리뷰의 사진 URL 리스트 설정
		responseDTO.setPhotoUrls(getReviewPhotosByReviewId(reviewEntity.getReviewId()));

		return responseDTO;
	}

	/**
	 * 리뷰 ID로 리뷰 사진을 조회하는 메서드
	 *
	 * @param reviewId 조회할 리뷰의 ID
	 * @return List<ReviewPhotoDTO> 리뷰 사진 응답 DTO 리스트
	 */
	@Transactional(readOnly = true)
	public List<String> getReviewPhotosByReviewId(Integer reviewId) {
		// 리뷰 ID를 기반으로 리뷰 사진 엔티티 목록 조회
		List<ReviewPhotoEntity> photoEntities = reviewPhotoRepository.findByReviewReviewId(reviewId);

		// 엔티티 목록을 DTO로 변환하여 반환
		return photoEntities.stream()
			.map(p -> "/files/" + p.getPhotoUrl())
			.collect(Collectors.toList());
	}

	public List<ReviewResponseDTO> getMyReviews(String userId) {
		List<ReviewEntity> reviewEntities = reviewRepository.findAllByUserUserId(userId);

		return reviewEntities.stream()
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
					} else {
						dto.setUserProfileUrl("/images/default-profile.png");
					}
					log.debug(dto.toString());
					return dto;
				}
			)
			.collect(Collectors.toList());
	}

	// 페이지네이션을 적용한 사용자의 리뷰 조회
	public Page<ReviewResponseDTO> getMyReviewsPageable(String userId, Pageable pageable) {
		Page<ReviewEntity> reviewPage = reviewRepository.findByUserUserId(userId, pageable);
		return reviewPage.map(this::convertToDTO);
	}

	// Review 엔티티를 ReviewResponseDTO로 변환
	private ReviewResponseDTO convertToDTO(ReviewEntity review) {
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
		} else {
			dto.setUserProfileUrl("/images/default-profile.png");
		}
		log.debug(dto.toString());
		return dto;
	}

	// 페이지네이션, 필터링, 정렬을 적용한 사용자의 리뷰 조회
	public Page<ReviewResponseDTO> getMyReviewsPageable(String userId, Pageable pageable, Integer rating, String sortBy) {
		Page<ReviewEntity> reviewPage;
		if (rating == null && sortBy.equals("createdTime")) {
			// 기본 쿼리 사용 (필터링 없음, 생성 시간순 정렬)
			reviewPage = reviewRepository.findByUserUserId(userId, pageable);
		} else {
			// 필터링 또는 정렬이 적용된 쿼리 사용
			reviewPage = reviewRepository.findByUserIdAndRatingOrderBy(userId, rating, sortBy, pageable);
		}
		return reviewPage.map(this::convertToDTO);
	}
}
