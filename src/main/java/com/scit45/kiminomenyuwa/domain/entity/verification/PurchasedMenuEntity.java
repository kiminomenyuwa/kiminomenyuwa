package com.scit45.kiminomenyuwa.domain.entity.verification;

import com.scit45.kiminomenyuwa.domain.entity.MenuEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "purchased_menu")
public class PurchasedMenuEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchased_menu_id")
	private Long id;

	/**
	 * 영수증 인증 정보
	 */
	@ManyToOne
	@JoinColumn(name = "receipt_verification_id", nullable = false)
	private ReceiptVerificationEntity receiptVerificationEntity;

	/**
	 * 구매한 메뉴 정보
	 */
	@ManyToOne
	@JoinColumn(name = "menu_id", nullable = false)
	private MenuEntity menu;

	/**
	 * 구매 수량
	 */
	@Column(name = "quantity")
	private Integer quantity;
}