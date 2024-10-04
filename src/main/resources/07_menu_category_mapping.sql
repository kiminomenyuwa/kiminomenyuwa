# -- 카테고리 매핑 템플릿
# INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
# SELECT 0000, `category_id` FROM `food_category` WHERE `category_name` IN ('', '', '', '', '', '', '');
#                                                                      (대분류, 소분류, 재료, 조리방법, 식이제한, 음식특성, 식사시간)
-- 이마트 테스트 데이터
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1000, `category_id` FROM `food_category` WHERE `category_name` IN ('중식', '볶음밥', '모짜렐라치즈', '볶음', '저칼로리', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1001, `category_id` FROM `food_category` WHERE `category_name` IN ('중식', '짜장면', '돼지고기', '볶음', '오가닉', '짠', '점심');

-- 맥도날드
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1101, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '햄버거', '소고기', '구이', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1102, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '햄버거', '닭고기', '구이', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1103, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '햄버거', '소고기', '구이', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1104, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '햄버거', '불고기', '구이', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1105, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '햄버거', '소고기', '구이', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1106, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '햄버거', '새우', '구이', '제한없음', '짠', '점심');

-- 반포식스포스코사거리점
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1201, `category_id` FROM `food_category` WHERE `category_name` IN ('베트남식', '월남쌈', '야채', '생', '저칼로리', '담백한', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1202, `category_id` FROM `food_category` WHERE `category_name` IN ('베트남식', '쌀국수', '쌀', '삶음', '저칼로리', '풍미가득', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1203, `category_id` FROM `food_category` WHERE `category_name` IN ('태국식', '튀김', '새우', '튀김', '제한없음', '고소한', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1204, `category_id` FROM `food_category` WHERE `category_name` IN ('베트남식', '샐러드', '야채', '생', '저칼로리', '새콤달콤', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1205, `category_id` FROM `food_category` WHERE `category_name` IN ('베트남식', '볶음밥', '쌀', '볶음', '제한없음', '고소한', '점심');

-- 기기커피
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1301, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '커피', '커피', '추출', '저칼로리', '쓴', '아침');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1302, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '스무디', '우유', '블렌딩', '제한없음', '단', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1303, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '주스', '수박', '블렌딩', '제한없음', '단', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1304, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '커피', '커피', '추출', '저칼로리', '쓴', '아침');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1305, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '라떼', '딸기', '블렌딩', '제한없음', '고소한', '점심');

-- 밥스키친
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1401, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '튀김', '돼지고기', '튀김', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1402, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '튀김', '돼지고기', '튀김', '제한없음', '고소한', '점심');

-- KFC코엑스Mall
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1501, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '치킨', '닭고기', '튀김', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1502, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '치킨', '닭고기', '튀김', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1503, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '햄버거', '닭고기', '구이', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1504, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '햄버거', '닭고기', '구이', '제한없음', '짠', '점심');

-- 폴바셋
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1601, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '커피', '커피', '추출', '저칼로리', '쓴', '아침');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1602, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '라떼', '우유', '블렌딩', '제한없음', '고소한', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1603, `category_id` FROM `food_category` WHERE `category_name` IN ('제과류', '디저트', '우유', '베이킹', '제한없음', '단', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1604, `category_id` FROM `food_category` WHERE `category_name` IN ('양식', '샌드위치', '베이컨', '구이', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1605, `category_id` FROM `food_category` WHERE `category_name` IN ('양식', '샌드위치', '베이컨', '구이', '제한없음', '짠', '아침');

-- 신사골감자탕
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1701, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '국', '돼지고기', '끓임', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1702, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '탕', '돼지고기', '끓임', '제한없음', '짠', '저녁');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1703, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '국', '생선', '끓임', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1704, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '찜', '돼지고기', '끓임', '제한없음', '짠', '저녁');

-- 고씨떡볶이삼성점
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1801, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '떡볶이', '떡', '볶음', '제한없음', '매운', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1802, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '순대', '돼지내장', '삶음', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1803, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '튀김', '오징어', '튀김', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1804, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '볶음밥', '쌀', '볶음', '제한없음', '짠', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1805, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '주스', '복숭아', '제한없음', '단', '점심');

-- 153구포국수
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1901, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '국수', '소면', '삶음', '저칼로리', '담백한', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1902, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '김밥', '계란', '구이', '저칼로리', '담백한', '아침');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1903, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '국수', '소면', '삶음', '저칼로리', '담백한', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1904, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '국수', '소면', '삶음', '저칼로리', '담백한', '저녁');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1905, `category_id` FROM `food_category` WHERE `category_name` IN ('한식', '국수', '소면', '삶음', '저칼로리', '매운', '저녁');

-- 치킨잇수다
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2001, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '치킨', '닭고기', '튀김', '제한없음', '짠', '저녁');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2002, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '치킨', '닭고기', '튀김', '제한없음', '짠', '저녁');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2003, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '치킨', '닭고기', '튀김', '제한없음', '짠', '저녁');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2004, `category_id` FROM `food_category` WHERE `category_name` IN ('패스트푸드', '치킨', '닭고기', '튀김', '제한없음', '짠', '저녁');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2005, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '맥주', '제한없음', '쓴', '저녁');

-- 공차코엑스몰점
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2101, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '밀크티', '우유', '블렌딩', '제한없음', '단', '간식');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2102, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '밀크티', '초콜릿', '블렌딩', '제한없음', '단', '간식');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2103, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '밀크티', '브라운슈가', '블렌딩', '제한없음', '단', '간식');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2104, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '스무디', '우유', '블렌딩', '제한없음', '단', '간식');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2105, `category_id` FROM `food_category` WHERE `category_name` IN ('음료류', '스무디', '딸기', '블렌딩', '제한없음', '단', '간식');

-- 팀호완삼성점
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2201, `category_id` FROM `food_category` WHERE `category_name` IN ('중식', '만두', '새우', '찜', '저칼로리', '담백한', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2202, `category_id` FROM `food_category` WHERE `category_name` IN ('중식', '만두', '돼지고기', '찜', '저칼로리', '담백한', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2203, `category_id` FROM `food_category` WHERE `category_name` IN ('중식', '라이스롤', '돼지고기', '구이', '저칼로리', '담백한', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2204, `category_id` FROM `food_category` WHERE `category_name` IN ('중식', '만두', '새우', '찜', '저칼로리', '담백한', '점심');
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 2205, `category_id` FROM `food_category` WHERE `category_name` IN ('중식', '라이스롤', '새우', '구이', '저칼로리', '담백한', '점심');
