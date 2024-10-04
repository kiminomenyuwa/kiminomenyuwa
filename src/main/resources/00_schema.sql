use kiminomenyuwa;
-- 외래 키 제약을 비활성화
SET FOREIGN_KEY_CHECKS = 0;

-- 테이블 삭제
DROP TABLE IF EXISTS purchased_menu;
DROP TABLE IF EXISTS receipt_verification;
DROP TABLE IF EXISTS profile_photo;
DROP TABLE IF EXISTS mini_game_menu_rating;
DROP TABLE IF EXISTS user_activity;
DROP TABLE IF EXISTS user_dining_history;
DROP TABLE IF EXISTS review_photo;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS menu_category_mapping;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS store;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS food_category;
DROP TABLE IF EXISTS category_type;
DROP TABLE IF EXISTS friendships;
DROP TABLE IF EXISTS store_photo;
DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS budget;
DROP TABLE IF EXISTS discount;

-- 외래 키 제약을 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;

-- 카테고리 타입 테이블: 카테고리의 종류를 정의 (예: 재료, 나라, 조리 방법)
CREATE TABLE `category_type`
(
    `type_id`   INT AUTO_INCREMENT NOT NULL, -- 카테고리 타입의 고유 식별자
    `type_name` VARCHAR(50)        NOT NULL, -- 카테고리 타입 이름 (예: '재료', '나라', '조리 방법')
    PRIMARY KEY (`type_id`)                  -- 기본 키 설정
);

-- 카테고리 테이블: 카테고리 항목을 정의 (예: 고추, 중식, 굽기)
CREATE TABLE `food_category`
(
    `category_id`   INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `category_name` VARCHAR(50) NOT NULL, -- 카테고리 이름 (예: '고추', '중식', '굽기')
    `type_id`       INT         NOT NULL, -- 카테고리 타입 ID (외래 키)
    FOREIGN KEY (`type_id`) REFERENCES `category_type` (`type_id`)
);

-- 사용자 테이블: 사용자 정보와 로그인 자격 증명을 저장 (기존 Member에서 User로 변경)
CREATE TABLE `user`
(
    `user_id`           VARCHAR(20)                                       NOT NULL,                           -- 사용자의 고유 식별자
    `password_hash`     VARCHAR(60)                                       NOT NULL,                           -- 암호화된 비밀번호 (해시값)
    `name`              VARCHAR(20),                                                                          -- 사용자 이름
    `birth_date`        DATE,                                                                                 -- 사용자 생년월일
    `gender`            ENUM ('male', 'female', 'non-binary', 'other')    NOT NULL,                           -- 성별
    `email`             VARCHAR(50),                                                                          -- 이메일 주소
    `road_name_address` VARCHAR(100),                                                                         -- 도로명 주소
    `detail_address`    VARCHAR(100),                                                                         -- 상세 주소
    `zipcode`           VARCHAR(20),                                                                          -- 우편번호
    `phone_number`      VARCHAR(20),                                                                          -- 전화번호
    `profile_img_uuid`  VARCHAR(100),                                                                         -- 프로필 사진 URL
    `role`              ENUM ('ROLE_USER', 'ROLE_MERCHANT', 'ROLE_ADMIN') NOT NULL,                           -- 사용자 역할
    `enabled`           TINYINT(1)                                        NOT NULL DEFAULT 1,                 -- 계정 활성화 여부
    `created_time`      TIMESTAMP                                                  DEFAULT CURRENT_TIMESTAMP, -- 계정 생성 시간
    PRIMARY KEY (`user_id`),                                                                                  -- 기본 키 설정
    CONSTRAINT `chk_gender` CHECK (`gender` IN ('male', 'female', 'non-binary', 'other')),                    -- 성별 값에 대한 제약 조건
    CONSTRAINT `chk_role` CHECK (`role` IN ('ROLE_USER', 'ROLE_MERCHANT', 'ROLE_ADMIN'))                      -- 역할 값에 대한 제약 조건
);


