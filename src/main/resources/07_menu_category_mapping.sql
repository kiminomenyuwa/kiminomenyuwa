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
