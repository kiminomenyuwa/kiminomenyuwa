# 🎯 키미노메뉴와 (Kimi no Menu wa) 🍽️

![Project Banner](images/logo.png) <!-- 프로젝트 배너 이미지를 여기에 추가 -->

## 📖 프로젝트 개요

"키미노메뉴와"는 사용자 취향을 분석해 맞춤형 메뉴를 추천하는 서비스입니다. 사용자의 데이터에 기반하여 새로운 음식을 추천하고, 예산에 맞는 메뉴를 제안하며, 그룹 추천 기능도 제공하는 등 사용자의 식사 선택 고민을 해결합니다.

---

## 🚀 주요 기능

- **🔍 맞춤형 메뉴 추천**: 사용자 취향과 데이터를 분석하여 정밀한 메뉴 추천
- **👥 그룹 추천**: 그룹 내 친구들의 선호도에 맞춘 음식 추천
- **📅 식사 내역 관리**: 사용자별 식사 내역을 기록하고 관리할 수 있는 캘린더 기능
- **✍ 리뷰 작성 및 별점 부여**: 영수증 인증을 통해 자동으로 유저 데이터 업데이트 및 추천의 정확도 향상
- **🛠 사장님 가게 관리**: 가게 정보 등록 및 할인 메뉴 추가 기능 제공

---

## 👥 팀원 소개

| <center><img src="images/2.jpg" width="100px" height="100px"></center> | <center><img src="images/5.jpg" width="100px" height="100px"></center> | <center><img src="images/1.jpg" width="100px" height="100px"></center> | <center><img src="images/3.jpeg" width="100px" height="100px"></center> | <center><img src="images/4.jpg" width="100px" height="100px"></center> |
|------------------------------------------------------------------------|------------------------------------------------------------------------|------------------------------------------------------------------------|-------------------------------------------------------------------------|------------------------------------------------------------------------|
| <center>김신일</center>                                                   | <center>김희윤</center>                                                   | <center>박현지</center>                                                   | <center>이설인</center>                                                    | <center>최재원</center>                                                   |

---

## 🗂 메뉴 구조도

![메뉴 구조도](images/menu-structure.jpg) <!-- 메뉴 구조도 이미지 추가 -->

---

## 🎥 기능별 시연 (GIF)

### **맞춤형 메뉴 추천**
![맞춤형 메뉴 추천 시연](gifs/menu-recommendation.gif) <!-- 맞춤형 메뉴 추천 기능 시연 GIF 추가 -->

### **그룹 추천**
![그룹 추천 시연](gifs/group-recommendation.gif) <!-- 그룹 추천 기능 시연 GIF 추가 -->

### **미니게임**
![미니게임 시연](gifs/minigame.gif) <!-- 예산 기반 메뉴 추천 기능 시연 GIF 추가 -->

### **식사 내역 관리**
![식사 내역 관리 시연](gifs/meal-history.gif) <!-- 식사 내역 관리 기능 시연 GIF 추가 -->

### **리뷰 작성 및 별점 부여**
![리뷰 작성 및 별점 부여 시연](gifs/review.gif) <!-- 리뷰 작성 및 별점 부여 시연 GIF 추가 -->

---


## 🛠 기술 스택

### 🌐 **Front-End**
![JavaScript](https://img.shields.io/badge/JavaScript-FFD700?style=for-the-badge&logo=javascript&logoColor=black)
![jQuery](https://img.shields.io/badge/jQuery-0868AC?style=for-the-badge&logo=jquery&logoColor=white)
![HTML](https://img.shields.io/badge/HTML-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-39B54A?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-8E44AD?style=for-the-badge&logo=bootstrap&logoColor=white)

### 🖥 **Back-End**
![Java](https://img.shields.io/badge/Java-17-F3913E?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-1ABC9C?style=for-the-badge&logo=spring-security&logoColor=white)
![Spring JPA](https://img.shields.io/badge/Spring%20JPA-27AE60?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-1E8EAB?style=for-the-badge&logo=gradle&logoColor=white)
![Azure Document Intelligence](https://img.shields.io/badge/Azure%20Document%20Intelligence-0078D4?style=for-the-badge&logo=microsoft-azure&logoColor=white)
![Clova Map API](https://img.shields.io/badge/Clova%20Map%20API-00C73C?style=for-the-badge&logo=naver&logoColor=white)

---

## 📊 데이터베이스 구조 (ERD)

![ERD Diagram](images/erd.png) <!-- ERD 다이어그램 이미지 추가 -->

- **User (사용자)**: 사용자 정보를 저장하는 테이블
- **Menu (메뉴)**: 음식 메뉴 데이터를 저장하는 테이블
- **Preference (선호도)**: 사용자의 음식 선호 데이터를 저장하는 테이블
- **Friendships (친구)**: 사용자가 관리하는 친구 목록 저장
- **Review (리뷰)**: 사용자가 작성한 음식 리뷰 및 별점 저장
- **Store (가게)**: 가게 정보를 저장하는 테이블

---

