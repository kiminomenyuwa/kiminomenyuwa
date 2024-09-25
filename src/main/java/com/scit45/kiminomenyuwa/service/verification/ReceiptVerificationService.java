package com.scit45.kiminomenyuwa.service.verification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scit45.kiminomenyuwa.domain.dto.ItemDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReceiptDTO;
import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.domain.entity.verification.ReceiptVerificationEntity;
import com.scit45.kiminomenyuwa.domain.repository.MenuRepository;
import com.scit45.kiminomenyuwa.domain.repository.ReceiptVerificationRepository;
import com.scit45.kiminomenyuwa.domain.repository.StoreRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ReceiptVerificationService {
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;
	private final ReceiptVerificationRepository receiptVerificationRepository;

	/**
	 * 영수증 인증을 수행하는 메소드
	 *
	 * @param receipt          분석된 영수증 데이터
	 * @param loggedInUserId   현재 로그인된 사용자 ID
	 * @return 인증 성공 여부
	 */
	public boolean verifyReceipt(ReceiptDTO receipt, String loggedInUserId) {
		// 입력 데이터 검증
		if (receipt == null) {
			throw new IllegalArgumentException("영수증 정보가 제공되지 않았습니다.");
		}
		if (loggedInUserId == null || loggedInUserId.trim().isEmpty()) {
			throw new IllegalArgumentException("로그인된 사용자 ID가 제공되지 않았습니다.");
		}
		validateReceiptDTO(receipt);

		UserEntity userEntity = validateUser(loggedInUserId);
		StoreEntity storeEntity = validateStore(
			receipt.getMerchantName(),
			receipt.getMerchantAddress(),
			receipt.getMerchantPhoneNumber()
		);
		validateItems(receipt.getItems(), storeEntity.getStoreId());
		if (receipt.getTransactionDate() != null) {
			validateReceiptDate(receipt.getTransactionDate());
		}
		// 검증 성공 시 영수증 인증 정보를 저장
		saveReceiptVerification(userEntity, storeEntity);

		return true;
	}

	/**
	 * ReceiptDTO의 입력 데이터 검증 메소드
	 *
	 * @param receipt  검증할 ReceiptDTO 객체
	 */
	private void validateReceiptDTO(ReceiptDTO receipt) {
		if (receipt.getMerchantName() == null || receipt.getMerchantName().trim().isEmpty()) {
			throw new IllegalArgumentException("가게 이름이 제공되지 않았습니다.");
		}
		if (receipt.getMerchantAddress() == null || receipt.getMerchantAddress().trim().isEmpty()) {
			throw new IllegalArgumentException("가게 주소가 제공되지 않았습니다.");
		}
	}

	/**
	 * 사용자 검증 메소드
	 *
	 * @param userId  검증할 사용자 ID
	 * @return 유효한 UserEntity 객체
	 */
	private UserEntity validateUser(String userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("로그인된 사용자가 유효하지 않습니다."));
	}

	/**
	 * 가게 검증 메소드
	 *
	 * @param merchantName     가게 이름
	 * @param fullAddress      전체 주소 (도로명 주소 + 상세 주소)
	 * @param merchantPhoneNumber  가게 전화번호
	 * @return 유효한 StoreEntity 객체
	 */
	private StoreEntity validateStore(String merchantName, String fullAddress, String merchantPhoneNumber) {
		if (merchantName == null || fullAddress == null || merchantPhoneNumber == null) {
			throw new IllegalArgumentException("가게 정보가 충분히 제공되지 않았습니다.");
		}

		// 전체 주소를 도로명 주소와 상세 주소로 분리
		String roadNameAddress = null;
		String detailAddress = null;

		if (fullAddress.contains(",")) {
			String[] addressParts = fullAddress.split(",", 2);
			roadNameAddress = addressParts[0].trim();
			detailAddress = addressParts[1].trim();
		} else {
			roadNameAddress = fullAddress.trim();
		}

		String normalizedName = normalizeString(merchantName);
		String normalizedRoadAddress = normalizeString(roadNameAddress);
		String normalizedDetailAddress = detailAddress != null ? normalizeString(detailAddress) : "";
		String normalizedPhone = normalizePhoneNumber(merchantPhoneNumber);

		log.info("Validating store with name: {}, roadAddress: {}, detailAddress: {}, phoneNumber: {}",
			merchantName, roadNameAddress, detailAddress, merchantPhoneNumber);

		// 데이터베이스에서 잠재적으로 일치할 수 있는 가게들 검색
		List<StoreEntity> potentialStores = storeRepository.findPotentialMatches(
			normalizedName, normalizedRoadAddress, normalizedDetailAddress, normalizedPhone
		);

		if (potentialStores.isEmpty()) {
			log.warn("No potential stores found for name: {}, roadAddress: {}, detailAddress: {}, phoneNumber: {}",
				merchantName, roadNameAddress, detailAddress, merchantPhoneNumber);
			throw new IllegalArgumentException("가게 정보를 찾을 수 없습니다.");
		}

		for (StoreEntity storeEntity : potentialStores) {
			String storeName = normalizeString(storeEntity.getName());
			String storeRoadAddress = normalizeString(storeEntity.getRoadNameAddress());
			String storeDetailAddress =
				storeEntity.getDetailAddress() != null ? normalizeString(storeEntity.getDetailAddress()) : "";
			String storePhone = normalizePhoneNumber(storeEntity.getPhoneNumber());

			boolean nameMatch = storeName.equals(normalizedName);
			boolean roadAddressMatch = storeRoadAddress.equals(normalizedRoadAddress);
			boolean detailAddressMatch = storeDetailAddress.equals(normalizedDetailAddress);
			boolean phoneMatch = storePhone.equals(normalizedPhone);

			int matchCount = 0;
			if (nameMatch)
				matchCount++;
			if (roadAddressMatch)
				matchCount++;
			if (detailAddressMatch)
				matchCount++;
			if (phoneMatch)
				matchCount++;

			// 두 개 이상의 정보가 일치하면 동일한 가게로 간주
			if (matchCount >= 2) {
				log.info("Store matched: {}", storeEntity.getName());
				return storeEntity;
			}
		}

		log.warn(
			"No matching store found after detailed comparison for name: {}, roadAddress: {}, detailAddress: {}, phoneNumber: {}",
			merchantName, roadNameAddress, detailAddress, merchantPhoneNumber);
		throw new IllegalArgumentException("가게 정보를 찾을 수 없습니다.");
	}

	/**
	 * 문자열을 정규화하는 메소드
	 *
	 * @param input  정규화할 문자열
	 * @return 정규화된 문자열
	 */
	private String normalizeString(String input) {
		if (input == null)
			return "";
		return input.toLowerCase()
			.replaceAll("\\s+", "")
			.replaceAll("[^a-zA-Z0-9가-힣]", "");
	}

	/**
	 * 전화번호를 정규화하는 메소드
	 *
	 * @param phoneNumber  정규화할 전화번호
	 * @return 정규화된 전화번호 (숫자만)
	 */
	private String normalizePhoneNumber(String phoneNumber) {
		if (phoneNumber == null)
			return "";
		return phoneNumber.replaceAll("\\D", ""); // 숫자만 남김
	}

	/**
	 * 구매 항목 검증 메소드
	 *
	 * @param items     검증할 아이템 목록
	 * @param storeId   가게 ID
	 */
	private void validateItems(List<ItemDTO> items, Integer storeId) {
		if (items == null || items.isEmpty()) {
			throw new IllegalArgumentException("구매한 아이템 목록이 제공되지 않았습니다.");
		}

		for (ItemDTO item : items) {
			if (item == null || item.getDescription() == null || item.getDescription().trim().isEmpty()) {
				throw new IllegalArgumentException("아이템 정보가 유효하지 않습니다.");
			}

			menuRepository.findByNameAndStoreId(item.getDescription(), storeId)
				.orElseThrow(() -> new IllegalArgumentException("메뉴 항목이 유효하지 않습니다: " + item.getDescription()));
		}
	}

	/**
	 * 영수증 날짜 검증 메소드
	 *
	 * @param transactionDate 검증할 거래 날짜
	 */
	private void validateReceiptDate(LocalDateTime transactionDate) {
		if (transactionDate == null) {
			throw new IllegalArgumentException("영수증 날짜가 제공되지 않았습니다.");
		}

		LocalDateTime currentDate = LocalDateTime.now();
		long daysBetween = ChronoUnit.DAYS.between(transactionDate.toLocalDate(), currentDate.toLocalDate());

		if (daysBetween < 0) {
			throw new IllegalArgumentException("영수증 날짜가 미래입니다.");
		} else if (daysBetween > 10) {
			throw new IllegalArgumentException("영수증 인증 가능 기간이 지났습니다.");
		}
	}

	/**
	 * 영수증 인증 정보를 저장하는 메소드
	 *
	 * @param userEntity   인증하는 사용자
	 * @param storeEntity  인증된 가게
	 */
	private void saveReceiptVerification(UserEntity userEntity, StoreEntity storeEntity) {
		ReceiptVerificationEntity verificationEntity = new ReceiptVerificationEntity();
		verificationEntity.setUserAndStore(userEntity, storeEntity);
		receiptVerificationRepository.save(verificationEntity);
	}
}
