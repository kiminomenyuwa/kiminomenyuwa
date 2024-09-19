package com.scit45.kiminomenyuwa.domain.entity.verification;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.scit45.kiminomenyuwa.domain.entity.StoreEntity;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;

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

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@Table(name = "receipt_verification")
public class ReceiptVerificationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "receipt_verification_id")
	private Long id;

	/**
	 * 리뷰 작성자
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity reviewer;

	/**
	 * 리뷰를 작성할 가게
	 */
	@ManyToOne
	@JoinColumn(name = "store_id", nullable = false)
	private StoreEntity targetStore;

	/**
	 *  인증 시각
	 */
	@CreatedDate
	@Column(name = "verification_date")
	private LocalDateTime verificationDate;

	public void setUserAndStore(UserEntity userEntity, StoreEntity storeEntity) {
		this.reviewer = userEntity;
		this.targetStore = storeEntity;
	}
}