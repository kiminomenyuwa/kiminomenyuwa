-- 이마트 테스트 데이터
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1000, `category_id` FROM `food_category` WHERE `category_name` IN ('중식', '볶음밥', '모짜렐라치즈', '볶음', '저칼로리', '짠', '점심');

-- 맥도날드코엑스점 데이터
INSERT INTO `menu_category_mapping` (`menu_id`, `category_id`)
SELECT 1001, `category_id` FROM `food_category` WHERE `category_name` IN ('중식', '짜장면', '돼지고기', '볶음', '오가닉', '짠', '점심');
