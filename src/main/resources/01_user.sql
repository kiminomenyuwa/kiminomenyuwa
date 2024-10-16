delete
from user_dining_history
where user_id like '%user%';
delete
from user
where user_id like '%user%';
delete
from profile_photo
where user_id like '%user%';

-- 신일
INSERT INTO `user` (`user_id`, `password_hash`, `name`, `birth_date`, `gender`, `email`, `road_name_address`,
                    `detail_address`, `zipcode`, `phone_number`, `profile_img_uuid`, `role`, `enabled`)
VALUES ('tlsdlf9502', '$2a$10$EVkG11VJBoWUV.ZiBY5gzeiAEdZS/Ap/W1JnuHdtwzQSkVUnOhc/.', '김신일', '1995-02-02', 'male',
        'tlsdlf9502@gmail.com', '경기 성남시 분당구 판교원로 207', '507동 2803호', '13485', '010-5033-2707',
        '20241007_14bf3b18-3707-43dd-8819-27581ceee7c3.jpg', 'ROLE_USER', 1);
INSERT INTO `user` (`user_id`, `password_hash`, `name`, `birth_date`, `gender`, `email`, `road_name_address`,
                    `detail_address`, `zipcode`, `phone_number`, `profile_img_uuid`, `role`, `enabled`)
VALUES ('hyunji123', '$2a$10$CCeSuSHUiHAH48Nnu/.6De1xU0xAYJsiUqSURM1FBwnHWiOIOjl5e', '박현지', '1997-09-11', 'female',
        'ossor322@gmail.com', '경기 성남시 분당구 판교역로 4', 'OOO호', '13536', '010-1234-5678',
        '20241007_6a122e2b-5ab0-4eea-9219-4bf44b8563a8.jpg', 'ROLE_MERCHANT', 1);

-- 최재원
INSERT INTO `user` (`user_id`, `password_hash`, `name`, `birth_date`, `gender`, `email`, `road_name_address`,
                    `detail_address`, `zipcode`, `phone_number`, `profile_img_uuid`, `role`, `enabled`)
VALUES ('japol', '$2a$10$Z5T.N5kAmGdBxROPhbTfee41IgzH3ob15QuxQf9UYKJcLSc.1IZ.6', '최재원', '1995-02-11', 'male',
        'japol@naver.com', '경기 고양시 덕양구 화신로 170', '0000동 0000호', '10484', '010-9114-3194',
        '20241007_a7515e4d-eafd-4c29-9dd1-eeead167dcca.jpeg', 'ROLE_USER', 1);

-- 김희윤
INSERT INTO `user` (`user_id`, `password_hash`, `name`, `birth_date`, `gender`, `email`, `road_name_address`,
                    `detail_address`, `zipcode`, `phone_number`, `profile_img_uuid`, `role`, `enabled`)
VALUES ('khy960713', '$2a$10$pTAHt65R9ixmDFMihrnMcOiCszqiTnI4yQLZ6t55Sf/pZvm5O13Ku', '김희윤', '1996-07-13', 'male',
        'heeyoonkim07@gmail.com', '경기 성남시 분당구 판교원로 207', '507동 2803호', '13485', '010-2079-2707',
        '스크린샷 2024-06-24 163555.jpg', 'ROLE_USER', 1);

INSERT INTO `user` (`user_id`, `password_hash`, `name`, `birth_date`, `gender`, `email`, `road_name_address`,
                    `detail_address`, `zipcode`, `phone_number`, `profile_img_uuid`, `role`, `enabled`)
VALUES ('mochirong', '$2a$10$CCeSuSHUiHAH48Nnu/.6De1xU0xAYJsiUqSURM1FBwnHWiOIOjl5e', '이설인', '2004-12-24', 'female',
        'best_slytherin@hogwarts.com', 'hogwarts', '507동 2803호', '13485', '010-1234-5678',
        'GUNf3TxaEAAlcvi.jpeg', 'ROLE_USER', 1);



select *
from profile_photo;
INSERT INTO `profile_photo`(`user_id`, `original_name`, `saved_name`)
VALUES ('tlsdlf9502', '20241007_14bf3b18-3707-43dd-8819-27581ceee7c3.jpg',
        '20241007_14bf3b18-3707-43dd-8819-27581ceee7c3.jpg');
