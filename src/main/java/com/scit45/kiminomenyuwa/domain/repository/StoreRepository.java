package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {

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

	StoreEntity findByName(String name);

	/**
	 * 특정 위치 주변의 상점을 검색합니다.
	 *
	 * @param pointWKT 중심점의 WKT 형식 포인트
	 * @param radius   반경 (미터 단위)
	 * @return 반경 내의 상점 목록
	 */
	@Query(value = "SELECT s.* FROM store s " +
		"WHERE ST_Distance_Sphere(s.location, ST_GeomFromText(:point)) <= :radius " +
		"ORDER BY ST_Distance_Sphere(s.location, ST_GeomFromText(:point)) ASC", nativeQuery = true)
	List<StoreEntity> findStoresWithinRadius(
		@Param("point") String pointWKT,
		@Param("radius") double radius);

	/**
	 * 상점 이름과 카테고리로 상점을 검색합니다.
	 *
	 * @param name     상점 이름 (옵션)
	 * @param category 상점 카테고리 (옵션)
	 * @return 검색된 상점 목록
	 */
	@Query("SELECT s FROM StoreEntity s " +
		"WHERE (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
		"AND (:category IS NULL OR s.category = :category)")
	List<StoreEntity> findStoresByNameAndCategory(@Param("name") String name, @Param("category") String category);

	Page<StoreEntity> findByUser_UserId(String userId, Pageable pageable);
}
