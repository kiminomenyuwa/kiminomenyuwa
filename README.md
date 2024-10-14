# 🎯 キミノメニューは (Kimi no Menu wa) 🍽️

![Project Banner](images/logo.png) <!-- プロジェクトバナーの画像をここに追加 -->

## 📖 プロジェクト概要

「キミノメニューは」は、ユーザーの嗜好を分析し、パーソナライズされたメニューを推薦するサービスです。ユーザーデータに基づいて新しい料理を推薦し、予算に合ったメニューを提案、グループ推薦機能も提供することで、ユーザーの食事選択における悩みを解決します。

---

## 🚀 主な機能

- **🔍 パーソナライズされたメニュー推薦**: ユーザーの嗜好とデータを分析し、精密なメニューを推薦
- **👥 グループ推薦**: グループ内の友人の嗜好に合った料理を推薦
- **📅 食事履歴管理**: ユーザーごとに食事履歴を記録・管理できるカレンダー機能
- **✍ レビュー作成および星評価**: レシート認証を通じて自動的にユーザーデータが更新され、推薦の精度が向上
- **🛠 店主の店舗管理**: 店舗情報の登録や割引メニュー追加機能を提供

---

## 👥 チーム紹介

| <center><img src="images/2.jpg" width="100px" height="100px"></center> | <center><img src="images/5.jpg" width="100px" height="100px"></center> | <center><img src="images/1.jpg" width="100px" height="100px"></center> | <center><img src="images/3.jpeg" width="100px" height="100px"></center> | <center><img src="images/4.jpg" width="100px" height="100px"></center> |
|------------------------------------------------------------------------|------------------------------------------------------------------------|------------------------------------------------------------------------|-------------------------------------------------------------------------|------------------------------------------------------------------------|
| <center>キム・シニル</center>                                              | <center>キム・ヒユン</center>                                              | <center>パク・ヒョンジ</center>                                              | <center>イ・ソリン</center>                                               | <center>チェ・ジェウォン</center>                                             |

---

## 🗂 メニュー構造図

![メニュー構造図](images/menu-structure.jpg) <!-- メニュー構造図の画像をここに追加 -->

---

## 🎥 機能ごとのデモ (GIF)

### **パーソナライズされたメニュー推薦**
![パーソナライズされたメニュー推薦デモ](gifs/menu-recommendation.gif) <!-- パーソナライズされたメニュー推薦機能のデモGIFを追加 -->

### **グループ推薦**
![グループ推薦デモ](gifs/group-recommendation.gif) <!-- グループ推薦機能のデモGIFを追加 -->

### **ミニゲーム**
![ミニゲームデモ](gifs/minigame.gif) <!-- ミニゲーム機能のデモGIFを追加 -->

### **食事履歴管理**
![食事履歴管理デモ](gifs/meal-history.gif) <!-- 食事履歴管理機能のデモGIFを追加 -->

### **レビュー作成および星評価**
![レビュー作成および星評価デモ](gifs/review.gif) <!-- レビュー作成および星評価デモGIFを追加 -->

---

## 🛠 技術スタック

### 🌐 **フロントエンド**
![JavaScript](https://img.shields.io/badge/JavaScript-FFD700?style=for-the-badge&logo=javascript&logoColor=black)
![jQuery](https://img.shields.io/badge/jQuery-0868AC?style=for-the-badge&logo=jquery&logoColor=white)
![HTML](https://img.shields.io/badge/HTML-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-39B54A?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-8E44AD?style=for-the-badge&logo=bootstrap&logoColor=white)

### 🖥 **バックエンド**
![Java](https://img.shields.io/badge/Java-17-F3913E?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-1ABC9C?style=for-the-badge&logo=spring-security&logoColor=white)
![Spring JPA](https://img.shields.io/badge/Spring%20JPA-27AE60?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-1E8EAB?style=for-the-badge&logo=gradle&logoColor=white)

### ☁️ **インフラ / 環境**
![Azure Document Intelligence](https://img.shields.io/badge/Azure%20Document%20Intelligence-0078D4?style=for-the-badge&logo=microsoft-azure&logoColor=white)
![Clova Map API](https://img.shields.io/badge/Clova%20Map%20API-00C73C?style=for-the-badge&logo=naver&logoColor=white)

---

## 📊 データベース構造 (ERD)

![ERD図](images/erd.png) <!-- ERD図の画像をここに追加 -->

- **User (ユーザー)**: ユーザー情報を保存するテーブル
- **Menu (メニュー)**: 食品メニューデータを保存するテーブル
- **Preference (嗜好)**: ユーザーの食品嗜好データを保存するテーブル
- **Friendships (友達)**: ユーザーが管理する友達リストを保存
- **Review (レビュー)**: ユーザーが作成した食品レビューと星評価を保存
- **Store (店舗)**: 店舗情報を保存するテーブル

---

This translation should work well for your README in Japanese while keeping all the links and image paths the same!