-- 상점 테이블: 사용자가 소유한 상점 정보를 저장
CREATE TABLE `store`
(
    `store_id`          INT AUTO_INCREMENT NOT NULL,           -- 상점의 고유 식별자
    `user_id`           VARCHAR(20)        NOT NULL,           -- 상점 소유자의 사용자 ID (변경된 필드 이름)
    `name`              VARCHAR(30)        NOT NULL,           -- 상점 이름
    `certification`     VARCHAR(100)       NOT NULL,           -- 인증 정보
    `road_name_address` VARCHAR(100),                          -- 도로명 주소
    `detail_address`    VARCHAR(100),                          -- 상세 주소
    `zipcode`           VARCHAR(20),                           -- 우편번호
    `phone_number`      VARCHAR(20),                           -- 전화번호
    `category`          VARCHAR(30),                           -- 상점 카테고리
    `description`       TEXT,                                  -- 상점 설명
    `enabled`           TINYINT(1)         NOT NULL DEFAULT 1, -- 상점 활성화 여부
    `location`          POINT,
    PRIMARY KEY (`store_id`),                                  -- 기본 키 설정
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

-- 메뉴 테이블: 상점에서 제공하는 메뉴 항목을 저장
CREATE TABLE `menu`
(
    `menu_id`     INT        NOT NULL,           -- 메뉴의 고유 식별자
    `store_id`    INT        NOT NULL,           -- 메뉴가 속한 상점의 ID
    `name`        VARCHAR(50),                   -- 메뉴 이름
    `price`       INT,                           -- 메뉴 가격
    `picture_url` VARCHAR(1000),                  -- 메뉴 사진 URL
    `enabled`     TINYINT(1) NOT NULL DEFAULT 1, -- 메뉴 활성화 여부
    PRIMARY KEY (`menu_id`),                     -- 기본 키 설정
    FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
);

-- 메뉴와 카테고리 간의 관계를 저장하는 테이블: 메뉴에 대한 카테고리 정보를 저장
CREATE TABLE `menu_category_mapping`
(
    `mapping_id`  INT AUTO_INCREMENT NOT NULL, -- 메뉴와 카테고리 간의 관계의 고유 식별자
    `menu_id`     INT                NOT NULL, -- 메뉴의 ID
    `category_id` INT                NOT NULL, -- 카테고리 ID
    PRIMARY KEY (`mapping_id`),                -- 기본 키 설정
    FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`),
    FOREIGN KEY (`category_id`) REFERENCES `food_category` (`category_id`)
);

-- 리뷰 테이블: 사용자 리뷰를 저장
CREATE TABLE `review`
(
    `review_id`     INT AUTO_INCREMENT NOT NULL,         -- 리뷰의 고유 식별자
    `store_id`      INT                NOT NULL,         -- 리뷰가 속한 상점의 ID
    `user_id`       VARCHAR(20)        NOT NULL,         -- 리뷰 작성자의 사용자 ID (변경된 필드 이름)
    `rating`        TINYINT            NOT NULL,         -- 리뷰 평점
    `comment`       TEXT,                                -- 리뷰 내용
    `created_time`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 리뷰 작성 시간
    `modified_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 리뷰 작성 시간
    PRIMARY KEY (`review_id`),                           -- 기본 키 설정
    FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
);

-- 리뷰 사진 테이블 생성
CREATE TABLE `review_photo`
(
    `photo_id`      INT AUTO_INCREMENT NOT NULL,         -- 사진의 고유 식별자
    `review_id`     INT                NOT NULL,         -- 사진이 속한 리뷰의 ID
    `photo_url`     VARCHAR(255)       NOT NULL,         -- 사진의 URL 또는 경로
    `uploaded_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 사진 업로드 시간
    PRIMARY KEY (`photo_id`),                            -- 기본 키 설정
    FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`) ON DELETE CASCADE
);

-- 사용자의 식사 내역 테이블: 사용자가 소비한 식사의 기록을 저장
CREATE TABLE `user_dining_history`
(
    `dining_id`   INT AUTO_INCREMENT NOT NULL,         -- 식사 기록의 고유 식별자
    `user_id`     VARCHAR(20)        NOT NULL,         -- 식사 기록의 사용자 ID (변경된 필드 이름)
    `menu_id`     INT                NOT NULL,         -- 식사한 메뉴의 ID
    `dining_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 식사 날짜
    PRIMARY KEY (`dining_id`),                         -- 기본 키 설정
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`)
);

-- 사용자의 활동 내역 테이블: 사용자가 수행한 활동 기록을 저장
CREATE TABLE `user_activity`
(
    `activity_id`   INT AUTO_INCREMENT NOT NULL,         -- 활동 기록의 고유 식별자
    `user_id`       VARCHAR(20)        NOT NULL,         -- 활동을 수행한 사용자의 ID (변경된 필드 이름)
    `menu_id`       INT                NOT NULL,         -- 관련된 메뉴의 ID
    `activity_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 활동 발생 시간
    `activity_name` VARCHAR(50),                         -- 활동 이름
    PRIMARY KEY (`activity_id`),                         -- 기본 키 설정
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`)
);

-- 미니게임에서 메뉴에 대한 별점 기록 테이블: 미니게임을 통해 메뉴에 대한 별점을 저장
CREATE TABLE `mini_game_menu_rating`
(
    `rating_id`   INT AUTO_INCREMENT NOT NULL,         -- 별점 기록의 고유 식별자
    `user_id`     VARCHAR(20)        NOT NULL,         -- 별점을 매긴 사용자의 ID (변경된 필드 이름)
    `menu_id`     INT                NOT NULL,         -- 별점을 부여한 메뉴의 ID
    `rating`      FLOAT              NOT NULL,         -- 별점 (0~5)
    `rating_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 별점 부여 일시
    PRIMARY KEY (`rating_id`),                         -- 기본 키 설정
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`)
);

-- 프로필 사진 테이블: 사용자가 업로드한 프로필 사진을 저장
CREATE TABLE `profile_photo`
(
    `photo_id`      INT AUTO_INCREMENT NOT NULL,         -- 사진의 고유 식별자
    `user_id`       VARCHAR(20)        NOT NULL,         -- 사진을 올린 사용자의 ID (외래 키 대신 문자열 필드로 저장)
    `original_name` VARCHAR(100)       NOT NULL,         -- 원본 파일명
    `saved_name`    VARCHAR(100)       NOT NULL,         -- UUID와 업로드 시간을 조합한 저장 파일명
    `upload_date`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 업로드 날짜
    PRIMARY KEY (`photo_id`)                             -- 기본 키 설정
);

