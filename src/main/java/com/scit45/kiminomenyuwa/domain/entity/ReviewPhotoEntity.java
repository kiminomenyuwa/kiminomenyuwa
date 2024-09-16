package com.scit45.kiminomenyuwa.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "review_photo") // 테이블 이름 수정
@Getter
@Setter
@NoArgsConstructor
public class ReviewPhotoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "photo_id", updatable = false, nullable = false, length = 36)
	private Integer photoId;

	@ManyToOne
	@JoinColumn(name = "review_id", nullable = false)
	private ReviewEntity review;

	@Column(name = "photo_url", nullable = false)
	private String photoUrl;

	@CreatedDate
	@Column(name = "uploaded_time")
	private LocalDateTime uploadedTime;
}