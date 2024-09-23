package com.scit45.kiminomenyuwa.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scit45.kiminomenyuwa.domain.entity.FriendshipEntity;
import com.scit45.kiminomenyuwa.domain.entity.FriendshipEntity.FriendshipStatus;

/**
 * 친구 관계(Friendship) 엔티티를 처리하는 리포지토리 인터페이스
 */
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Integer> {

	/**
	 * 상태에 따른 친구 목록을 가져오는 메서드
	 * @param status 친구 관계 상태 (ACCEPTED, PENDING 등)
	 * @return 해당 상태에 따른 친구 목록 반환
	 */
	List<FriendshipEntity> findAllByStatus(FriendshipStatus status);

	/**
	 * 사용자 ID와 친구 ID로 친구 관계를 조회하는 메서드
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @return 해당하는 친구 관계 목록 반환
	 */
	List<FriendshipEntity> findByUserIdOrFriendId(String userId, String friendId);

	/**
	 * 특정 사용자와 친구 관계 중 ACCEPTED 상태인 친구 관계 조회
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @param status ACCEPTED 상태의 친구 관계
	 * @return ACCEPTED 상태의 친구 관계 목록 반환
	 */
	List<FriendshipEntity> findByUserIdOrFriendIdAndStatus(String userId, String friendId,
		FriendshipEntity.FriendshipStatus status);

	/**
	 * 특정 사용자와 친구 간의 관계가 존재하는지 확인
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @return 존재하면 true, 없으면 false 반환
	 */
	boolean existsByUserIdAndFriendId(String userId, String friendId);

	/**
	 * ACCEPTED 또는 PENDING 상태인 친구 요청 목록을 가져오는 메서드
	 * @param userId 사용자 ID
	 * @param statuses ACCEPTED 또는 PENDING 상태의 친구 요청 목록
	 * @return 해당 상태의 친구 관계 목록 반환
	 */
	@Query("SELECT f FROM FriendshipEntity f WHERE f.userId = :userId AND f.status IN :statuses")
	List<FriendshipEntity> findAllByUserIdAndStatusIn(@Param("userId")
	String userId, @Param("statuses")
	List<FriendshipStatus> statuses);

	/**
	 * 사용자 ID와 친구 ID로 특정 친구 관계를 조회하는 메서드
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @return 해당하는 친구 관계 반환
	 */
	Optional<FriendshipEntity> findByUserIdAndFriendId(String userId, String friendId);

	/**
	 * 사용자 ID와 친구 ID로 특정 상태의 친구 요청을 찾는 메서드
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @param status 찾고자 하는 상태 (ACCEPTED, PENDING 등)
	 * @return 해당 상태의 친구 관계 반환
	 */
	@Query("SELECT f FROM FriendshipEntity f WHERE ((f.userId = :userId AND f.friendId = :friendId) OR (f.userId = :friendId AND f.friendId = :userId)) AND f.status = :status")
	Optional<FriendshipEntity> findByUserIdAndFriendIdAndStatus(@Param("userId")
	String userId, @Param("friendId")
	String friendId, @Param("status")
	FriendshipStatus status);

	/**
	 * 사용자 ID와 친구 ID로 특정 상태의 친구 관계를 삭제하는 메서드
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @param status 삭제하고자 하는 상태
	 */
	@Modifying
	@Query("DELETE FROM FriendshipEntity f WHERE f.userId = :userId AND f.friendId = :friendId AND f.status = :status")
	void deleteByUserIdAndFriendIdAndStatus(@Param("userId")
	String userId, @Param("friendId")
	String friendId, @Param("status")
	FriendshipStatus status);

	/**
	 * 특정 사용자가 받은 친구 요청 목록을 가져오는 메서드
	 * @param friendId 친구 요청을 받은 사용자 ID
	 * @param status 친구 요청의 상태 (PENDING)
	 * @return 받은 친구 요청 목록 반환
	 */
	List<FriendshipEntity> findByFriendIdAndStatus(String friendId, FriendshipStatus status);

	/**
	 * 특정 사용자가 보낸 친구 요청 목록을 가져오는 메서드
	 * @param userId 친구 요청을 보낸 사용자 ID
	 * @param status 친구 요청의 상태 (PENDING)
	 * @return 보낸 친구 요청 목록 반환
	 */
	List<FriendshipEntity> findByUserIdAndStatus(String userId, FriendshipStatus status);

	/**
	 * ACCEPTED 상태의 친구 목록을 조회하는 쿼리 메서드
	 * @param loggedInUserId 로그인한 사용자 ID
	 * @return ACCEPTED 상태의 친구 목록 반환
	 */
	@Query("SELECT f FROM FriendshipEntity f WHERE f.status = 'ACCEPTED' AND (f.userId = :loggedInUserId OR f.friendId = :loggedInUserId)")
	List<FriendshipEntity> findAcceptedFriends(@Param("loggedInUserId")
	String loggedInUserId);

	/**
	 * ACCEPTED 상태의 친구 관계만을 가져오는 메서드 (로그인한 사용자를 제외)
	 * @param loggedInUserId 로그인한 사용자 ID
	 * @return ACCEPTED 상태의 친구 목록 반환 (로그인한 사용자 제외)
	 */
	@Query("SELECT f FROM FriendshipEntity f WHERE (f.userId = :loggedInUserId OR f.friendId = :loggedInUserId) AND f.status = com.scit45.kiminomenyuwa.domain.entity.FriendshipEntity.FriendshipStatus.ACCEPTED")
	List<FriendshipEntity> findByUserIdOrFriendIdAndStatus(@Param("loggedInUserId")
	String loggedInUserId);

	/**
	 * 친구 요청 ID로 친구 요청을 찾는 메서드
	 * @param friendshipId 친구 요청 ID
	 * @return 해당 친구 요청 반환
	 */
	@Override
	Optional<FriendshipEntity> findById(Integer friendshipId);

	/**
	 * 특정 사용자와 친구 관계가 ACCEPTED 상태인지 확인
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @param status ACCEPTED 상태
	 * @return 존재하면 true, 없으면 false 반환
	 */
	boolean existsByUserIdAndFriendIdAndStatus(String userId, String friendId,
		FriendshipEntity.FriendshipStatus status);

	/**
	 * 특정 친구 ID와 사용자 ID, 상태로 친구 관계가 존재하는지 확인
	 * @param friendId 친구 ID
	 * @param userId 사용자 ID
	 * @param status 친구 관계 상태 (ACCEPTED 등)
	 * @return 존재하면 true, 없으면 false 반환
	 */
	boolean existsByFriendIdAndUserIdAndStatus(String friendId, String userId,
		FriendshipEntity.FriendshipStatus status);

	/**
	 * ACCEPTED 상태의 친구 목록을 가져오되, 로그인한 사용자는 제외하는 메서드
	 * @param loggedInUserId 로그인한 사용자 ID
	 * @return ACCEPTED 상태의 친구 목록 (로그인한 사용자 제외)
	 */
	@Query("SELECT f FROM FriendshipEntity f WHERE (f.userId = :loggedInUserId OR f.friendId = :loggedInUserId) AND f.status = 'ACCEPTED' AND f.friendId <> :loggedInUserId")
	List<FriendshipEntity> findAcceptedFriendsExcludingSelf(@Param("loggedInUserId")
	String loggedInUserId);
}
