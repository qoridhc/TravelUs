-- country 데이터 삽입 (명시적으로 컬럼을 지정)
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (1, 'KOR', 'Korea');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (2, 'USA', 'United States');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (3, 'JPN', 'Japan');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (4, 'EUR', 'European Union');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (5, 'CNY', 'China');

-- currency 데이터 삽입
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate, exchange_min) VALUES ('1','KRW', 1, 1.0, 1);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate, exchange_min) VALUES ('2','USD', 2, 1200.0, 100);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate, exchange_min) VALUES ('3','JPY', 3, 100.0, 100);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate, exchange_min) VALUES ('4','EUR', 4, 1400.0, 100);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate, exchange_min) VALUES ('5','CNY', 5, 180.0, 800);

-- merchant 데이터 삽입
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng) VALUES (1,'입금','BANK','주소',0,0);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng) VALUES (2,'출금','BANK','주소',0,0);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng) VALUES (3,'이체 입금','BANK','주소',0,0);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng) VALUES (4,'이체 출금','BANK','주소',0,0);

-- user 데이터 삽입
INSERT IGNORE INTO user (user_id, credential_id, role, is_exit, create_at, update_at, exit_at) VALUES (1, 'heo_dongwon@naver.com', 'ADMIN', null, null, null, null);
INSERT IGNORE INTO user (user_id, credential_id, role, is_exit, create_at, update_at, exit_at) VALUES (2, '2pearl522@gmail.com', 'ADMIN', null, null, null, null);
INSERT IGNORE INTO user (user_id, credential_id, role, is_exit, create_at, update_at, exit_at) VALUES (3, 'lsh@gmail.com', 'ADMIN', null, null, null, null);

-- api_key 데이터 삽입
INSERT IGNORE INTO tunabank.api_key (id, create_at, expire_at, status, type, value, user_id) VALUES (1, '2024-09-18 22:06:16.282217', '2025-09-18 22:06:16.282217', 'ACTIVE', 'API', 'HUo52t-HWD_BYTDPMtnloxnppmSoRrBfduY6XYjnyC2-a84p9xxJwB_RKinE6WvQ', 1);

-- bank 데이터 삽입
INSERT IGNORE INTO bank (bank_id, bank_name) VALUES (1, '튜나은행');

-- card_product 데이터 삽입
INSERT IGNORE INTO card_product(card_product_id,card_name, card_description, card_unique_no) VALUES (1, '튜나 카드', '튜나뱅크의 카드입니다.', 'DC5O2YKQ');