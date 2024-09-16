package com.scit45.kiminomenyuwa.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
import lombok.NoArgsConstructor;

/**
 * 사용자 리뷰를 저장하는 엔티티 클래스입니다.
 */
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "review")
@NoArgsConstructor
public class ReviewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Integer reviewId;

	/**
	 * 리뷰 대상 가게
	 */
	@ManyToOne
	@JoinColumn(name = "store_id", nullable = false)
	private StoreEntity store;

	/**
	 *	리뷰 작성자
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	/**
	 * 리뷰 점수는 1 ~ 5점, 정수
	 */
	@Column(name = "rating", nullable = false)
	private Byte rating;

	@Column(name = "comment")
	private String comment;

	@CreatedDate
	@Column(name = "created_time")
	private LocalDateTime createdTime;

	@LastModifiedDate
	@Column(name = "modified_time")
	private LocalDateTime modifiedTime;
}
