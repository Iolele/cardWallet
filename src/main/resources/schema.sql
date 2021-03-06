CREATE TABLE app_user(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
email VARCHAR(254) NOT NULL,
password VARCHAR(100) NOT NULL,
login_token VARCHAR(20) UNIQUE NOT NULL,
is_active BOOLEAN NOT NULL
);

CREATE TABLE gift_card(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
store_name VARCHAR(100) NOT NULL,
card_code VARCHAR(40) NOT NULL,
expiry_date DATE,
balance_int INT,
logo_image VARCHAR(255),
app_user_id BIGINT,
FOREIGN KEY (app_user_id) REFERENCES app_user(id)
);


