package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {

	// 가게 이름을 기준으로 검색 (부분 일치)
	List<StoreEntity> findByNameContainingIgnoreCase(String name);

	// 도로명 주소를 기준으로 검색 (부분 일치)
	List<StoreEntity> findByRoadNameAddressContainingIgnoreCase(String roadNameAddress);

	// 상세 주소를 기준으로 검색 (부분 일치)
	List<StoreEntity> findByDetailAddressContainingIgnoreCase(String detailAddress);

	// 전화번호를 기준으로 검색 (부분 일치)
	List<StoreEntity> findByPhoneNumberContaining(String phoneNumber);

	/**
	 * 가게 이름, 도로명 주소, 상세 주소, 전화번호를 기준으로 두 개 이상의 필드가 일치하는 가게를 검색
	 *
	 * @param name            가게 이름
	 * @param roadNameAddress 도로명 주소
	 * @param detailAddress   상세 주소
	 * @param phoneNumber     전화번호
	 * @return 잠재적으로 일치할 수 있는 가게 목록
	 */
	@Query("SELECT s FROM StoreEntity s WHERE " +
		"LOWER(REPLACE(s.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:name, ' ', ''), '%')) AND " +
		"(LOWER(REPLACE(s.roadNameAddress, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:roadNameAddress, ' ', ''), '%')) OR "
		+
		"LOWER(REPLACE(s.detailAddress, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:detailAddress, ' ', ''), '%')) OR " +
		"s.phoneNumber LIKE CONCAT('%', :phoneNumber, '%'))")
	List<StoreEntity> findPotentialMatches(
		@Param("name") String name,
		@Param("roadNameAddress") String roadNameAddress,
		@Param("detailAddress") String detailAddress,
		@Param("phoneNumber") String phoneNumber);
}