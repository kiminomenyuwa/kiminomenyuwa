START TRANSACTION;

INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (13208, 'user01', 5, 'Amazing experience! The food was delicious and the staff was friendly.');

INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (13208, 'user02', 4, 'Great place to visit. The ambiance was nice, and the menu had a good variety.');

INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (13208, 'user03', 3, 'It was okay. Some dishes were tasty, but others could be improved.');

INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (13208, 'user04', 2, 'Not very satisfied. The service was slow and the food was mediocre.');

INSERT INTO `review` (`store_id`, `user_id`, `rating`, `comment`)
VALUES (13208, 'user05', 5, 'Absolutely loved it! Highly recommend to everyone looking for quality food.');

COMMIT;