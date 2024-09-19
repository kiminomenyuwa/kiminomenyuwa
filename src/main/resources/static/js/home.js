$(document).ready(function() {
	// "게임 시작" 버튼을 클릭하면 게임을 시작하고, "좋아요/싫어요" 버튼을 표시
	$("#user-start-game").click(function() {
		$("#user-start-game").hide(); // "게임 시작" 버튼 숨기기
		$("#user-rating-controls").show(); // "좋아요/싫어요" 버튼 표시
		startGame(); // 게임 시작 함수 호출
	});

	// "싫어요" 버튼을 클릭하면 0점을 주고 메뉴 평가
	$("#user-dislike-button").click(function() {
		rateMenu(window.menuId, 0.0); // 0점으로 메뉴 평가
	});

	// "좋아요" 버튼 관련 변수 초기화
	const likeButton = document.getElementById("user-like-button"); // "좋아요" 버튼
	const starsContainer = likeButton.querySelector(".stars"); // 별점 컨테이너
	const stars = starsContainer.querySelectorAll(".star"); // 각 별 요소
	const buttonText = likeButton.firstChild; // "좋아요" 버튼의 텍스트 노드

	// "좋아요" 버튼에 마우스를 올렸을 때 별점 컨테이너 표시 및 텍스트 지움
	likeButton.addEventListener("mouseover", () => {
		starsContainer.style.display = "flex"; // 별점 컨테이너 표시
		buttonText.textContent = ""; // "좋아요" 텍스트 숨김
	});

	// "좋아요" 버튼에서 마우스를 뗐을 때 별점 컨테이너 숨김 및 텍스트 복원
	likeButton.addEventListener("mouseout", () => {
		starsContainer.style.display = "none"; // 별점 컨테이너 숨김
		buttonText.textContent = "좋아요"; // "좋아요" 텍스트 복원
		resetStars(); // 별점 초기화
	});

	// 별에 마우스를 올렸을 때 해당 별까지 채워지는 이벤트 처리
	stars.forEach((star) => {
		star.addEventListener("mouseover", function() {
			const value = parseInt(star.getAttribute("data-value")); // 별의 값 가져오기
			fillStars(value); // 해당 별까지 채우기
		});

		// 별을 클릭했을 때 별점 평가와 알림 표시
		star.addEventListener("click", function() {
			const value = parseInt(star.getAttribute("data-value")); // 클릭한 별의 값 가져오기
			alert(`${value}개의 별을 선택하셨습니다.`); // 선택된 별의 수를 알림으로 표시
			rateMenu(window.menuId, value); // 선택된 별의 수로 메뉴 평가
		});
	});

	// 별을 채우는 함수
	function fillStars(value) {
		resetStars(); // 별점 초기화
		stars.forEach((star) => {
			const starValue = parseInt(star.getAttribute("data-value"));
			if (starValue <= value) {
				star.classList.add("fill"); // 해당 별까지 채우기
			}
		});
	}

	// 별점 초기화 함수
	function resetStars() {
		stars.forEach((star) => {
			star.classList.remove("fill"); // 모든 별을 초기 상태로 되돌림
		});
	}

	// 게임 시작 함수: 랜덤 메뉴를 추천받고 화면에 표시
	function startGame() {
		$.post("/get-random-menu", function(menu) {
			if (menu) {
				$("#user-menu-header").text(menu.name); // 추천된 메뉴 이름을 화면에 표시
				window.menuId = menu.menuId; // 메뉴 ID를 전역 변수에 저장
			} else {
				alert("모든 메뉴를 평가하셨습니다!"); // 평가할 메뉴가 없을 때 알림
				$("#user-rating-controls").hide(); // "좋아요/싫어요" 버튼 숨기기
			}
		});
	}

	// 메뉴를 평가하고 다음 메뉴를 추천받는 함수
	function rateMenu(menuId, rating) {
		$.post("/rate-menu", { menuId: menuId, rating: rating }, function(menu) {
			if (menu) {
				$("#user-menu-header").text(menu.name); // 추천된 새로운 메뉴 이름을 화면에 표시
				window.menuId = menu.menuId; // 새로운 메뉴 ID를 전역 변수에 저장
			} else {
				alert("모든 메뉴를 평가하셨습니다!"); // 평가할 메뉴가 없을 때 알림
				$("#user-rating-controls").hide(); // "좋아요/싫어요" 버튼 숨기기
			}
		}).fail(function() {
			alert("오류가 발생했습니다. 다시 시도해주세요."); // 요청 실패 시 오류 알림
		});
	}
});
