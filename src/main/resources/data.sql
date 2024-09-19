-- IMPORTANT! DATA 생성 SQL문 실행 전에 프로젝트 실행해서 id를 aaa로 회원 가입 후 실행할 것!
-- 현재 모든 테스트 데이터가 userID : 'aaa'를 기준으로 되어있고. 회원가입의 경우 비밀번호 암호화때문에 직접 해줘야하는 듯
use kiminomenyuwa;

-- 카테고리 타입 테이블에 데이터 추가
INSERT INTO category_type (type_name)
VALUES ('재료'),
       ('나라'),
       ('조리 방법'),
       ('음식 종류'),
       ('식사 시간'),
       ('식이 제한'),
       ('음식 특성');

-- 재료 카테고리 데이터 추가
INSERT INTO food_category (category_name, type_id)
VALUES ('고추', 1),
       ('돼지고기', 1),
       ('양파', 1),
       ('마늘', 1),
       ('버섯', 1),
       ('당근', 1),
       ('대파', 1),
       ('양배추', 1),
       ('토마토', 1),
       ('생선', 1),
       ('감자', 1);

-- 나라 카테고리 데이터 추가
INSERT INTO food_category (category_name, type_id)
VALUES ('중식', 2),
       ('일식', 2),
       ('한식', 2),
       ('양식', 2),
       ('태국식', 2),
       ('인도식', 2),
       ('베트남식', 2),
       ('멕시코식', 2),
       ('지중해식', 2),
       ('프랑스식', 2);

-- 조리 방법 카테고리 데이터 추가
INSERT INTO food_category (category_name, type_id)
VALUES ('구이', 3),
       ('찜', 3),
       ('볶음', 3),
       ('튀김', 3),
       ('삶음', 3),
       ('조림', 3),
       ('훈제', 3),
       ('찌개', 3),
       ('베이킹', 3);

-- 음식 종류 카테고리 데이터 추가
INSERT INTO food_category (category_name, type_id)
VALUES ('스프', 4),
       ('샌드위치', 4),
       ('파스타', 4),
       ('피자', 4),
       ('햄버거', 4),
       ('스튜', 4),
       ('볶음밥', 4),
       ('스무디', 4),
       ('디저트', 4),
       ('롤', 4);

-- 식사 시간 카테고리 데이터 추가
INSERT INTO food_category (category_name, type_id)
VALUES ('아침', 5),
       ('점심', 5),
       ('저녁', 5),
       ('간식', 5),
       ('브런치', 5),
       ('야식', 5),
       ('콜드브루', 5),
       ('오후 간식', 5);

-- 식이 제한 카테고리 데이터 추가
INSERT INTO food_category (category_name, type_id)
VALUES ('채식', 6),
       ('비건', 6),
       ('글루텐 프리', 6),
       ('무유당', 6),
       ('저염', 6),
       ('저칼로리', 6),
       ('무설탕', 6),
       ('오가닉', 6);

-- 음식 특성 카테고리 데이터 추가
INSERT INTO food_category (category_name, type_id)
VALUES ('매운', 7),
       ('짠', 7),
       ('단', 7),
       ('신', 7),
       ('쓴', 7),
       ('고소한', 7),
       ('새콤달콤', 7),
       ('풍미 가득', 7);

-- 유저 추가
INSERT INTO `user` (`user_id`, `password_hash`, `name`, `birth_date`, `gender`, `email`, `road_name_address`, `detail_address`, `zipcode`, `phone_number`, `profile_img_uuid`, `role`, `enabled`)
VALUES ('testuser01', '$2y$10$abcdefghijklmnopqrstuvwx', '김철수', '1985-10-15', 'male', 'testuser01@example.com', '서울시 강남구', '테헤란로 123', '06234', '010-1234-5678', 'http://example.com/photo.jpg', 'ROLE_USER', 1);

-- 상점 추가
INSERT INTO `store` (`user_id`, `name`, `certification`, `road_name_address`, `detail_address`, `zipcode`,
                     `phone_number`, `category`, `description`, `enabled`)
VALUES ('aaa', '철수네 식당', '인증 정보', '서울시 강남구', '테헤란로 123', '06234', '010-8765-4321', '한식', '서울에서 가장 맛있는 한식당입니다.', 1);

