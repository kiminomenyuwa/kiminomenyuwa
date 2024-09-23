package com.scit45.kiminomenyuwa.domain.entity;

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

@Entity
@Table(name = "budget")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class BudgetEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer budgetId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "month", nullable = false)
	private Integer month;

	@Column(name = "year", nullable = false)
	private Integer year;

	@Column(name = "budget", nullable = false)
	private Integer budget;
}
