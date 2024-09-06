package com.scit45.kiminomenyuwa.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

/**
 * 프로필 사진을 처리하기 위한 엔티티입니다
 */
@Entity
@Table(name = "profile_photo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilePhotoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "photo_id")
	private Integer photoId;  // 사진의 고유 식별자

	// 회원가입 하기 전에 사진을 올리기 때문에 단순히 String으로 받음
	// 받는 파라미터: joinForm.html의 userId
	@Column(name = "user_id", nullable = false)
	private String userId;  // 사진을 올린 사용자의 ID

	@Column(name = "original_name", nullable = false, length = 100)
	private String originalName;  // 원본 파일명

	@Column(name = "saved_name", nullable = false, length = 100)
	private String savedName;  // UUID와 업로드 시간을 조합한 저장 파일명

	@CreatedDate
	@Column(name = "upload_date", nullable = false)
	private LocalDateTime uploadDate;  // 업로드 날짜
}