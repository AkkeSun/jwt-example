CREATE TABLE IF NOT EXISTS USERS
(
    TABLE_INDEX  int auto_increment primary key,
    USER_NAME varchar(50)  not null,
    PASSWORD varchar(100) not null,
    HOBBY varchar(50),
    ROLE varchar(50) not null
);

CREATE TABLE IF NOT EXISTS USER_REFRESH_TOKEN
(
    TABLE_INDEX  int auto_increment primary key,
    USER_NAME varchar(50)  not null,
    USER_AGENT varchar(100) not null,
    REFRESH_TOKEN varchar(200),
    REG_DATE timestamp
);