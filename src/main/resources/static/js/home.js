// src/main/resources/static/js/home.js

$(document).ready(function() {
  // 현재 위치 불러오기
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showPosition, showError);
  } else {
    $('#current-address').text("Geolocation is not supported by this browser.");
    $('#user-current-address').text("Geolocation is not supported by this browser.");
  }

  function showPosition(position) {
    const lat = position.coords.latitude;
    const lng = position.coords.longitude;

    // 네이버 지도 Geocoder 사용하여 주소 변환
    const geocoder = new naver.maps.Geocoder();

    geocoder.coord2Address(lng, lat, function(status, response) {
      if (status === naver.maps.Service.Status.OK) {
        const address = response.v2.address.address;
        $('#current-address').text(address);
        $('#user-current-address').text(address);
      } else {
        $('#current-address').text("주소를 불러올 수 없습니다.");
        $('#user-current-address').text("주소를 불러올 수 없습니다.");
      }
    });
  }

  function showError(error) {
    switch(error.code) {
      case error.PERMISSION_DENIED:
        $('#current-address').text("사용자가 위치 정보 제공을 거부했습니다.");
        $('#user-current-address').text("사용자가 위치 정보 제공을 거부했습니다.");
        break;
      case error.POSITION_UNAVAILABLE:
        $('#current-address').text("위치 정보를 사용할 수 없습니다.");
        $('#user-current-address').text("위치 정보를 사용할 수 없습니다.");
        break;
      case error.TIMEOUT:
        $('#current-address').text("위치 정보를 가져오는 데 시간이 초과되었습니다.");
        $('#user-current-address').text("위치 정보를 가져오는 데 시간이 초과되었습니다.");
        break;
      case error.UNKNOWN_ERROR:
        $('#current-address').text("알 수 없는 오류가 발생했습니다.");
        $('#user-current-address').text("알 수 없는 오류가 발생했습니다.");
        break;
    }
  }

  // 게임 시작 버튼 클릭 이벤트
  $('#start-game, #user-start-game').click(function() {
    // 임의의 메뉴 추천 로직 (예시)
    const menus = [
      { name: "김치찌개", image: "/images/kimchi_jjigae.jpg" },
      { name: "비빔밥", image: "/images/bibimbap.jpg" },
      { name: "불고기", image: "/images/bulgogi.jpg" },
      { name: "삼겹살", image: "/images/samgyeopsal.jpg" },
      { name: "떡볶이", image: "/images/tteokbokki.jpg" }
    ];

    const randomMenu = menus[Math.floor(Math.random() * menus.length)];

    if ($(this).attr('id') === 'start-game') {
      $('#menu-header').text(randomMenu.name);
      $('#menu-image').attr('src', randomMenu.image).show();
    } else if ($(this).attr('id') === 'user-start-game') {
      $('#user-menu-header').text(randomMenu.name);
      $('#user-menu-image').attr('src', randomMenu.image).show();
      $('#user-rating-controls').show();
    }
  });

  // 별점 선택 이벤트
  $('.star').click(function() {
    const rating = $(this).data('value');
    $('.star').each(function() {
      if ($(this).data('value') <= rating) {
        $(this).addClass('selected');
      } else {
        $(this).removeClass('selected');
      }
    });
  });

  // 싫어요 버튼 클릭 이벤트
  $('#user-dislike-button').click(function() {
    alert("싫어요를 선택하셨습니다.");
  });

  // 좋아요 3점 고정 체크박스 이벤트
  $('#fixed-rating-checkbox').change(function() {
    if ($(this).is(':checked')) {
      $('.star').removeClass('selected');
      $('.star').each(function() {
        if ($(this).data('value') <= 3) {
          $(this).addClass('selected');
        }
      });
      $('.star').css('pointer-events', 'none'); // 더 이상 클릭 불가능하게 설정
    } else {
      $('.star').css('pointer-events', 'auto');
    }
  });
});