INSERT INTO `profile_photo`(`user_id`, `original_name`, `saved_name`)
VALUES ('hyunji123', '20241007_14bf3b18-3707-43dd-8819-27581ceee7c3.jpg',
        '20241007_6a122e2b-5ab0-4eea-9219-4bf44b8563a8.jpg');
INSERT INTO `profile_photo`(`user_id`, `original_name`, `saved_name`)
VALUES ('japol', '20241007_a7515e4d-eafd-4c29-9dd1-eeead167dcca.jpeg',
        '20241007_a7515e4d-eafd-4c29-9dd1-eeead167dcca.jpeg');
INSERT INTO `profile_photo`(`user_id`, `original_name`, `saved_name`)
VALUES ('khy960713', '스크린샷 2024-06-24 163555.jpg',
        '스크린샷 2024-06-24 163555.jpg');
INSERT INTO `profile_photo`(`user_id`, `original_name`, `saved_name`)
VALUES ('mochirong', 'GUNf3TxaEAAlcvi.jpeg',
        'GUNf3TxaEAAlcvi.jpeg');


select *
from user;
INSERT INTO `user` (`user_id`, `password_hash`, `name`, `birth_date`, `gender`, `email`, `road_name_address`,
                    `detail_address`, `zipcode`, `phone_number`, `profile_img_uuid`, `role`, `enabled`)
VALUES ('aaa', '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS', '김철수', '1998-10-15', 'male',
        'testuser01@example.com', '서울시 강남구', '테헤란로 123', '06234', '010-1234-5678', 'http://example.com/photo.jpg',
        'ROLE_USER', 1);
INSERT INTO `user` (`user_id`, `password_hash`, `name`, `birth_date`, `gender`, `email`, `road_name_address`,
                    `detail_address`, `zipcode`, `phone_number`, `profile_img_uuid`, `role`, `enabled`)
VALUES ('bbb', '$2a$10$kz9me5s4fHK50IGx/L.R2exLsxnfvH9msIulhpn.Em8ctaBPm3vAS', '최재원', '1995-02-11', 'male',
        'japol@naver.com', '서울 강남구 봉은사로13길 10', '26동 1011호', '06122', '070-9114-3194', 'http://example.com/photo.jpg',
        'ROLE_USER', 1);
INSERT INTO `user` (`user_id`, `password_hash`, `name`, `birth_date`, `gender`, `email`, `road_name_address`,
                    `detail_address`, `zipcode`, `phone_number`, `profile_img_uuid`, `role`, `enabled`)
VALUES ('ccc', '$2a$10$9J/wHkfdw2/4ix3ZX74ZkeFkM4BviPLbgONDNiaJDQ6dxFqUZVHFG', '카리나', '2002-06-15', 'female',
        'karina@daum.net', '서울 송파구 올림픽로4길 42', '6동 907호', '05571', '010-3333-3333', 'http://example.com/photo.jpg',
        'ROLE_USER', 1);



INSERT INTO `user` (`user_id`,
                    `password_hash`,
                    `name`,
                    `birth_date`,
                    `gender`,
                    `email`,
                    `road_name_address`,
                    `detail_address`,
                    `zipcode`,
                    `phone_number`,
                    `profile_img_uuid`,
                    `role`,
                    `enabled`,
                    `created_time`)
VALUES
-- 기존 5명 데이터
('user01',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '김하늘',
 '1999-02-23',
 'male',
 '김하늘1@example.com',
 '서울특별시 서초구 테헤란로 259',
 '380호',
 '55777',
 '010-5420-1532',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),
