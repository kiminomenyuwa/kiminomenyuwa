package com.scit45.kiminomenyuwa.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 친구 관계(Friendship) 엔티티
 * 각 사용자 간의 친구 요청 상태와 그에 대한 정보를 저장하는 테이블로 매핑되는 클래스
 */
@Entity
@Table(name = "friendships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendshipEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성되는 기본 키
	private Integer friendshipId; // 친구 관계 식별자 (Primary Key)

	@Column(name = "user_id", nullable = false) // user_id 필드는 NULL 값을 가질 수 없음
	private String userId; // 친구 요청을 보낸 사용자 ID

	@Column(name = "friend_id", nullable = false) // friend_id 필드도 NULL 값을 가질 수 없음
	private String friendId; // 친구 요청을 받은 사용자 ID

	@Enumerated(EnumType.STRING) // ENUM 타입을 문자열로 DB에 저장
	@Column(nullable = false) // status 필드도 NULL을 허용하지 않음
	private FriendshipStatus status; // 친구 관계 상태 (PENDING, ACCEPTED 등)

	/**
	 * 친구 관계 상태를 나타내는 ENUM
	 * PENDING - 친구 요청 대기 중
	 * ACCEPTED - 친구 요청 수락됨
	 * DECLINED - 친구 요청 거절됨
	 * BLOCKED - 친구가 차단됨
	 */
	public enum FriendshipStatus {
		PENDING, ACCEPTED, DECLINED, BLOCKED
	}
}
