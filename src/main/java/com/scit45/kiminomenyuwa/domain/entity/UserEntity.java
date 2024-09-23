package com.scit45.kiminomenyuwa.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자의 정보를 저장하는 엔티티 클래스입니다.
 * 로그인 자격 증명 및 개인 정보를 포함합니다.
 */
@Getter
@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

	@Id
	@Column(name = "user_id")
	private String userId;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(name = "name")
	private String name;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false)
	private Gender gender;

	@Column(name = "email")
	private String email;

	@Column(name = "road_name_address")
	private String roadNameAddress;

	@Column(name = "detail_address")
	private String detailAddress;

	@Column(name = "zipcode")
	private String zipcode;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "profile_img_uuid")
	private String profileImgUuid;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Column(name = "enabled", nullable = false)
	private Boolean enabled;

	@Column(name = "monthly_budget")
	private Integer monthlyBudget; // 한 달 예산 필드 추가

	@CreatedDate
	@Column(name = "created_time")
	private LocalDateTime createdTime;
}