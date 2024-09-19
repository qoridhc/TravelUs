-- country 데이터 삽입 (명시적으로 컬럼을 지정)
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (1, 'KOR', 'Korea');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (2, 'USA', 'United States');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (3, 'JPN', 'Japan');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (4, 'EUR', 'European Union');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (5, 'CNY', 'China');

-- currency 데이터 삽입
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate) VALUES ('1','KRW', 1, 1.0);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate) VALUES ('2','USD', 2, 1200.0);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate) VALUES ('3','JPY', 3, 100.0);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate) VALUES ('4','EUR', 4, 1400.0);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate) VALUES ('5','CNY', 5, 180.0);

-- merchant 데이터 삽입
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng) VALUES (1,'입금','BANK','주소',0,0);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng) VALUES (2,'출금','BANK','주소',0,0);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng) VALUES (3,'이체 입금','BANK','주소',0,0);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng) VALUES (4,'이체 출금','BANK','주소',0,0);

-- user 데이터 삽입
INSERT IGNORE INTO `user` (user_id, email, role, is_exit, create_at, update_at, exit_at) VALUES (1, 'heo_dongwon@naver.com', 'ADMIN', null, null, null, null);