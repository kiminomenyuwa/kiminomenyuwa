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
       ('찌개', 3), -- 찌개 추가됨
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

-- test 데이터 저장
-- 유저 추가
INSERT INTO `user` (`user_id`, `password_hash`, `name`, `birth_date`, `gender`, `email`, `road_name_address`, `detail_address`, `zipcode`, `phone_number`, `profile_img_uuid`, `role`, `enabled`)
VALUES ('testuser01', '$2y$10$abcdefghijklmnopqrstuvwx', '김철수', '1985-10-15', 'male', 'testuser01@example.com', '서울시 강남구', '테헤란로 123', '06234', '010-1234-5678', 'http://example.com/photo.jpg', 'ROLE_USER', 1);

-- 상점 추가
INSERT INTO `store` (`user_id`, `name`, `certification`, `road_name_address`, `detail_address`, `zipcode`, `phone_number`, `category`, `description`, `enabled`)
VALUES ('testuser01', '철수네 식당', '인증 정보', '서울시 강남구', '테헤란로 123', '06234', '010-8765-4321', '한식', '서울에서 가장 맛있는 한식당입니다.', 1);

-- 메뉴 추가
INSERT INTO `menu` (`store_id`, `name`, `price`, `picture_url`, `enabled`)
VALUES 
(1, '비빔밥', 8000, 'http://example.com/bibimbap.jpg', 1),
(1, '된장찌개', 7000, 'http://example.com/doenjangjjigae.jpg', 1),
(1, '불고기', 12000, 'http://example.com/bulgogi.jpg', 1),
(1, '김치찌개', 7000, 'http://example.com/kimchijjigae.jpg', 1),
(1, '잡채', 9000, 'http://example.com/japchae.jpg', 1);

-- menu 테이블의 메뉴에 카테고리를 부여
insert into menu_category_mapping (`menu_id`, `category_name`)
values
(1, '한식'), -- 비빔밥
(1, '볶음밥'), -- 비빔밥
(2, '한식'), -- 된장찌개
(2, '찌개'), -- 된장찌개
(3, '한식'), -- 불고기
(3, '구이'), -- 불고기
(4, '한식'), -- 김치찌개
(4, '찌개'), -- 김치찌개
(5, '한식'), -- 잡채
(5, '볶음'); -- 잡채

--
INSERT INTO user_dining_history (`user_id`, `menu_id`, `dining_date`)
VALUES
    ('aaa', 1, '2024-09-10');

-- 메뉴 전체 정보 확인
SELECT 
    m.menu_id,                -- 메뉴 ID
    m.name AS menu_name,      -- 메뉴 이름
    m.price,                  -- 메뉴 가격
    m.picture_url,            -- 메뉴 사진 URL
    s.name AS store_name,     -- 상점 이름
    u.name AS owner_name,     -- 상점 주인 이름
    GROUP_CONCAT(fc.category_name SEPARATOR ', ') AS categories -- 메뉴의 카테고리들
FROM 
    menu m
JOIN 
    store s ON m.store_id = s.store_id
JOIN 
    user u ON s.user_id = u.user_id
LEFT JOIN 
    menu_category_mapping mcm ON m.menu_id = mcm.menu_id
LEFT JOIN 
    food_category fc ON mcm.category_name = fc.category_name
GROUP BY 
    m.menu_id, m.name, m.price, m.picture_url, s.name, u.name;
    