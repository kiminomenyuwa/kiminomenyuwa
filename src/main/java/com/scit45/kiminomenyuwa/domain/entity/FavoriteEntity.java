package com.scit45.kiminomenyuwa.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "favorite", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "store_id"})})
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "favorite_id")
	private Integer favoriteId;  // 찜 항목의 고유 식별자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;  // 찜한 사용자의 엔티티 참조

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private StoreEntity store;  // 찜한 상점의 엔티티 참조

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;  // 찜한 날짜와 시간

	public FavoriteEntity(UserEntity user, StoreEntity store) {
		if (user == null || store == null) {
			throw new IllegalArgumentException("favorite: user " + user + ", store" + store);
		}

		this.user = user;
		this.store = store;
	}
}
