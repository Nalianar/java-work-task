INSERT INTO user(id, login, password, token) VALUES (1, 'vasya', '', 't1');
INSERT INTO user(id, login, password, token) VALUES (2, 'jon seno', '', 't2');
INSERT INTO card(id, currency, money, user_id) VALUES (1, 'USD', 1, 1);
INSERT INTO card(id, currency, money, user_id) VALUES (2, 'EUR', 10, 1);
INSERT INTO card(id, currency, money, user_id) VALUES (3, 'GBP', 0.5, 1);
INSERT INTO card(id, currency, money, user_id) VALUES (4, 'USD', 2, 2);
INSERT INTO card(id, currency, money, user_id) VALUES (5, 'EUR', 20, 2);
INSERT INTO card(id, currency, money, user_id) VALUES (6, 'GBP', 1, 2);
INSERT INTO transaction(id, transaction_amount, recipient_card_id, sender_card_id) VALUES (1, 111, 1, 3)
INSERT INTO transaction(id, transaction_amount, recipient_card_id, sender_card_id) VALUES (2, 222, 2, 3)
INSERT INTO transaction(id, transaction_amount, recipient_card_id, sender_card_id) VALUES (3, 333, 3, 1)
