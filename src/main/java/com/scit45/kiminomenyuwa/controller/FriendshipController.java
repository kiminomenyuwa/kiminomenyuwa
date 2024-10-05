package com.scit45.kiminomenyuwa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scit45.kiminomenyuwa.domain.dto.FriendDTO;
import com.scit45.kiminomenyuwa.domain.dto.recommendation.UserSimpleDTO;
import com.scit45.kiminomenyuwa.domain.entity.UserEntity;
import com.scit45.kiminomenyuwa.security.AuthenticatedUser;
import com.scit45.kiminomenyuwa.service.FriendshipService;
import com.scit45.kiminomenyuwa.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 친구 요청 및 친구 목록과 관련된 기능을 처리하는 컨트롤러
 */
@Controller
@RequestMapping("/friendships")
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성
@Slf4j
public class FriendshipController {

	private final FriendshipService friendshipService; // 친구 관련 로직을 처리하는 서비스
	private final UserService userService; // 사용자 관련 로직을 처리하는 서비스

	/**
	 * 친구 목록 페이지로 이동
	 * @param model 모델에 친구 목록과 요청 상태를 담아 뷰로 전달
	 * @return friendList 뷰로 이동
	 */
	@GetMapping("/list")
	public String showFriendList(Model model) {
		// 로그인한 사용자 ID 가져오기
		String loggedInUserId = getLoggedInUserId();

		// 받은 친구 요청 목록 가져오기
		// 받은 친구 요청 목록 처리 - FriendshipEntity에서 친구 정보를 추출한 후 FriendDTO로 변환
		List<FriendDTO> receivedRequests = friendshipService.getReceivedFriendRequests(loggedInUserId).stream()
			.map(friendshipEntity -> {
				// FriendshipEntity에서 친구 ID 추출
				String friendId = friendshipEntity.getUserId(); // 친구 요청을 보낸 사용자의 ID

				// 친구 ID로 사용자 정보를 조회
				Optional<UserEntity> friendEntity = userService.findByUserId(friendId);
				if (friendEntity.isPresent()) {
					String profileImg = (friendEntity.get().getProfileImgUuid() != null
						&& !friendEntity.get().getProfileImgUuid().isEmpty())
							? "/files/" + friendEntity.get().getProfileImgUuid()
							: "/images/default-profile.png";

					// FriendDTO에 friendshipId 추가
					return FriendDTO.builder()
						.friendshipId(friendshipEntity.getFriendshipId()) // friendshipId를 포함
						.userId(friendshipEntity.getUserId()) // 요청 보낸 사용자 ID
						.friendId(friendEntity.get().getUserId())
						.friendName(friendEntity.get().getName())
						.friendProfileImg(profileImg)
						.status(friendshipEntity.getStatus()) // 상태도 포함
						.build();
				} else {
					return null; // 친구 정보를 찾을 수 없을 때 null 반환
				}
			})
			.filter(Objects::nonNull) // null 값 필터링
			.collect(Collectors.toList());

		// 모델에 받은 요청 추가
		model.addAttribute("receivedRequests", receivedRequests);

		// 보낸 친구 요청 목록 처리 - FriendshipEntity에서 친구 ID를 추출한 후 사용
		List<FriendDTO> sentRequests = friendshipService.getSentFriendRequests(loggedInUserId).stream()
			.map(friendshipEntity -> {
				// FriendshipEntity에서 친구 ID 추출 (올바른 필드 이름 확인)
				String friendId = friendshipEntity.getFriendId(); // 여기에 정확한 필드명 사용

				// 친구 ID로 사용자 정보를 조회
				Optional<UserEntity> friendEntity = userService.findByUserId(friendId);
				if (friendEntity.isPresent()) {
					String profileImg = (friendEntity.get().getProfileImgUuid() != null
						&& !friendEntity.get().getProfileImgUuid().isEmpty())
							? "/files/" + friendEntity.get().getProfileImgUuid()
							: "/images/default-profile.png";

					// FriendDTO 생성
					return FriendDTO.builder()
						.friendId(friendEntity.get().getUserId())
						.friendName(friendEntity.get().getName())
						.friendProfileImg(profileImg)
						.build();
				} else {
					return null; // 친구 정보를 찾을 수 없을 때 null 반환
				}
			})
			.filter(Objects::nonNull) // null 값 필터링
			.collect(Collectors.toList());

		// 수락된 친구 목록 가져오기
		Set<String> acceptedFriendIds = friendshipService.getAcceptedFriends(loggedInUserId);

		// 수락된 친구 ID를 바탕으로 FriendDTO 생성
		List<FriendDTO> acceptedFriends = acceptedFriendIds.stream()
			.map(friendId -> {
				// 친구 ID로 사용자 정보를 조회
				Optional<UserEntity> friendEntity = userService.findByUserId(friendId);
				if (friendEntity.isPresent()) {
					String profileImg = (friendEntity.get().getProfileImgUuid() != null
						&& !friendEntity.get().getProfileImgUuid().isEmpty())
							? "/files/" + friendEntity.get().getProfileImgUuid()
							: "/images/default-profile.png";

					// FriendDTO 생성
					return FriendDTO.builder()
						.friendId(friendEntity.get().getUserId())
						.friendName(friendEntity.get().getName())
						.friendProfileImg(profileImg)
						.build();
				} else {
					// 만약 친구 정보가 없을 경우, null을 반환
					return null;
				}
			})
			.filter(Objects::nonNull) // null 값 필터링
			.collect(Collectors.toList());

		// 모델에 각각의 목록 추가 (수락된 친구 목록, 받은 요청, 보낸 요청)
		model.addAttribute("receivedRequests", receivedRequests);
		model.addAttribute("sentRequests", sentRequests); // 보낸 요청 목록 추가
		model.addAttribute("acceptedFriends", acceptedFriends);

		return "friendshipView/friendList"; // friendList 뷰로 이동
	}

