START TRANSACTION;

-- user01 리뷰
INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (20025, 'user01', 5, '스프링 롤이 너무 맛있었어요. 신선한 재료와 깔끔한 맛이 좋았어요!'),
       (22643, 'user01', 4, '튀김류는 바삭하고 맛있었지만 조금 기름진 느낌이 있었어요. 그래도 다시 가고 싶어요!'),
       (7947, 'user01', 5, '불고기 버거 세트, 정말 환상적이었어요! 한국적인 맛과 미국식 패스트푸드의 조합이 훌륭했어요.'),
       (22668, 'user01', 4, '징거버거 세트는 기대한 만큼 맛있었어요. 빠른 서비스도 좋았고요.'),
       (22691, 'user01', 5, '폴바셋의 라떼, 정말 부드럽고 진한 맛이 인상적이었어요. 추천합니다.'),
       (13200, 'user01', 5, '치킨은 항상 옳아요. 순살 후라이드, 바삭하고 맛있었습니다.'),
       (11374, 'user01', 4, '매콤국수는 적당히 매운맛이 있어서 좋았어요. 양도 푸짐하네요.'),
       (13208, 'user01', 4, '차슈 라이스 롤은 부드럽고 담백한 맛이 일품이었어요.'),
       (20062, 'user01', 5, '딸기 초코 라떼는 처음 시도해봤는데, 달콤하고 만족스러웠어요.'),
       (17693, 'user01', 5, '안심 스테이크는 육즙이 풍부하고 부드러웠어요. 매우 만족스러웠습니다.'),
       (17704, 'user01', 5, '비스크 치즈 새우버거, 새우의 탱글함과 치즈의 조화가 너무 좋았어요.'),
       (17723, 'user01', 4, '고기차뽕은 기대 이상이었어요. 고기와 국수의 궁합이 좋았습니다.'),
       (15491, 'user01', 5, '특선 야키토리 세트는 여러 가지 맛을 즐길 수 있어 좋았어요.');

-- user02 리뷰
INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (22691, 'user02', 4, '까눌레와 함께 한 커피 한 잔, 정말 좋았어요. 디저트도 훌륭했습니다.'),
       (20062, 'user02', 4, '딸기 초코 라떼는 기대 이상이었어요. 달콤하고 부드러웠어요.'),
       (20025, 'user02', 4, '분짜 샐러드는 건강한 맛이었어요. 신선한 채소들이 기분 좋게 다가왔습니다.'),
       (22668, 'user02', 4, '핫크리스피치킨은 바삭한 식감과 짭짤한 맛이 인상적이었어요.'),
       (17704, 'user02', 5, 'NBB 시그니처 버거, 가성비 최고! 맛도 훌륭했어요.'),
       (9734, 'user02', 3, '황태해장국은 괜찮았지만 조금 더 깊은 맛이 있었으면 좋겠네요.'),
       (17723, 'user02', 4, '차뽕은 고소하고 담백한 맛이 일품이었어요.'),
       (11370, 'user02', 4, '야채 떡볶이는 매콤하고 양이 많아서 만족스러웠어요.'),
       (22643, 'user02', 5, '뷔페에서 다양한 메뉴를 즐길 수 있어서 좋았어요. 음식도 깔끔하고 맛있었습니다.'),
       (7947, 'user02', 4, '베이컨 토마토 디럭스 세트, 베이컨과 토마토의 조화가 훌륭했어요.'),
       (13208, 'user02', 4, '부채교는 새우와 부추의 맛이 잘 어우러졌어요.'),
       (17693, 'user02', 5, '토마호크 스테이크는 부드럽고 맛있었어요. 분위기도 좋았습니다.'),
       (13204, 'user02', 5, '초콜렛 밀크티는 달달하고 부드러워서 좋았어요.');

INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (15481, 'user02', 4, 'Great place to visit. The ambiance was nice, and the menu had a good variety.');

INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (15481, 'user03', 3, 'It was okay. Some dishes were tasty, but others could be improved.');

INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (15481, 'user04', 2, 'Not very satisfied. The service was slow and the food was mediocre.');

INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (15481, 'user05', 5, 'Absolutely loved it! Highly recommend to everyone looking for quality food.');

COMMIT;