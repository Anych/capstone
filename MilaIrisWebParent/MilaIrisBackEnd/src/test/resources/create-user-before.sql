SET foreign_key_checks = 0;

TRUNCATE TABLE users_roles;
TRUNCATE TABLE users;
TRUNCATE TABLE roles;

INSERT INTO users(id, email, password, first_name, last_name, photos, enabled) VALUES
(1, 'test@test.com', '$2a$10$zBKHN1FibRtAjXiyzk0e.eTj6vxkmA5yI5IK/B.txN1qZMiHFfh8K', 'Test', 'Test', 'default.png', 1),
(2, 'test1@test1.com', '$2a$10$zBKHN1FibRtAjXiyzk0e.eTj6vxkmA5yI5IK/B.txN1qZMiHFfh8K', 'Test1', 'Test1', 'default.png', 0);

INSERT INTO roles(id, name, description) VALUES
(1, 'Admin', 'manage everything'),
(2, 'Salesperson', 'manage product price, customers, shipping, orders and sales report'),
(3, 'Editor', 'manage categories, brands, products, articles and menus'),
(4, 'Shipper', 'view products, view orders and update order status'),
(5, 'Assistant', 'manage questions and reviews');

INSERT INTO users_roles(role_id, user_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1);

SET foreign_key_checks = 1;