	/**
	 * 아이디로 사용자 정보를 확인하는 메서드
	 * @param friendId 조회할 사용자 ID
	 * @return 사용자 존재 여부, 친구 상태, 요청 상태 등을 담은 JSON 응답
	 */
	@GetMapping("/checkUser")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> checkUser(@RequestParam("friendId")
	String friendId) {
		// 현재 로그인한 사용자 ID 가져오기
		String loggedInUserId = getLoggedInUserId();
		Map<String, Object> response = new HashMap<>();

		// 본인 확인 (본인 아이디를 검색한 경우)
		if (loggedInUserId.equals(friendId)) {
			// 본인 정보 조회
			Optional<UserEntity> currentUserOpt = userService.findByUserId(loggedInUserId);
			if (currentUserOpt.isPresent()) {
				UserEntity currentUser = currentUserOpt.get();
				String profileImg = (currentUser.getProfileImgUuid() != null
					&& !currentUser.getProfileImgUuid().isEmpty())
						? "/files/" + currentUser.getProfileImgUuid()
						: "/images/default-profile.png";

				response.put("exists", true);
				response.put("isSelf", true);
				response.put("message", "본인입니다.");
				response.put("name", currentUser.getName());
				response.put("profileImg", profileImg);
				response.put("isFriend", false); // 본인은 자기 자신과 친구 관계가 아니므로 false
				response.put("hasPendingRequest", false); // 본인은 자기 자신에게 요청을 보낼 수 없으므로 false
			} else {
				// 로그인한 사용자가 존재하지 않는 경우 (비정상적인 상황)
				response.put("exists", false);
				response.put("message", "사용자를 찾을 수 없습니다.");
			}
		} else {
			// 사용자 정보 조회
			Optional<UserEntity> user = userService.findByUserId(friendId);

			if (user.isPresent()) {
				// 친구 관계 및 요청 상태 확인
				boolean isFriend = friendshipService.areFriends(loggedInUserId, friendId);
				boolean hasPendingRequest = friendshipService.hasPendingRequest(loggedInUserId, friendId);

				// 프로필 사진이 없는 경우 기본 이미지 경로 설정
				String profileImg = (user.get().getProfileImgUuid() != null
					&& !user.get().getProfileImgUuid().isEmpty())
						? "/files/" + user.get().getProfileImgUuid()
						: "/images/default-profile.png";

				// 사용자 정보 및 상태 응답에 추가
				response.put("exists", true);
				response.put("isSelf", false);
				response.put("isFriend", isFriend);
				response.put("hasPendingRequest", hasPendingRequest);
				response.put("name", user.get().getName()); // 사용자 이름 추가
				response.put("profileImg", profileImg);

				// 상태에 따른 메시지 추가
				if (isFriend) {
					response.put("message", "이미 친구입니다.");
				} else if (hasPendingRequest) {
					response.put("message", "이미 친구 요청을 보냈습니다.");
				} else {
					response.put("message", "친구 요청을 보낼 수 있습니다.");
				}
			} else {
				// 사용자 존재하지 않을 경우
				response.put("exists", false);
				response.put("message", "존재하지 않는 아이디입니다.");
			}
		}

		// 응답 반환
		return ResponseEntity.ok(response);
	}