('user02',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '박지민',
 '1981-04-18',
 'female',
 '박지민2@example.com',
 '서울특별시 서초구 월드컵북로 403',
 '274호',
 '70461',
 '010-6898-1925',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user03',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '이수민',
 '1990-10-18',
 'male',
 '이수민3@example.com',
 '서울특별시 마포구 월드컵북로 278',
 '200호',
 '74551',
 '010-7140-3234',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user04',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '최영훈',
 '1994-05-16',
 'male',
 '최영훈4@example.com',
 '서울특별시 강남구 테헤란로 872',
 '752호',
 '32704',
 '010-7593-7389',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user05',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '정다현',
 '1990-06-02',
 'female',
 '정다현5@example.com',
 '서울특별시 강남구 테헤란로 720',
 '205호',
 '24099',
 '010-7776-1230',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user06',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '오지호',
 '1998-07-05',
 'male',
 '오지호6@example.com',
 '서울특별시 마포구 월드컵북로 642',
 '434호',
 '36210',
 '010-3839-3136',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user07',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '김민준',
 '1982-07-24',
 'male',
 '김민준7@example.com',
 '서울특별시 강남구 양재대로 227',
 '673호',
 '24779',
 '010-1861-9933',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user08',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '이서현',
 '1991-03-26',
 'male',
 '이서현8@example.com',
 '서울특별시 서초구 올림픽대로 529',
 '231호',
 '38174',
 '010-4158-3577',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user09',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '박지우',
 '1986-08-24',
 'female',
 '박지우9@example.com',
 '서울특별시 강남구 올림픽대로 784',
 '334호',
 '97218',
 '010-9779-2612',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user10',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '송다은',
 '1996-05-03',
 'male',
 '송다은10@example.com',
 '서울특별시 마포구 테헤란로 807',
 '650호',
 '74213',
 '010-7462-3764',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user11',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '조성준',
 '1995-04-11',
 'female',
 '조성준11@example.com',
 '서울특별시 마포구 월드컵북로 710',
 '323호',
 '83014',
 '010-1924-1818',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user12',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '윤지아',
 '1999-05-25',
 'female',
 '윤지아12@example.com',
 '서울특별시 서초구 올림픽대로 569',
 '935호',
 '44331',
 '010-5291-1554',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user13',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '백현우',
 '1982-02-19',
 'male',
 '백현우13@example.com',
 '서울특별시 강남구 올림픽대로 700',
 '192호',
 '65327',
 '010-3738-6454',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user14',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '강하은',
 '1985-03-08',
 'female',
 '강하은14@example.com',
 '서울특별시 마포구 올림픽대로 656',
 '200호',
 '41145',
 '010-8652-5358',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user15',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '이유진',
 '1995-04-26',
 'female',
 '이유진15@example.com',
 '서울특별시 송파구 양재대로 490',
 '212호',
 '83599',
 '010-6334-1628',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user16',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '최지후',
 '1980-01-15',
 'female',
 '최지후16@example.com',
 '서울특별시 마포구 테헤란로 653',
 '799호',
 '55153',
 '010-9740-8178',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user17',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '김수빈',
 '1981-04-15',
 'female',
 '김수빈17@example.com',
 '서울특별시 강남구 양재대로 306',
 '887호',
 '77528',
 '010-7834-5983',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user18',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '박민수',
 '1984-12-27',
 'male',
 '박민수18@example.com',
 '서울특별시 마포구 양재대로 411',
 '640호',
 '87509',
 '010-9882-4075',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user19',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '이하린',
 '1984-06-02',
 'female',
 '이하린19@example.com',
 '서울특별시 마포구 월드컵북로 818',
 '776호',
 '81134',
 '010-3147-9659',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user20',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '장민호',
 '1996-06-06',
 'female',
 '장민호20@example.com',
 '서울특별시 마포구 월드컵북로 418',
 '108호',
 '91307',
 '010-3361-1394',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user21',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '서지민',
 '2000-03-13',
 'female',
 '서지민21@example.com',
 '서울특별시 서초구 테헤란로 396',
 '940호',
 '19407',
 '010-2657-9275',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user22',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '오현수',
 '1998-06-08',
 'female',
 '오현수22@example.com',
 '서울특별시 서초구 월드컵북로 263',
 '949호',
 '31381',
 '010-6304-7848',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user23',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '이도현',
 '1989-04-26',
 'female',
 '이도현23@example.com',
 '서울특별시 강남구 테헤란로 892',
 '802호',
 '43337',
 '010-5973-6082',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user24',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '김예지',
 '1985-03-08',
 'female',
 '김예지24@example.com',
 '서울특별시 서초구 테헤란로 823',
 '216호',
 '51787',
 '010-5747-7084',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user25',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '정수민',
 '1985-04-16',
 'female',
 '정수민25@example.com',
 '서울특별시 마포구 올림픽대로 824',
 '899호',
 '86619',
 '010-9050-9749',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user26',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '한지우',
 '1981-09-05',
 'male',
 '한지우26@example.com',
 '서울특별시 서초구 올림픽대로 940',
 '298호',
 '30696',
 '010-8294-7770',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user27',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '윤지우',
 '1988-09-19',
 'male',
 '윤지우27@example.com',
 '서울특별시 마포구 양재대로 800',
 '185호',
 '58499',
 '010-5117-3085',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user28',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '이준서',
 '1997-09-01',
 'male',
 '이준서28@example.com',
 '서울특별시 송파구 올림픽대로 707',
 '330호',
 '71952',
 '010-8032-9153',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user29',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '박수민',
 '1988-02-18',
 'male',
 '박수민29@example.com',
 '서울특별시 강남구 양재대로 747',
 '503호',
 '35315',
 '010-7555-6668',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user30',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '고다현',
 '1996-10-18',
 'male',
 '고다현30@example.com',
 '서울특별시 송파구 테헤란로 698',
 '686호',
 '11698',
 '010-4693-8739',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user31',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '차수현',
 '1986-04-10',
 'female',
 '차수현31@example.com',
 '서울특별시 송파구 올림픽대로 160',
 '160호',
 '66885',
 '010-6013-9094',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user32',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '백준혁',
 '1987-09-25',
 'female',
 '백준혁32@example.com',
 '서울특별시 마포구 양재대로 434',
 '436호',
 '31933',
 '010-5724-6929',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user33',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '정현우',
 '1982-03-27',
 'male',
 '정현우33@example.com',
 '서울특별시 송파구 양재대로 385',
 '633호',
 '33137',
 '010-3923-6256',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user34',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '장지우',
 '1996-01-22',
 'male',
 '장지우34@example.com',
 '서울특별시 서초구 양재대로 360',
 '438호',
 '97421',
 '010-5533-2189',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user35',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '서수민',
 '1992-11-16',
 'female',
 '서수민35@example.com',
 '서울특별시 송파구 월드컵북로 920',
 '506호',
 '22961',
 '010-9548-5237',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user36',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '최서준',
 '1990-02-01',
 'female',
 '최서준36@example.com',
 '서울특별시 송파구 월드컵북로 490',
 '108호',
 '11927',
 '010-6909-6703',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user37',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '김도윤',
 '2000-11-23',
 'male',
 '김도윤37@example.com',
 '서울특별시 서초구 테헤란로 692',
 '450호',
 '63173',
 '010-3622-9593',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user38',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '강예은',
 '1997-02-12',
 'female',
 '강예은38@example.com',
 '서울특별시 강남구 테헤란로 940',
 '283호',
 '65290',
 '010-5547-7385',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user39',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '조지호',
 '1994-07-01',
 'female',
 '조지호39@example.com',
 '서울특별시 마포구 올림픽대로 997',
 '987호',
 '40822',
 '010-4560-5487',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user40',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '오지후',
 '1981-09-02',
 'female',
 '오지후40@example.com',
 '서울특별시 마포구 양재대로 642',
 '213호',
 '81459',
 '010-1044-1017',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user41',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '이유나',
 '1997-06-11',
 'female',
 '이유나41@example.com',
 '서울특별시 강남구 양재대로 154',
 '127호',
 '60471',
 '010-9561-1966',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user42',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '윤하은',
 '1990-08-18',
 'female',
 '윤하은42@example.com',
 '서울특별시 강남구 테헤란로 924',
 '263호',
 '27116',
 '010-8307-1407',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user43',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '김태우',
 '1995-05-12',
 'male',
 '김태우43@example.com',
 '서울특별시 강남구 테헤란로 267',
 '342호',
 '48698',
 '010-9862-6882',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user44',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '박다현',
 '1990-02-13',
 'male',
 '박다현44@example.com',
 '서울특별시 강남구 월드컵북로 462',
 '556호',
 '75048',
 '010-4143-5469',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user45',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '정재민',
 '1998-08-13',
 'male',
 '정재민45@example.com',
 '서울특별시 강남구 올림픽대로 259',
 '463호',
 '41971',
 '010-1371-5217',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user46',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '이소민',
 '1984-04-06',
 'female',
 '이소민46@example.com',
 '서울특별시 마포구 테헤란로 102',
 '554호',
 '77387',
 '010-7641-3302',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user47',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '백수현',
 '1988-06-04',
 'male',
 '백수현47@example.com',
 '서울특별시 서초구 월드컵북로 880',
 '249호',
 '89070',
 '010-6691-1538',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user48',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '장서윤',
 '1989-01-24',
 'female',
 '장서윤48@example.com',
 '서울특별시 송파구 양재대로 112',
 '352호',
 '38456',
 '010-5951-8114',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user49',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '한서준',
 '2000-11-28',
 'male',
 '한서준49@example.com',
 '서울특별시 마포구 양재대로 186',
 '717호',
 '93333',
 '010-4369-8648',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user50',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '고다연',
 '1995-06-19',
 'male',
 '고다연50@example.com',
 '서울특별시 강남구 월드컵북로 852',
 '604호',
 '77900',
 '010-7441-4713',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user51',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '김하늘',
 '1986-03-24',
 'female',
 '김하늘51@example.com',
 '서울특별시 강남구 월드컵북로 412',
 '369호',
 '88004',
 '010-5643-1308',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user52',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '박지민',
 '1983-01-07',
 'female',
 '박지민52@example.com',
 '서울특별시 마포구 올림픽대로 290',
 '65호',
 '29186',
 '010-6100-8619',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user53',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '이수민',
 '1987-05-27',
 'female',
 '이수민53@example.com',
 '서울특별시 서초구 올림픽대로 821',
 '673호',
 '87782',
 '010-7299-1732',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user54',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '최영훈',
 '1994-10-04',
 'female',
 '최영훈54@example.com',
 '서울특별시 송파구 올림픽대로 518',
 '362호',
 '76367',
 '010-8411-3441',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user55',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '정다현',
 '1983-06-25',
 'male',
 '정다현55@example.com',
 '서울특별시 서초구 올림픽대로 488',
 '822호',
 '21424',
 '010-8310-3754',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user56',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '오지호',
 '1982-12-21',
 'male',
 '오지호56@example.com',
 '서울특별시 서초구 월드컵북로 107',
 '608호',
 '53462',
 '010-9116-6641',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user57',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '김민준',
 '1980-03-26',
 'male',
 '김민준57@example.com',
 '서울특별시 강남구 월드컵북로 280',
 '744호',
 '75201',
 '010-7209-5918',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user58',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '이서현',
 '1997-10-01',
 'female',
 '이서현58@example.com',
 '서울특별시 송파구 월드컵북로 946',
 '61호',
 '16671',
 '010-5367-1708',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user59',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '박지우',
 '1993-03-03',
 'female',
 '박지우59@example.com',
 '서울특별시 송파구 양재대로 402',
 '38호',
 '91536',
 '010-5848-4047',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP),