-- 영수증 인증 정보를 저장하는 테이블
CREATE TABLE receipt_verification
(
    receipt_verification_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 고유 ID (자동 증가)
    user_id                 VARCHAR(20) NOT NULL,               -- 사용자 ID (UserEntity와 외래키 관계)
    store_id                INT         NOT NULL,               -- 가게 ID (StoreEntity와 외래키 관계)
    review_id               INT,                                -- 리뷰 ID (ReviewEntity와 외래키 관계)
    verification_date       DATETIME DEFAULT CURRENT_TIMESTAMP, -- 인증 날짜 (기본값: 현재 시간)

    CONSTRAINT FK_user FOREIGN KEY (user_id) REFERENCES User (user_id),
    CONSTRAINT FK_store FOREIGN KEY (store_id) REFERENCES Store (store_id),
    CONSTRAINT FK_review FOREIGN KEY (review_id) REFERENCES review (review_id)
);

-- 사용자가 구매한 메뉴 정보를 저장하는 테이블
CREATE TABLE purchased_menu
(
    purchased_menu_id       BIGINT AUTO_INCREMENT PRIMARY KEY, -- 고유 ID (자동 증가)
    receipt_verification_id BIGINT NOT NULL,                   -- 영수증 인증 ID (ReceiptVerificationEntity와 외래키 관계)
    menu_id                 INT    NOT NULL,                   -- 메뉴 ID (MenuEntity와 외래키 관계)
    quantity                INT,                               -- 구매 수량

    CONSTRAINT FK_receipt_verification FOREIGN KEY (receipt_verification_id) REFERENCES receipt_verification (receipt_verification_id),
    CONSTRAINT FK_menu FOREIGN KEY (menu_id) REFERENCES `menu` (menu_id)
);

-- 친구관계도 테이블
CREATE TABLE friendships
(
    friendship_id INT PRIMARY KEY AUTO_INCREMENT,                                        -- 고유한 친구 관계 ID
    user_id       VARCHAR(20) NOT NULL,                                                  -- 친구 요청을 보낸 사용자 ID
    friend_id     VARCHAR(20) NOT NULL,                                                  -- 친구 요청을 받은 사용자 ID
    status        ENUM ('PENDING', 'ACCEPTED', 'DECLINED', 'BLOCKED') DEFAULT 'PENDING', -- 친구 관계 상태
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE,                   -- user_id 외래 키
    FOREIGN KEY (friend_id) REFERENCES user (user_id) ON DELETE CASCADE                  -- friend_id 외래 키
);

-- user_id와 friend_id 쌍의 중복을 방지하는 고유 인덱스 추가
CREATE UNIQUE INDEX friendship_unique_index ON friendships (user_id, friend_id);

CREATE TABLE store_photo
(
    photo_id  INT AUTO_INCREMENT PRIMARY KEY,
    store_id  INT           NOT NULL,
    photo_url VARCHAR(1000) NOT NULL,
    is_main   BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (store_id) REFERENCES store (store_id) ON DELETE CASCADE
);

-- 사용자가 입력한 예산 테이블
CREATE TABLE budget
(
    budget_id INT AUTO_INCREMENT PRIMARY KEY,       -- 예산의 고유 ID
    user_id   VARCHAR(20),                          -- 사용자 ID
    month     INT,                                  -- 예산의 해당 월 (1 ~ 12)
    year      INT,                                  -- 예산의 해당 연도 (예: 2024)
    budget    INT,                                  -- 해당 월의 예산 금액
    FOREIGN KEY (user_id) REFERENCES user (user_id) -- 사용자 테이블과의 외래키 관계
);

CREATE TABLE `favorite`
(
    `favorite_id` INT AUTO_INCREMENT NOT NULL,         -- 찜 항목의 고유 식별자
    `user_id`     VARCHAR(20)        NOT NULL,         -- 찜한 사용자의 ID
    `store_id`    INT                NOT NULL,         -- 찜한 상점의 ID
    `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 찜한 날짜와 시간
    PRIMARY KEY (`favorite_id`),                       -- 기본 키 설정
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`) ON DELETE CASCADE,
    UNIQUE KEY unique_favorite (user_id, store_id)
);

-- Discount 테이블 생성
CREATE TABLE Discount
(
    discount_id      INT AUTO_INCREMENT PRIMARY KEY,
    menu_id          INT            NOT NULL,
    original_price   DECIMAL(10, 2) NOT NULL,
    discounted_price DECIMAL(10, 2) NOT NULL,
    discount_rate    INT            NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES Menu (menu_id)
);
ALTER TABLE discount
    MODIFY COLUMN original_price DECIMAL(10, 2) NOT NULL;
ALTER TABLE discount
    MODIFY COLUMN discounted_price DECIMAL(10, 2) NOT NULL;