	/**
	 * 친구 요청을 보내는 API
	 * @param friendId 친구 요청을 보낼 사용자 ID
	 * @return 친구 요청 성공 메시지 응답
	 */
	@PostMapping("/sendRequest")
	public ResponseEntity<String> sendFriendRequest(@RequestParam("friendId")
	String friendId) {
		// 로그인한 사용자 ID 가져오기
		String loggedInUserId = getLoggedInUserId();
		// 친구 요청 처리
		friendshipService.sendFriendRequest(loggedInUserId, friendId);
		return ResponseEntity.ok("친구 요청을 보냈습니다.");
	}

	/**
	 * 현재 로그인한 사용자 ID를 가져오는 메서드
	 * @return 로그인한 사용자 ID
	 */
	private String getLoggedInUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			return ((UserDetails)authentication.getPrincipal()).getUsername();
		}
		return null;
	}

	/**
	 * 친구 요청을 취소하는 API
	 * @param friendId 친구 요청을 취소할 사용자 ID
	 * @return 친구 요청 취소 성공 메시지 응답
	 */
	@PostMapping("/cancelRequest")
	public ResponseEntity<String> cancelFriendRequest(@RequestParam("friendId")
	String friendId) {
		// 로그인한 사용자 ID 가져오기
		String loggedInUserId = getLoggedInUserId();
		// 친구 요청 취소 처리
		friendshipService.cancelFriendRequest(loggedInUserId, friendId);
		return ResponseEntity.ok("친구 요청이 취소되었습니다.");
	}

	/**
	 * 친구 요청을 수락하는 API
	 * @param friendshipId 수락할 친구 요청 ID
	 * @return 친구 요청 수락 성공 메시지 응답
	 */
	@PostMapping("/acceptRequest")
	public ResponseEntity<String> acceptFriendRequest(@RequestParam("friendshipId")
	Integer friendshipId) {
		// 로그인한 사용자 ID 가져오기
		String loggedInUserId = getLoggedInUserId();
		// 친구 요청 수락 처리
		boolean success = friendshipService.acceptFriendRequest(friendshipId, loggedInUserId);

		if (success) {
			return ResponseEntity.ok("친구 요청을 수락했습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구 요청 수락에 실패했습니다.");
		}
	}

	/**
	 * 친구 요청을 거절하는 API
	 * @param friendshipId 거절할 친구 요청 ID
	 * @return 친구 요청 거절 성공 메시지 응답
	 */
	@PostMapping("/declineRequest")
	public ResponseEntity<String> declineFriendRequest(@RequestParam("friendshipId")
	Integer friendshipId) {
		// 로그인한 사용자 ID 가져오기
		String loggedInUserId = getLoggedInUserId();
		// 친구 요청 거절 처리
		boolean success = friendshipService.declineFriendRequest(friendshipId, loggedInUserId);

		if (success) {
			return ResponseEntity.ok("친구 요청을 거절했습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구 요청 거절에 실패했습니다.");
		}
	}

	/**
	 * 친구 삭제 요청을 처리하는 메서드.
	 *
	 * @param friendId 삭제할 친구의 ID
	 * @return 삭제 성공 시 HTTP 200 OK와 메시지 반환, 실패 시 HTTP 400 BAD REQUEST 반환
	 */
	@PostMapping("/deleteFriend")
	public ResponseEntity<String> deleteFriend(@RequestParam("friendId")
	String friendId) {
		String loggedInUserId = getLoggedInUserId();
		boolean success = friendshipService.deleteFriendship(loggedInUserId, friendId);

		if (success) {
			return ResponseEntity.ok("친구가 삭제되었습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("친구 삭제에 실패했습니다.");
		}
	}

	/**
	 * 친구 검색 엔드포인트
	 * @param query 검색어 (이름 또는 이메일)
	 * @param user 현재 로그인한 사용자 정보
	 * @return 검색된 친구 목록
	 */
	@GetMapping("/search")
	@ResponseBody
	public ResponseEntity<List<UserSimpleDTO>> searchFriends(
		@RequestParam("query") String query,
		@AuthenticationPrincipal AuthenticatedUser user) {

		String currentUserId = user.getUsername();
		List<UserSimpleDTO> friends = friendshipService.searchFriends(currentUserId, query);
		return ResponseEntity.ok(friends);
	}
}
