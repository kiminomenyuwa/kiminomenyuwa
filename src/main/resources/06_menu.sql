delete
from menu;
-- 이마트 테스트 데이터
INSERT INTO `menu`
VALUES
('1000', '5831', 'A3리필속지(20매)', '1300', 'https://m.ppbank.co.kr/data/goodsold/detail/50.jpg', 1),
('1001', '5831', '합지D링3공바인다(70mm)', '2500', 'https://gdimg.gmarket.co.kr/1813362616/still/400?ver=1590115832', 1);

-- 맥도날드코엑스점
INSERT INTO `menu`
VALUES
    ('1101', '7947', '빅맥 세트', '7100', 'https://ldb-phinf.pstatic.net/20220708_127/1657245800032ACyys_PNG/1.png', 1),
    ('1102', '7947', '맥스파이시 상하이 버거 세트', '7900', 'https://ldb-phinf.pstatic.net/20220708_255/16572459143724zjSF_PNG/2.png', 1),
    ('1103', '7947', '베이컨 토마토 디럭스 세트', '8300', 'https://ldb-phinf.pstatic.net/20200402_43/1585799390626dW1Xz_PNG/vjxntRY-WVCtrQJvmaheg4tw.png', 1),
    ('1104', '7947', '불고기 버거 세트', '6500', 'https://ldb-phinf.pstatic.net/20200402_172/1585799362554N1dIv_PNG/sMVv9eKeoqUbkcDCXZCpABKk.png', 1),
    ('1105', '7947', '더블 쿼터파운더 치즈', '5000', 'https://www.mcdonalds.co.kr/upload/product/pcfile/1583727501907.png', 1),
    ('1106', '7947', '슈비 버거', '4500', 'https://www.mcdonalds.co.kr/upload/product/pcfile/1583727918778.png', 1);

-- 반포식스포스코사거리점
INSERT INTO `menu`
VALUES
    ('1201', '20025', '스프링 롤', '5500', 'https://ldb-phinf.pstatic.net/20210621_12/16242634964184Wv9s_JPEG/nYQvcst2R6Bh0Ms1BkAcBNsQ.JPG.jpg', 1),
    ('1202', '20025', '반포쌀국수', '8000', 'https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyNDA0MjZfMjUy%2FMDAxNzE0MTI5OTQzMTI4.0vNqgTdNqlDpnNTtyw18tsiaNUf9qJ7ausHvXFCzeasg.4IDSoEkadQaEZBJ08lsZvxwaK-b7zr7a30r1j8sjhYog.JPEG%2FCE48549A-03DC-4E58-8C69-0C27024302C5.jpeg%3Ftype%3Dw1500_60_sharpen', 1),
    ('1203', '20025', '태국식 새우 너겟', '6500', 'https://ldb-phinf.pstatic.net/20210621_271/1624263508134uljKk_JPEG/u_lQMEP6_gkLMEu0de_c4zk3.JPG.jpg', 1),
    ('1204', '20025', '분짜 샐러드', '19000', 'https://ldb-phinf.pstatic.net/20220207_159/1644209646951bbHzB_JPEG/%BA%D0%C2%A5%BB%F8%B7%AF%B5%E5.JPG', 1),
    ('1205', '20025', '나시고랭', '12000', 'https://d12zq4w4guyljn.cloudfront.net/750_750_20231212132647_photo2_376c99c8dd30.jpg', 1);

-- 기기커피
INSERT INTO `menu`
VALUES
    ('1301', '20062', '아메리카노(할로)', '3300', 'http://imagefarm.baemin.com/smartmenuimage/upload/image/2024/4/16/LHhfoBoyMfI3JKlFA9xShsD1CBEHEWrgqOnl-tqT2H946DMsaYC6kKQfc7l6zvsnTKMDi5MIUd9x7uIA9zh2rA==.jpg', 1),
    ('1302', '20062', '프랜치 순수우유 쿠키슈', '5000', 'http://imagefarm.baemin.com/smartmenuimage/upload/image/2024/7/1/AkcYPo2A3LnCgiWiJcmcc91alL8CLqmYv6qnYqi6eJW8M15MN7cdahx0Fr_IlTvGqCtTV817lA5j3dM5J7Gtyw==.jpg', 1),
    ('1303', '20062', '수박 크러쉬 주스', '28000', 'http://imagefarm.baemin.com/smartmenuimage/upload/image/2024/5/9/LovJOcuWp57bUYtRj3Zbfd0K-VhbyvhcV-EO0iv2B4HiiHcV1_7Dr6H2ObfQ_g8FOHyrnzGshNUFyRuxWi7pTw==.jpg', 1),
    ('1304', '20062', '콜드브루 아메리카노', '4300', 'http://imagefarm.baemin.com/smartmenuimage/upload/image/2024/1/19/zJxR401OnFKfgGr3W68yyzHB1Q2KVaujQHpjBEdEorhcBGcIbpiiZB7vjyDkyi6tv-_n6DhwUPlyC_F9QLuDrA==.jpg', 1),
    ('1305', '20062', '딸기 초코 라떼', '4900', 'http://imagefarm.baemin.com/smartmenuimage/upload/image/2024/6/20/fdP6CvUrXj8BMOEV8PVxS8dR9Crh8ERsYalnrXJ95gMHP5Ql3QW6BnWoo0hl3GWaTmqXfturDjbPrLpgqDwhgg==.jpg', 1);

INSERT INTO `menu`
VALUES
    ('', '', '', '', '', 1);

SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE menu MODIFY COLUMN menu_id INT NOT NULL AUTO_INCREMENT;
SET FOREIGN_KEY_CHECKS = 1;