-- 메뉴 추가 (20개로 늘림)
INSERT INTO `menu` (`store_id`, `name`, `price`, `picture_url`, `enabled`)
VALUES (1, '비빔밥', 8000, 'http://example.com/bibimbap.jpg', 1),
       (1, '된장찌개', 7000, 'http://example.com/doenjangjjigae.jpg', 1),
       (1, '불고기', 12000, 'http://example.com/bulgogi.jpg', 1),
       (1, '김치찌개', 7000, 'http://example.com/kimchijjigae.jpg', 1),
       (1, '잡채', 9000, 'http://example.com/japchae.jpg', 1),
       (1, '제육볶음', 9000, 'http://example.com/jeyukbokkeum.jpg', 1),
       (1, '콩나물국밥', 6000, 'http://example.com/kongnamulgukbap.jpg', 1),
       (1, '갈비탕', 12000, 'http://example.com/galbitang.jpg', 1),
       (1, '떡볶이', 5000, 'http://example.com/tteokbokki.jpg', 1),
       (1, '비빔국수', 7000, 'http://example.com/bibimguksu.jpg', 1),
       (1, '삼겹살', 15000, 'http://example.com/samgyeopsal.jpg', 1),
       (1, '냉면', 8000, 'http://example.com/naengmyeon.jpg', 1),
       (1, '우동', 7000, 'http://example.com/udon.jpg', 1),
       (1, '라면', 4000, 'http://example.com/ramen.jpg', 1),
       (1, '초밥', 10000, 'http://example.com/sushi.jpg', 1),
       (1, '튀김', 5000, 'http://example.com/fried.jpg', 1),
       (1, '볶음우동', 9000, 'http://example.com/yakisoba.jpg', 1),
       (1, '파스타', 12000, 'http://example.com/pasta.jpg', 1),
       (1, '햄버거', 8000, 'http://example.com/burger.jpg', 1),
       (1, '피자', 15000, 'http://example.com/pizza.jpg', 1);

-- menu_category_mapping
INSERT INTO menu_category_mapping (`menu_id`, `category_name`)
VALUES
-- 비빔밥 (menu_id 1)
(1, '고추'),
(1, '한식'),
(1, '볶음'),
(1, '볶음밥'),
(1, '점심'),
(1, '채식'),
(1, '매운'),

-- 된장찌개 (menu_id 2)
(2, '돼지고기'),
(2, '한식'),
(2, '찌개'),
(2, '찌개'),
(2, '저녁'),
(2, '저염'),
(2, '고소한'),

-- 불고기 (menu_id 3)
(3, '양파'),
(3, '한식'),
(3, '구이'),
(3, '구이'),
(3, '저녁'),
(3, '무유당'),
(3, '짠'),

-- 김치찌개 (menu_id 4)
(4, '마늘'),
(4, '한식'),
(4, '찌개'),
(4, '찌개'),
(4, '저녁'),
(4, '저염'),
(4, '매운'),

-- 잡채 (menu_id 5)
(5, '버섯'),
(5, '한식'),
(5, '볶음'),
(5, '볶음'),
(5, '점심'),
(5, '채식'),
(5, '고소한'),

-- 제육볶음 (menu_id 6)
(6, '돼지고기'),
(6, '한식'),
(6, '볶음'),
(6, '볶음'),
(6, '점심'),
(6, '저염'),
(6, '매운'),

-- 콩나물국밥 (menu_id 7)
(7, '대파'),
(7, '한식'),
(7, '삶음'),
(7, '스프'),
(7, '아침'),
(7, '저염'),
(7, '고소한'),

-- 갈비탕 (menu_id 8)
(8, '감자'),
(8, '한식'),
(8, '삶음'),
(8, '스프'),
(8, '점심'),
(8, '저염'),
(8, '고소한'),

-- 떡볶이 (menu_id 9)
(9, '대파'),
(9, '한식'),
(9, '볶음'),
(9, '볶음'),
(9, '점심'),
(9, '저염'),
(9, '매운'),

-- 비빔국수 (menu_id 10)
(10, '토마토'),
(10, '한식'),
(10, '볶음'),
(10, '볶음'),
(10, '점심'),
(10, '저염'),
(10, '새콤달콤'),

-- 삼겹살 (menu_id 11)
(11, '돼지고기'),
(11, '한식'),
(11, '구이'),
(11, '구이'),
(11, '저녁'),
(11, '저염'),
(11, '고소한'),

-- 냉면 (menu_id 12)
(12, '감자'),
(12, '한식'),
(12, '삶음'),
(12, '스프'),
(12, '점심'),
(12, '저염'),
(12, '새콤달콤'),

