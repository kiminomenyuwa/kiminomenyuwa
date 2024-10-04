// src/main/resources/static/js/header.js

document.addEventListener('DOMContentLoaded', function() {
    // 햄버거 메뉴 관련 요소
    const hamburger = document.getElementById('hamburger');
    const sideMenu = document.getElementById('hbgMenu');
    const overlay = document.getElementById('overlay');

    // 햄버거 메뉴 클릭 시 사이드 메뉴와 오버레이 토글
    hamburger.addEventListener('click', () => {
        sideMenu.classList.toggle('open');
        if (sideMenu.classList.contains('open')) {
            overlay.style.display = 'block';
            hamburger.setAttribute('aria-expanded', 'true');
        } else {
            overlay.style.display = 'none';
            hamburger.setAttribute('aria-expanded', 'false');
        }
    });

    // 오버레이 클릭 시 사이드 메뉴와 오버레이 숨김
    overlay.addEventListener('click', () => {
        sideMenu.classList.remove('open');
        overlay.style.display = 'none';
        hamburger.setAttribute('aria-expanded', 'false');
    });

    // 드롭다운 메뉴 관련 요소
    const profileMenuBtn = document.getElementById('profile-menu-btn');
    const dropdownMenu = document.getElementById('dropdown-menu');

    // 프로필 메뉴 버튼 클릭 시 드롭다운 메뉴 토글
    profileMenuBtn.addEventListener('click', (event) => {
        event.stopPropagation(); // 클릭 이벤트가 상위로 전파되는 것을 방지
        dropdownMenu.classList.toggle('show'); // 'show' 클래스 토글
        // aria-expanded 속성 업데이트
        const isExpanded = dropdownMenu.classList.contains('show');
        profileMenuBtn.setAttribute('aria-expanded', isExpanded);
    });

    // 드롭다운 메뉴 내 클릭 시 이벤트 전파 방지
    dropdownMenu.addEventListener('click', (event) => {
        event.stopPropagation();
    });

    // 외부 클릭 시 드롭다운 메뉴 닫기
    window.addEventListener('click', (event) => {
        if (!profileMenuBtn.contains(event.target) && !dropdownMenu.contains(event.target)) {
            dropdownMenu.classList.remove('show');
            profileMenuBtn.setAttribute('aria-expanded', 'false');
        }
    });

    // 키보드 접근성: Enter 또는 Space 키로 드롭다운 메뉴 토글
    profileMenuBtn.addEventListener('keydown', (event) => {
        if (event.key === 'Enter' || event.key === ' ') {
            event.preventDefault();
            dropdownMenu.classList.toggle('show');
            const isExpanded = dropdownMenu.classList.contains('show');
            profileMenuBtn.setAttribute('aria-expanded', isExpanded);
        }
    });
});
