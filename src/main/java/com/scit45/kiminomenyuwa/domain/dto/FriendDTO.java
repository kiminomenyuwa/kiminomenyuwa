package com.scit45.kiminomenyuwa.domain.dto;

import com.scit45.kiminomenyuwa.domain.entity.FriendshipEntity.FriendshipStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 친구 데이터 전송 객체 (DTO)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendDTO {
	private Integer friendshipId; // 친구 관계 식별자
	private String userId; // 친구 요청을 보낸 사용자 ID
	private String friendId; // 친구 요청을 받은 사용자 ID
	private FriendshipStatus status; // 친구 관계 상태
	private String friendName; // 친구의 이름
	private String friendProfileImg; // 친구의 프로필 이미지 URL
}