-- 우동 (menu_id 13)
(13, '양파'),
(13, '일식'),
(13, '삶음'),
(13, '스프'),
(13, '저녁'),
(13, '채식'),
(13, '고소한'),

-- 라면 (menu_id 14)
(14, '대파'),
(14, '일식'),
(14, '삶음'),
(14, '스프'),
(14, '점심'),
(14, '채식'),
(14, '짠'),

-- 초밥 (menu_id 15)
(15, '생선'),
(15, '일식'),
(15, '구이'),
(15, '롤'),
(15, '점심'),
(15, '저염'),
(15, '고소한'),

-- 튀김 (menu_id 16)
(16, '버섯'),
(16, '일식'),
(16, '튀김'),
(16, '튀김'),
(16, '간식'),
(16, '채식'),
(16, '고소한'),

-- 볶음우동 (menu_id 17)
(17, '양파'),
(17, '일식'),
(17, '볶음'),
(17, '볶음'),
(17, '점심'),
(17, '채식'),
(17, '짠'),

-- 파스타 (menu_id 18)
(18, '토마토'),
(18, '양식'),
(18, '볶음'),
(18, '볶음'),
(18, '점심'),
(18, '비건'),
(18, '새콤달콤'),

-- 햄버거 (menu_id 19)
(19, '양파'),
(19, '양식'),
(19, '구이'),
(19, '샌드위치'),
(19, '점심'),
(19, '무설탕'),
(19, '짠'),

-- 피자 (menu_id 20)
(20, '토마토'),
(20, '양식'),
(20, '베이킹'),
(20, '피자'),
(20, '저녁'),
(20, '저염'),
(20, '고소한');



-- 테스트용 사용자 음식 먹은 내역
INSERT INTO user_dining_history (`user_id`, `menu_id`, `dining_date`)
VALUES
    ('aaa', 1, '2024-09-01 12:30:45'),
    ('aaa', 2, '2024-09-02 08:15:30'),
    ('aaa', 3, '2024-09-03 14:22:10'),
    ('aaa', 4, '2024-09-04 18:45:00'),
    ('aaa', 5, '2024-09-05 09:10:25'),
    ('aaa', 1, '2024-09-06 19:05:50'), -- menu_id 1 다시 사용
    ('aaa', 6, '2024-09-07 11:55:35'),
    ('aaa', 2, '2024-09-08 07:30:20'), -- menu_id 2 다시 사용
    ('aaa', 7, '2024-09-09 15:45:40'),
    ('aaa', 8, '2024-09-10 13:20:15'),
    ('aaa', 3, '2024-09-11 16:40:05'), -- menu_id 3 다시 사용
    ('aaa', 9, '2024-09-12 10:00:50'),
    ('aaa', 10, '2024-09-13 17:35:00');

select * from user_dining_history;

-- 메뉴 전체 정보 확인
SELECT m.menu_id,                                                   -- 메뉴 ID
       m.name                                        AS menu_name,  -- 메뉴 이름
       m.price,                                                     -- 메뉴 가격
       m.picture_url,                                               -- 메뉴 사진 URL
       s.name                                        AS store_name, -- 상점 이름
       u.name                                        AS owner_name, -- 상점 주인 이름
       GROUP_CONCAT(fc.category_name SEPARATOR ', ') AS categories  -- 메뉴의 카테고리들
FROM menu m
         JOIN
     store s ON m.store_id = s.store_id
         JOIN
     user u ON s.user_id = u.user_id
         LEFT JOIN
     menu_category_mapping mcm ON m.menu_id = mcm.menu_id
         LEFT JOIN
     food_category fc ON mcm.category_name = fc.category_name
GROUP BY m.menu_id, m.name, m.price, m.picture_url, s.name, u.name;

SELECT *
FROM USER;


-- 아래 sql은 그때 그때 미니게임을 다시 해보고 싶을 때 기록해둔 점수 초기화 하기 위한 sql
DROP TABLE IF EXISTS `mini_game_menu_rating`;
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
select *
from mini_game_menu_rating mgmr;
-- 여기까지

-- 사용자가 먹은 내역의 메뉴들의 카테고리 중 TOP10 카운트
SELECT mcm.category_name, COUNT(*) AS category_count
FROM user_dining_history udh
         JOIN menu_category_mapping mcm ON udh.menu_id = mcm.menu_id
WHERE udh.user_id = 'aaa' -- 여기에서 'aaa'는 사용자의 user_id
GROUP BY mcm.category_name
ORDER BY category_count DESC
LIMIT 10;