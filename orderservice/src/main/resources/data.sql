CREATE TABLE orders
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    product_id  VARCHAR(120) NOT NULL,
    unit_price  INT          NOT NULL,
    quantity    INT          NOT NULL,
    total_price INT          NOT NULL,
    user_id     VARCHAR(255) NOT NULL,
    order_id    VARCHAR(255) NOT NULL UNIQUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

INSERT INTO orders
    (product_id, unit_price, quantity, total_price, user_id, order_id, created_at)
VALUES
    ('product1', 1000, 10, 2000, 'user', 'order', NOW());