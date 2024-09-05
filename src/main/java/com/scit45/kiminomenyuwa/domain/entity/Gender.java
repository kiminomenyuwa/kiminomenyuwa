package com.scit45.kiminomenyuwa.domain.entity;

public enum Gender {
	// DB에는 소문자로 저장되어 있었기 때문에 원래 MALE/FEMALE로 되어 있어서 오류가 있어서 entity에서 소문자로 변경
	male,
	female
}