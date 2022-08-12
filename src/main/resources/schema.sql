DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS store;
DROP TABLE IF EXISTS owner;
DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id               int AUTO_INCREMENT NOT NULL,
    email            varchar(45)        NOT NULL,
    password         varchar(70)        NOT NULL,
    name             varchar(45)        NOT NULL,
    phone            varchar(45)        NOT NULL,
    zipcode          varchar(45)        NOT NULL,
    address          varchar(45)        NOT NULL,
    create_date_time timestamp          NOT NULL,
    modify_date_time timestamp          NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (email)
);

CREATE TABLE owner
(
    id               INT(11)     NOT NULL AUTO_INCREMENT,
    email            VARCHAR(50) NOT NULL,
    password         VARCHAR(70) NOT NULL,
    name             VARCHAR(50),
    phone            VARCHAR(50),
    create_date_time VARCHAR(50),
    update_date_time VARCHAR(50),
    PRIMARY KEY (id)
);

CREATE TABLE store
(
    id               INT(11)     NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50) NOT NULL,
    phone            VARCHAR(50),
    address          VARCHAR(50),
    status           VARCHAR(50),
    description      VARCHAR(250),
    image            VARCHAR(50),
    owner_id         INT(11),
    create_date_time VARCHAR(50),
    update_date_time VARCHAR(50),
    PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES owner(id)
);

CREATE TABLE menu
(
    id               INT(11)     NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50) NOT NULL,
    price            VARCHAR(50),
    image            VARCHAR(50),
    store_id         INT(11),
    create_date_time timestamp,
    modify_date_time timestamp,
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES store(id)
);

INSERT INTO owner (id, email, password, name, phone, create_date_time, update_date_time)
VALUES (1, 'email@email.com', 'password', '사장님', '010-0000-0000', '2020-01-01 00:00:00',
        '2020-01-01 00:00:00');

INSERT INTO store (id, name, phone, address, status, description, create_date_time,
                   update_date_time, owner_id)
VALUES (1, '가게', '051-000-0000', '주소 1234567890', '영업중', '설명', '2020-01-01 00:00:00',
        '2020-01-01 00:00:00', 1);

