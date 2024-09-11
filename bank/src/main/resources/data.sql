-- country 데이터 삽입 (명시적으로 컬럼을 지정)
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (1, 'KOR', 'Korea');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (2, 'USA', 'United States');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (3, 'JPN', 'Japan');

-- currency 데이터 삽입
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate) VALUES (1,'KRW', 1, 1.0);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate) VALUES (2,'USD', 2, 1200.0);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate) VALUES (3,'JPY', 3, 100.0);