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
