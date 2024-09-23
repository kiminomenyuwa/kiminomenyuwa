package com.scit45.kiminomenyuwa.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.scit45.kiminomenyuwa.domain.entity.FriendshipEntity;
import com.scit45.kiminomenyuwa.domain.entity.FriendshipEntity.FriendshipStatus;
import com.scit45.kiminomenyuwa.domain.repository.FriendshipRepository;
import com.scit45.kiminomenyuwa.domain.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 친구 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FriendshipService {

	private final FriendshipRepository friendshipRepository; // 친구 관계 관련 DB 처리를 담당하는 리포지토리
	private final UserRepository userRepository; // 사용자 관련 DB 처리를 담당하는 리포지토리

	/**
	 * 사용자 ID 존재 여부 확인
	 * @param userId 확인할 사용자 ID
	 * @return 사용자가 존재하면 true, 없으면 false 반환
	 */
	public boolean checkUserExists(String userId) {
		return userRepository.existsByUserId(userId);
	}

	/**
	 * 친구 요청 보내기
	 * @param userId 친구 요청을 보낸 사용자 ID
	 * @param friendId 친구 요청을 받을 사용자 ID
	 */
	public void sendFriendRequest(String userId, String friendId) {
		// 이미 친구 요청을 보냈는지 확인
		boolean exists = friendshipRepository.existsByUserIdAndFriendId(userId, friendId);
		if (exists) {
			throw new IllegalStateException("이미 친구 요청을 보냈습니다.");
		}

		// 새로운 친구 요청 생성 (PENDING 상태)
		FriendshipEntity friendshipRequest = FriendshipEntity.builder()
			.userId(userId)
			.friendId(friendId)
			.status(FriendshipStatus.PENDING)
			.build();

		// 친구 요청 저장
		friendshipRepository.save(friendshipRequest);
	}

	/**
	 * 모든 ACCEPTED 상태의 친구 목록을 가져오는 메서드
	 * @return ACCEPTED 상태의 친구 목록
	 */
	public List<FriendshipEntity> getAllAcceptedFriendships() {
		List<FriendshipEntity> friendships = friendshipRepository.findAllByStatus(FriendshipStatus.ACCEPTED);

		// 로그 출력
		log.debug("가져온 ACCEPTED 상태의 친구 수: {}", friendships.size());
		if (friendships.isEmpty()) {
			log.warn("ACCEPTED 상태의 친구가 없습니다.");
		} else {
			log.info("ACCEPTED 상태의 친구 목록이 성공적으로 조회되었습니다.");
		}

		return friendships;
	}

	/**
	 * 로그인한 사용자의 친구 목록을 중복 제거하여 가져오는 메서드
	 * @param loggedInUserId 로그인한 사용자 ID
	 * @return ACCEPTED 상태의 친구 수
	 */
	public int getFriendCount(String loggedInUserId) {
		List<FriendshipEntity> friendships = friendshipRepository.findByUserIdOrFriendId(loggedInUserId,
			loggedInUserId);

		// 중복된 친구 관계 제거를 위한 Set 사용
		Set<String> friendIds = new HashSet<>();

		for (FriendshipEntity friendship : friendships) {
			// ACCEPTED 상태일 때만 친구로 추가
			if (friendship.getStatus() == FriendshipStatus.ACCEPTED) {
				if (friendship.getUserId().equals(loggedInUserId)) {
					friendIds.add(friendship.getFriendId());
				} else {
					friendIds.add(friendship.getUserId());
				}
			}
		}

		// 친구 수 반환
		return friendIds.size();
	}

	/**
	 * 로그인한 사용자의 ACCEPTED 상태의 친구 목록만 가져오는 메서드
	 * @param loggedInUserId 로그인한 사용자 ID
	 * @return ACCEPTED 상태의 친구 목록 (중복 제거됨)
	 */
	public Set<String> getAcceptedFriends(String loggedInUserId) {
		List<FriendshipEntity> friendships = friendshipRepository.findAcceptedFriends(loggedInUserId);
		Set<String> acceptedFriends = new HashSet<>();

		// 친구 목록에서 자신을 제외하고 친구 추가
		for (FriendshipEntity friendship : friendships) {
			if (!friendship.getUserId().equals(loggedInUserId)) {
				acceptedFriends.add(friendship.getUserId());
			} else {
				acceptedFriends.add(friendship.getFriendId());
			}
		}

		return acceptedFriends;
	}

	/**
	 * 받은 친구 요청을 가져오는 메서드 (로그인한 사용자에게 온 친구 요청)
	 * @param loggedInUserId 로그인한 사용자 ID
	 * @return PENDING 상태의 친구 요청 목록
	 */
	public List<FriendshipEntity> getReceivedFriendRequests(String loggedInUserId) {
		return friendshipRepository.findByFriendIdAndStatus(loggedInUserId, FriendshipStatus.PENDING);
	}

	/**
	 * 보낸 친구 요청을 가져오는 메서드 (로그인한 사용자가 보낸 친구 요청)
	 * @param loggedInUserId 로그인한 사용자 ID
	 * @return PENDING 상태의 보낸 친구 요청 목록
	 */
	public List<FriendshipEntity> getSentFriendRequests(String loggedInUserId) {
		return friendshipRepository.findByUserIdAndStatus(loggedInUserId, FriendshipStatus.PENDING);
	}

	/**
	 * 친구 요청 취소 메서드
	 * @param loggedInUserId 로그인한 사용자 ID
	 * @param friendId 친구 요청을 취소할 상대방 ID
	 * @return 요청 취소 성공 여부
	 */
	public boolean cancelFriendRequest(String loggedInUserId, String friendId) {
		// PENDING 상태의 친구 요청 찾기
		Optional<FriendshipEntity> friendship = friendshipRepository.findByUserIdAndFriendIdAndStatus(
			loggedInUserId, friendId, FriendshipStatus.PENDING);

		if (friendship.isPresent()) {
			// 친구 요청 삭제
			friendshipRepository.delete(friendship.get());
			return true; // 취소 성공
		} else {
			log.warn("취소할 친구 요청을 찾을 수 없습니다.");
		}

		return false; // 취소 실패
	}

	/**
	 * 친구 요청 수락 메서드
	 * @param friendshipId 친구 요청 ID
	 * @param loggedInUserId 로그인한 사용자 ID
	 * @return 요청 수락 성공 여부
	 */
	public boolean acceptFriendRequest(Integer friendshipId, String loggedInUserId) {
		Optional<FriendshipEntity> friendship = friendshipRepository.findById(friendshipId);

		if (friendship.isPresent()) {
			FriendshipEntity request = friendship.get();

			// 친구 요청을 받은 사용자만 수락 가능
			if (request.getFriendId().equals(loggedInUserId) && request.getStatus() == FriendshipStatus.PENDING) {
				request.setStatus(FriendshipStatus.ACCEPTED);
				friendshipRepository.save(request);

				// 양방향 친구 관계 생성 (본인을 제외한 친구 요청자 추가)
				if (!request.getUserId().equals(loggedInUserId)) {
					FriendshipEntity reverseFriendship = FriendshipEntity.builder()
						.userId(request.getFriendId()) // 요청을 받은 사람을 요청자
						.friendId(request.getUserId()) // 요청 보낸 사람을 친구로
						.status(FriendshipStatus.ACCEPTED)
						.build();
					friendshipRepository.save(reverseFriendship); // 친구 관계 저장
				}

				return true;
			}
		}

		return false; // 수락 실패
	}

	/**
	 * 친구 요청 거절 메서드
	 * @param friendshipId 친구 요청 ID
	 * @param loggedInUserId 로그인한 사용자 ID
	 * @return 요청 거절 성공 여부
	 */
	public boolean declineFriendRequest(Integer friendshipId, String loggedInUserId) {
		Optional<FriendshipEntity> friendship = friendshipRepository.findById(friendshipId);

		if (friendship.isPresent()) {
			FriendshipEntity request = friendship.get();

			// 요청을 받은 사용자만 거절 가능
			if (request.getFriendId().equals(loggedInUserId) && request.getStatus() == FriendshipStatus.PENDING) {
				friendshipRepository.delete(request); // 친구 요청 삭제
				return true;
			}
		}
		return false; // 거절 실패
	}

	/**
	 * 두 사용자가 이미 친구인지 확인하는 메서드
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @return 이미 친구이면 true 반환
	 */
	public boolean isAlreadyFriend(String userId, String friendId) {
		return friendshipRepository.existsByUserIdAndFriendIdAndStatus(userId, friendId, FriendshipStatus.ACCEPTED)
			|| friendshipRepository.existsByUserIdAndFriendIdAndStatus(friendId, userId, FriendshipStatus.ACCEPTED);
	}

	/**
	 * 두 사용자가 친구 상태인지 확인하는 메서드
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @return 친구 상태면 true 반환
	 */
	public boolean areFriends(String userId, String friendId) {
		return friendshipRepository.existsByUserIdAndFriendIdAndStatus(userId, friendId, FriendshipStatus.ACCEPTED)
			|| friendshipRepository.existsByUserIdAndFriendIdAndStatus(friendId, userId, FriendshipStatus.ACCEPTED);
	}

	/**
	 * 두 사용자 간 친구 요청이 대기 중인지 확인하는 메서드
	 * @param userId 사용자 ID
	 * @param friendId 친구 ID
	 * @return 요청이 대기 중이면 true 반환
	 */
	public boolean hasPendingRequest(String userId, String friendId) {
		return friendshipRepository.existsByUserIdAndFriendIdAndStatus(userId, friendId, FriendshipStatus.PENDING)
			|| friendshipRepository.existsByUserIdAndFriendIdAndStatus(friendId, userId, FriendshipStatus.PENDING);
	}
}
