package com.scit45.kiminomenyuwa.domain.entity;

import org.locationtech.jts.geom.Geometry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자가 소유한 상점 정보를 저장하는 엔티티 클래스입니다.
 */
@Data
@Entity
@Table(name = "store")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id")
	private Integer storeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "certification", nullable = false)
	private String certification;

	@Column(name = "road_name_address")
	private String roadNameAddress;

	@Column(name = "detail_address")
	private String detailAddress;

	@Column(name = "zipcode")
	private String zipcode;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "category")
	private String category;

	@Column(name = "description")
	private String description;

	@Column(name = "enabled", nullable = false)
	private Boolean enabled;

	// 공간 정보 필드 추가
	@Column(name = "location", columnDefinition = "POINT")
	private Geometry location;
}