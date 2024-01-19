CREATE TABLE catalog
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id   VARCHAR(120) NOT NULL UNIQUE,
    product_name VARCHAR(255) NOT NULL,
    stock        INT          NOT NULL,
    unit_price   INT          NOT NULL,
    created_at   NOT NULL,
    mod_date     TIMESTAMP NULL,
);
insert into catalog(id, product_id, product_name, stock, unit_price, created_at)
values (1, 'product1', 'product1', 10, 1000, NOW());
insert into catalog(id,product_id, product_name, stock, unit_price, created_at)
values (2, 'product2', 'product2', 10, 1000, NOW());
insert into catalog(id,product_id, product_name, stock, unit_price, created_at)
values (3, 'product3', 'product3', 1, 1000, NOW());
insert into catalog(id,product_id, product_name, stock, unit_price, created_at)
values (4, 'pen', 'pen', 10, 1000, NOW());