('user60',
 '$2a$10$IqLsIOXCXOcJjMg2Gz7gW.vEBJP02BfIT6kWfcBVdWkraI5/Y.1JS',
 '송다은',
 '1997-08-13',
 'female',
 '송다은60@example.com',
 '서울특별시 마포구 월드컵북로 695',
 '336호',
 '87181',
 '010-4977-7419',
 '111',
 'ROLE_USER',
 1,
 CURRENT_TIMESTAMP);

INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user01', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user02', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user03', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user04', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user05', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user06', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user07', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user08', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user09', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user10', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user11', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user12', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user13', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user14', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user15', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user16', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user17', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user18', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user19', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user20', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user21', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user22', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user23', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user24', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user25', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user26', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user27', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user28', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user29', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user30', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user31', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user32', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user33', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user34', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user35', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user36', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user37', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user38', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user39', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user40', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user41', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user42', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user43', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user44', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user45', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user46', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user47', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user48', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user49', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user50', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user51', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user52', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user53', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user54', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user55', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user56', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user57', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user58', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user59', '111.jpg', '111.jpg');
INSERT INTO `profile_photo` (`user_id`, `original_name`, `saved_name`)
VALUES ('user60', '111.jpg', '111.jpg');

-- 친구 관계 추가
INSERT INTO friendships (user_id, friend_id, status)
VALUES ('user01', 'user02', 'ACCEPTED'),
       ('user01', 'user03', 'ACCEPTED'),
       ('user02', 'user03', 'ACCEPTED'),
       ('user02', 'user04', 'ACCEPTED'),
       ('user03', 'user04', 'PENDING'),
       ('user04', 'user05', 'ACCEPTED'),
       ('user05', 'user06', 'ACCEPTED'),
       ('user06', 'user07', 'ACCEPTED'),
       ('user07', 'user08', 'PENDING'),
       ('user08', 'user09', 'ACCEPTED'),
       ('user09', 'user10', 'DECLINED'),
       ('user01', 'user05', 'ACCEPTED'),
       ('user02', 'user06', 'BLOCKED'),
       ('user03', 'user07', 'ACCEPTED'),
       ('user04', 'user08', 'PENDING'),
       ('user05', 'user09', 'ACCEPTED'),
       ('user06', 'user10', 'DECLINED');