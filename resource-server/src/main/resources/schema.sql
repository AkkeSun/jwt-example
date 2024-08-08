CREATE TABLE IF NOT EXISTS PRODUCTS
(
    TABLE_INDEX  int auto_increment primary key,
    PRODUCT_NAME varchar(100)  not null,
    PRICE int not null,
    DESCRIPTION varchar(200),
    ROLE varchar(50) not null
);
