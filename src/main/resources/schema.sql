CREATE TABLE user(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
email VARCHAR(254) NOT NULL,
password VARCHAR(100) NOT NULL,
login_token VARCHAR(20) UNIQUE NOT NULL,
is_active BOOLEAN NOT NULL
);

CREATE TABLE card(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
store_name VARCHAR(100) NOT NULL,
card_code VARCHAR(40) NOT NULL,
expiry_date DATE,
balance_int INT,
logo_image VARCHAR(255),
user_id BIGINT NOT NULL,
FOREIGN KEY (user_id) REFERENCES user(id)
);


