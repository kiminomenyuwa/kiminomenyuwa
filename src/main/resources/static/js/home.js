$(document).ready(function () {
  // 위치 관련 변수 선언
  let latitude, longitude;
  let userLatitude, userLongitude;

  // 페이지 로드 시 위치 정보 가져오기
  getLocation();

  // 사용자의 현재 위치를 가져오는 함수
  function getLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        function (position) {
          latitude = position.coords.latitude;
          longitude = position.coords.longitude;
          userLatitude = latitude;
          userLongitude = longitude;
          getAddressFromCoords(latitude, longitude, "#current-address"); // 비회원용 주소 가져오기
          getAddressFromCoords(
            userLatitude,
            userLongitude,
            "#user-current-address"
          ); // 회원용 주소 가져오기
        },
        function () {
          alert("위치를 불러오는데 실패했습니다.");
        }
      );
    } else {
      alert("Geolocation을 지원하지 않는 브라우저입니다.");
    }
  }

  // 네이버 지도 API를 이용하여 좌표를 주소로 변환하는 함수
  function getAddressFromCoords(lat, lng, addressElementId) {
    const coords = new naver.maps.LatLng(lat, lng);
    naver.maps.Service.reverseGeocode(
      {
        coords: coords,
      },
      function (status, response) {
        if (status !== naver.maps.Service.Status.OK) {
          return alert("주소를 가져오는데 실패했습니다.");
        }
        const result = response.v2;
        const address =
          result.address.jibunAddress || result.address.roadAddress;
        $(addressElementId).text(`현재 위치: ${address}`);
      }
    );
  }

  // 비회원용 "게임 시작" 버튼 클릭 이벤트 처리
  $("#start-game").click(function () {
    const radius = $("#radius-select").val(); // 선택된 반경 값을 가져옴

    if (!latitude || !longitude) {
      alert("위치 정보를 가져오지 못했습니다.");
      return;
    }

    $.post(
      "/get-random-menu-by-location",
      {
        latitude: latitude,
        longitude: longitude,
        radius: radius,
      },
      function (menu) {
        if (menu) {
          $("#menu-header").text(menu.name); // 선택된 메뉴의 이름을 화면에 표시
          $("#menu-image").attr("src", menu.pictureUrl).show(); // 이미지 표시
          $("#start-game").text("다시 시작");
        } else {
          alert("반경 내에 더 이상 메뉴가 없습니다.");
        }
      }
    ).fail(function (xhr, status, error) {
      alert("메뉴를 가져오는 데 실패했습니다.");
      console.log(status, error);
    });
  });

  // 회원용 "게임 시작" 버튼 클릭 이벤트 처리
  $("#user-start-game").click(function () {
    $("#user-start-game").hide(); // "게임 시작" 버튼 숨기기
    $("#user-rating-controls").show(); // "좋아요/싫어요" 버튼 표시
    startGame(); // 게임 시작 함수 호출
  });

  // "싫어요" 버튼을 클릭하면 0점을 주고 메뉴 평가
  $("#user-dislike-button").click(function () {
    rateMenu(window.menuId, 0.0); // 0점으로 메뉴 평가
  });

  // "좋아요" 버튼 관련 변수 초기화
  const likeButton = document.getElementById("user-like-button"); // "좋아요" 버튼
  const starsContainer = likeButton.querySelector(".stars"); // 별점 컨테이너
  const stars = starsContainer.querySelectorAll(".star"); // 각 별 요소
  const buttonText = likeButton.firstChild; // "좋아요" 버튼의 텍스트 노드
  const fixedRatingCheckbox = document.getElementById("fixed-rating-checkbox"); // 3점 고정 체크박스

  // "좋아요" 버튼 클릭 이벤트
  likeButton.addEventListener("click", () => {
    if (fixedRatingCheckbox.checked) {
      // 체크박스가 선택된 경우 3점으로 평가
      rateMenu(window.menuId, 3); // 3점으로 평가
    } else {
      // 체크박스가 선택되지 않은 경우 기존 별점 기능을 활성화
      starsContainer.style.display = "flex"; // 별점 컨테이너 표시
      buttonText.textContent = ""; // "좋아요" 텍스트 숨김
    }
  });

  // "좋아요" 버튼에 마우스를 올렸을 때 별점 컨테이너 표시 및 텍스트 지움
  likeButton.addEventListener("mouseover", () => {
    if (!fixedRatingCheckbox.checked) {
      starsContainer.style.display = "flex";
      buttonText.textContent = "";
    }
  });

  // "좋아요" 버튼에서 마우스를 뗐을 때 별점 컨테이너 숨김 및 텍스트 복원
  likeButton.addEventListener("mouseout", () => {
    if (!fixedRatingCheckbox.checked) {
      starsContainer.style.display = "none";
      buttonText.textContent = "좋아요";
      resetStars();
    }
  });

  // 별점에 마우스를 올렸을 때 별 채우기
  stars.forEach((star) => {
    star.addEventListener("mouseover", function () {
      const value = parseInt(star.getAttribute("data-value"));
      fillStars(value);
    });

    // 별점 클릭 시 평가
    star.addEventListener("click", function () {
      const value = parseInt(star.getAttribute("data-value"));
      alert(`${value}개의 별을 선택하셨습니다.`);
      rateMenu(window.menuId, value);
    });
  });

  // 별점 채우는 함수
  function fillStars(value) {
    resetStars();
    stars.forEach((star) => {
      const starValue = parseInt(star.getAttribute("data-value"));
      if (starValue <= value) {
        star.classList.add("fill");
      }
    });
  }

  // 별점 초기화 함수
  function resetStars() {
    stars.forEach((star) => {
      star.classList.remove("fill");
    });
  }

  // 회원용 게임 시작 함수
  function startGame() {
    const radius = $("#user-radius-select").val(); // 회원용 반경 값 가져오기

    if (!userLatitude || !userLongitude) {
      alert("위치 정보를 가져오지 못했습니다.");
      return;
    }

    $.post(
      "/get-random-menu",
      {
        latitude: userLatitude,
        longitude: userLongitude,
        radius: radius,
      },
      function (menu) {
        if (menu) {
          $("#user-menu-header").text(menu.name);
          window.menuId = menu.menuId;

          // 이미지 표시
          if (menu.pictureUrl) {
            $("#user-menu-image").attr("src", menu.pictureUrl).show();
          } else {
            $("#user-menu-image").hide();
          }
        } else {
          alert("반경 내에 더 이상 메뉴가 없습니다!");
          $("#user-rating-controls").hide();
        }
      }
    ).fail(function () {
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    });
  }

  // 메뉴 평가 함수
  function rateMenu(menuId, rating) {
    const radius = $("#user-radius-select").val(); // 회원용 반경 값 가져오기

    $.post(
      "/rate-menu",
      { menuId: menuId, rating: rating, radius: radius },
      function (menu) {
        if (menu) {
          $("#user-menu-header").text(menu.name);
          window.menuId = menu.menuId;

          // 이미지 표시
          if (menu.pictureUrl) {
            $("#user-menu-image").attr("src", menu.pictureUrl).show();
          } else {
            $("#user-menu-image").hide();
          }
        } else {
          alert("반경 내에 더 이상 메뉴가 없습니다!");
          $("#user-rating-controls").hide();
        }
      }
    ).fail(function () {
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    });
  }
});
