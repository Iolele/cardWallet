INSERT INTO user (email,login_token, password, is_active)
VALUES ('geir.karlsen@hotmail.com','12345678901234567890', '$2a$10$M7NO5E0MexfYqqqmZK1QROKeZS1U8HJbXpTcdoB7zv6l6ZPXOH652', true );-- 'CoolDad1'

INSERT INTO user (email, login_token, password, is_active)
VALUES ('elise.norvik@hotmail.com', '09876543210897654321', '$2a$10$M7NO5E0MexfYqqqmZK1QROKeZS1U8HJbXpTcdoB7zv6l6ZPXOH652', true);

INSERT INTO user (email, login_token, password, is_active)
VALUES ('nils.nordmann@hotmail.com', '11111222223333344444', '$2a$10$VQzuRHEplm/oyqp1uyJc0eghTjPuTJOC/A3RG3tOJeXxiQlf27eqG', true);

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('Jernia', '12345678', 50050, 1, TO_DATE('22.12.2020', 'DD/MM/YYYY'), 'Logo image URL');

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('Clas Ohlson', '12345543', 82299, 1, TO_DATE('02/02/2022', 'dd/mm/yyyy'), '(Insert image URL here)');

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('Intersport', '9934356738', 90000, 1, TO_DATE('13/05/2022', 'dd/mm/yyyy'), '(Insert image URL here)');

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('hm', '87654321', 75000, 2, TO_DATE('19/09/2022', 'dd/mm/yyyy'), '(Insert image URL here)');

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('Intersport', '87654321', 40000, 2, TO_DATE('21/04/2021', 'dd/mm/yyyy'), '(Insert image URL here)');

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('Cubus', '87654321', 350, 2, TO_DATE('04/03/2020', 'dd/mm/yyyy'), '(Insert image URL here)');

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('Jernia', '12345678', 50050, 3, TO_DATE('22.12.2020', 'DD/MM/YYYY'), 'Logo image URL');

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('Clas Ohlson', '12345543', 82299, 3, TO_DATE('02/02/2019', 'dd/mm/yyyy'), '(Insert image URL here)');

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('Intersport', '9934356738', 90000, 3, TO_DATE('13/03/2020', 'dd/mm/yyyy'), '(Insert image URL here)');

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('Nille', '87654321', 70000, 3, TO_DATE('19/09/2021', 'dd/mm/yyyy'), '(Insert image URL here)');

INSERT INTO card (store_name, card_code, balance_int, user_id, expiry_date, logo_image)
VALUES ('Cubus', '87654321', 70000, 3, TO_DATE('29/03/2020', 'dd/mm/yyyy'), '(Insert image URL here)');


