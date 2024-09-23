package com.scit45.kiminomenyuwa.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.scit45.kiminomenyuwa.domain.entity.Gender;
import com.scit45.kiminomenyuwa.domain.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 용 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	String userId; //회원 아이디
	String passwordHash; //비밀번호
	String name; //이름
	LocalDate birthDate; //생년월일
	Gender gender; //성별
	String email; //이메일
	String zipcode; //우편번호
	String roadNameAddress; //도로명주소
	String detailAddress; //상세주소
	String phoneNumber; //핸드폰 번호
	String profileImgUuid; //프로필 사진 주소
	Role role; //계정 등급
	Boolean enabled; //계정 활성화 여부
	LocalDateTime createdTime; //계정 생성 시각

}